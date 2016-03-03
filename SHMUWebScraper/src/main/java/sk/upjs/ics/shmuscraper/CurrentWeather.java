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
