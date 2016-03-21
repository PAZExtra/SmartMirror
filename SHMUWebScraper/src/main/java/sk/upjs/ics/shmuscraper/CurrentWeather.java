package sk.upjs.ics.shmuscraper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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
	 * Zaznam o aktualnom stave na nejakej stanici
	 */
	public static class Station {

		/**
		 * Nazov stanice.
		 */
		public String name;
		public Integer temperature;
		public String windDirection;
		public Integer windSpeed;
		public Integer gustSpeed;
		public String cloudiness;
		public String weatherSpecification;

		/**
		 * Kod ikonky stavu pocasia (null, ak nie je definovany)
		 */
		public Integer iconCode;

		/**
		 * Interny identifikator stanice (vyskytuje sa v URL ako parameter ii)
		 */
		private Integer iiCode;
	}

	/**
	 * Datum a cas, ktoreho stav zachycuje aktualne pocasie.
	 */
	private LocalDateTime dateTime;

	/**
	 * Zoznam s aktualnym pocasim v jednotlivych staniciach
	 */
	private final List<Station> stations = new ArrayList<>();

	/**
	 * Aktualizuje stav pocasia a vrati, ci sa aktualizacia podarila.
	 * 
	 * @return true, ak sa aktualizacia podarila, false inak.
	 */
	public boolean update() {

		dateTime = null;
		stations.clear();

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
			String tableTitle = mainContentElement.select("h3").first().text();

			findDateTime(tableTitle);

			// Vydolujeme udaje z jednotlivych stanic
			Element tbodyElement = mainContentElement.select("tbody").first();

			for (Element trElement : tbodyElement.select("tr")) {

				Elements cells = trElement.select("td");

				int pocitadlo = 0;

				Station station = new Station();

				station.name = cells.get(pocitadlo++).text().trim();
				station.temperature = getTemperatureAsInt(cells.get(pocitadlo++).text().trim());
				station.windDirection = cells.get(pocitadlo++).text().trim();
				station.windSpeed = getWindSpeedAsInt(cells.get(pocitadlo++).text().trim());
				station.gustSpeed = getWindSpeedAsInt(cells.get(pocitadlo++).text().trim());
				station.cloudiness = cells.get(pocitadlo++).text().trim();
				station.weatherSpecification = cells.get(pocitadlo++).text().trim();

				Elements tdElement = trElement.select("td > a");
				station.iiCode = getIICodeAsInt(tdElement.attr("href"));

				stations.add(station);
			}
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_TABLE_URL + ".");
			throw e;
		}
	}

	private Integer getIICodeAsInt(String link) {

		try {
			int indexRovnasa = link.lastIndexOf("=");

			if (link.endsWith("sk"))
				return null;

			link = link.substring(indexRovnasa + 1).trim();

			return Integer.parseInt(link);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa ziskat II code, sme luzri");
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
			System.err.println("Nepodarilo sa rozparsovat rychlost vetra, co je fakt smutne.");
		}

		return null;
	}

	private Integer getTemperatureAsInt(String teplota) {

		try {
			int indexGulicky = teplota.indexOf('°');

			teplota = teplota.substring(0, indexGulicky).trim();

			return Integer.parseInt(teplota);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat teplotu, co je fakt smutne.");
		}

		return null;
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
				sc.useDelimiter(Pattern.compile(":"));
				hodina = sc.nextInt();
				minuta = sc.nextInt();
			}

			dateTime = LocalDateTime.of(rok, mesiac, den, hodina, minuta);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat datum a cas.");
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
			Element mapOverlayUl = doc.getElementById("mainmap-v2");

			Elements iconLinks = mapOverlayUl.select("li > a");

			for (int i = 1; i < iconLinks.size(); i = i + 2) {

				Element iconLink = iconLinks.get(i);
				String href = iconLink.attr("href");
				Element img = iconLink.select("img").first();
				String iconSrc = (img != null) ? img.attr("src") : null;

				// kod ikony (v atribute src elementu img) prepojime so
				// stanicou podla parametra ii v href
				Integer iiCode = getIICodeAsInt(href);
				Station station = getStationFromCode(iiCode);

				if (station == null)
					continue;

				station.iconCode = getIconCodeAsInteger(iconSrc);
			}
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_MAP_URL + ".");
			throw e;
		}
	}

	private Integer getIconCodeAsInteger(String iconSrc) {

		try {
			int indexLomitka = iconSrc.lastIndexOf("/");

			int indexBodky = iconSrc.lastIndexOf(".");

			iconSrc = iconSrc.substring(indexLomitka + 1, indexBodky);

			return Integer.parseInt(iconSrc);
		}

		catch (RuntimeException e) {
			System.err.println("Nepodarilo sa ziskat kod ikonky. Zial...");
		}

		return null;
	}

	private Station getStationFromCode(Integer iiCode) {

		if (iiCode == null)
			return null;

		for (Station station : stations)
			if (station.iiCode.equals(iiCode))
				return station;

		return null;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public List<Station> getStations() {
		return stations;
	}
}