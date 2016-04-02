package sk.upjs.ics.shmuscraper;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sk.upjs.ics.utilities.Citac;

public class CitacMapy extends Citac {
	
	private static CitacMapy instance = new CitacMapy();
	
	/**
	 * URL stranky s mapkou s aktualnym pocasim
	 */
	static final String CURRENT_WEATHER_MAP_URL = "http://www.shmu.sk/sk/?page=1&id=meteo_gapocasie_sk";

	private Stations stanice;

	@Override
	protected void update() {
		precitajMapu();
	}

	void doplnStaniceOIkony(Stations stanice) throws Exception {
		this.stanice = stanice;
		read(CURRENT_WEATHER_MAP_URL);
	}

	private void precitajMapu() {

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
	
	static CitacMapy getInstance(){
		return instance;
	}
}