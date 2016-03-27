package sk.upjs.ics.shmuscraper;

import java.time.LocalDateTime;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CurrentWeather {

	/**
	 * URL stranky s tabulkou s aktualnym pocasim
	 */
	public static final String CURRENT_WEATHER_TABLE_URL = "http://www.shmu.sk/sk/?page=1&id=meteo_apocasie_sk";

	/**
	 * URL stranky s mapkou s aktualnym pocasim
	 */
	public static final String CURRENT_WEATHER_MAP_URL = "http://www.shmu.sk/sk/?page=1&id=meteo_gapocasie_sk";

	/**
	 * Datum a cas, ktoreho stav zachycuje aktualne pocasie.
	 */
	private LocalDateTime dateTime;

	/**
	 * Zoznam s aktualnym pocasim v jednotlivych staniciach
	 */
	private final Stations stanice = new Stations();

	/**
	 * Aktualizuje stav pocasia a vrati, ci sa aktualizacia podarila.
	 * 
	 * @return true, ak sa aktualizacia podarila, false inak.
	 */
	public boolean update() {

		dateTime = null;
		stanice.clear();

		try {
			readAndParseTable();
			readIconInfoFromMap();
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	private void readAndParseTable() throws Exception {

		Document doc;

		try {
			doc = Jsoup.connect(CURRENT_WEATHER_TABLE_URL).get();
		}

		catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + CURRENT_WEATHER_TABLE_URL + " sa nepodarilo.");
			throw e;
		}

		try {
			Element mainContentElement = doc.getElementById("maincontent");

			// Vydolujeme informaciu, z akeho datumu a casu su aktualne zaznamy
			findDateTime(mainContentElement.select("h3").first().text());

			// Vydolujeme udaje z jednotlivych stanic
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
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_TABLE_URL + ".");
			throw e;
		}
	}

	/**
	 * Zaznamy stanic rozsirime o obrazkove kody stavu pocasia (iconCode).
	 * Parsovanie robime podla hodnoty ii v html.
	 */
	private void readIconInfoFromMap() throws Exception {

		Document doc;

		try {
			doc = Jsoup.connect(CURRENT_WEATHER_MAP_URL).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + CURRENT_WEATHER_MAP_URL + " sa nepodarilo.");
			throw e;
		}

		try {

			Elements iconLinks = doc.getElementById("mainmap-v2").select("a");

			for (int i = 1; i < iconLinks.size(); i = i + 2) {

				Element iconLink = iconLinks.get(i);

				Integer iiCode = getIICodeAsInt(iconLink.attr("href"));

				Station station = stanice.getStationFromCode(iiCode);

				if (station == null)
					continue;

				String linkOfIcon = "www.shmu.sk" + iconLink.select("img").attr("src");

				station.setIconLink(linkOfIcon);
			}
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_MAP_URL + ".");
			throw e;
		}
	}

	private void findDateTime(String tableTitle) {

		// Aktuálny stav počasia - 20.03.2016 - 21:00 SEČ

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

			dateTime = LocalDateTime.of(rok, mesiac, den, hodina, minuta);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat datum a cas.");
		}
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

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public Stations getStations() {
		return stanice;
	}

	public Station getKosice() {
		return stanice.getByName("Košice");
	}
}