package sk.upjs.ics.shmuscraper;

import java.time.LocalDateTime;
import java.util.Scanner;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sk.upjs.ics.utilities.Citac;
import sk.upjs.ics.utilities.StringUtilities;

public class CitacTabulky extends Citac {

	private static CitacTabulky instance = new CitacTabulky();

	/**
	 * URL stranky s tabulkou s aktualnym pocasim
	 */
	public static final String CURRENT_WEATHER_TABLE_URL = "http://www.shmu.sk/sk/?page=1&id=meteo_apocasie_sk";

	private Stations stanice;

	@Override
	protected void update() {
		precitajTabulku();
	}

	Stations nacitajPocasie() throws Exception {
		stanice = new Stations();
		read(CURRENT_WEATHER_TABLE_URL);
		return stanice;
	}

	private void precitajTabulku() {

		Element mainContentElement = doc.getElementById("maincontent");

		LocalDateTime casAktualizacie = parseDateTime(mainContentElement.select("h3").first().text());

		stanice.setCasAktualizacie(casAktualizacie);

		Elements trElements = mainContentElement.select("tbody").first().select("tr");

		for (Element trElement : trElements) {

			Station station = new Station();

			String iiCode = trElement.select("a").first().attr("href");

			station.setIiCode(getIICodeAsInt(iiCode));

			Elements cells = trElement.select("td");

			int i = 0;

			station.setName(StringUtilities.parseEmptyStringToNull(cells.get(i++).text()));
			station.setTemperature(getTemperatureAsInt(cells.get(i++).text()));
			station.setWindDirection(StringUtilities.parseEmptyStringToNull(cells.get(i++).text()));
			station.setWindSpeed(getWindSpeedAsInt(cells.get(i++).text()));
			station.setGustSpeed(getWindSpeedAsInt(cells.get(i++).text()));
			station.setCloudiness(StringUtilities.parseEmptyStringToNull(cells.get(i++).text()));
			station.setWeatherSpecification(StringUtilities.parseEmptyStringToNull(cells.get(i++).text()));

			stanice.addStation(station);
		}
	}

	private LocalDateTime parseDateTime(String tableTitle) {

		try {
			int den = 0;
			int mesiac = 0;
			int rok = 0;
			int hodina = 0;
			int minuta = 0;

			int indexPomlcky = tableTitle.indexOf("-");
			int poslednyIndex = tableTitle.lastIndexOf("-");

			String datum = tableTitle.substring(indexPomlcky + 2, poslednyIndex - 1);
			String cas = tableTitle.substring(poslednyIndex + 2, poslednyIndex + 7);

			try (Scanner sc = new Scanner(datum)) {
				sc.useDelimiter("\\.");
				den = sc.nextInt();
				mesiac = sc.nextInt();
				rok = sc.nextInt();
			}

			try (Scanner sc = new Scanner(cas)) {
				sc.useDelimiter(":");
				hodina = sc.nextInt();
				minuta = sc.nextInt();
			}

			return LocalDateTime.of(rok, mesiac, den, hodina, minuta);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat datum a cas.");
		}

		return null;
	}

	private Integer getIICodeAsInt(String link) {

		try {
			int indexRovnasa = link.lastIndexOf("=");

			link = link.substring(indexRovnasa + 1).trim();

			return Integer.parseInt(link);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa ziskat II code.");
		}

		return null;
	}

	private Integer getWindSpeedAsInt(String speed) {

		try {
			int indexM = speed.indexOf('m');

			if (indexM == -1)
				return null;

			speed = speed.substring(0, indexM).trim();

			return Integer.parseInt(speed);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat rychlost vetra alebo narazov.");
		}

		return null;
	}

	private Integer getTemperatureAsInt(String teplota) {

		try {
			int indexGulicky = teplota.indexOf('°');

			teplota = teplota.substring(0, indexGulicky).trim();

			return Integer.parseInt(teplota);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat teplotu.");
		}

		return null;
	}

	static CitacTabulky getInstance() {
		return instance;
	}
}