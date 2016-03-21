package sk.upjs.ics.shmuscraper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
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
		public Integer windSpeed;
		public Integer gustSpeed;

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
		} catch (Exception e) {
			return false;
		}
	}

	private void readAndParseTable() throws Exception {
		Document doc;
		try {
			doc = Jsoup.connect(CURRENT_WEATHER_TABLE_URL).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + CURRENT_WEATHER_TABLE_URL + " sa nepodarilo.");
			throw e;
		}

		try {
			Element mainContentElement = doc.getElementById("maincontent");

			// Vydolujeme informaciu, z akeho datumu a casu su aktualne zaznamy
			// TODO rozparsovat a nastavit dateTime tak, aby uchovaval cas z
			// akeho mame informacie
			String tableTitle = mainContentElement.select("h3").first().text();

			// Vydolujeme udaje z jednotlivych stanic
			Element tbodyElement = mainContentElement.select("tbody").first();
			for (Element trElement : tbodyElement.select("tr")) {
				Elements cells = trElement.select("td");
				Station station = new Station();
				station.name = cells.get(0).text().trim();
				// TODO dalsie vlasnosti

				stations.add(station);
			}
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_TABLE_URL + ".");
			throw e;
		}
	}

<<<<<<< HEAD
=======
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
			int indexGulicky = teplota.indexOf('�');

			teplota = teplota.substring(0, indexGulicky).trim();

			return Integer.parseInt(teplota);

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa rozparsovat teplotu, co je fakt smutne.");
		}

		return null;
	}

	private void findDateTime(String tableTitle) {

		// Aktu�lny stav po�asia - 20.03.2016 - 21:00 SE�

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

>>>>>>> origin/master
	/**
	 * Zaznamy stanic rozsirime o obrazkove kody stavu pocasia (iconCode).
	 * Parovanie robime podla hodnoty ii v html.
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
			for (Element iconLink : iconLinks) {
				String href = iconLink.attr("href");
				Element img = iconLink.select("img").first();
				String iconSrc = (img != null) ? img.attr("src") : null;
				// TODO kod ikony (v atribute src elementu img) prepojime so
				// stanicou podla parametra ii v href
			}
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + CURRENT_WEATHER_MAP_URL + ".");
			throw e;
		}
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public List<Station> getStations() {
		return stations;
	}
}
