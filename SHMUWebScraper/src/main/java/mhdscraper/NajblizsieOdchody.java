package mhdscraper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class NajblizsieOdchody {

	private List<InfoOOdchode> odchody = new ArrayList<>();

	public void najdiOdchody(String link) throws Exception {

		Document doc;

		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + link + " sa nepodarilo.");
			throw e;
		}

		try {
			Elements zastavky = doc.select(".cestovny_poriadok_zastavkova_tabula").select("tr");

			for (Element trElement : zastavky) {

				Elements tdElements = trElement.select("td");

				if (tdElements.isEmpty())
					continue;

				InfoOOdchode info = new InfoOOdchode();

				info.setLinka(tdElements.get(0).text());
				info.setSmer(tdElements.get(1).text());
				info.setOdchod(LocalTime.parse(tdElements.get(2).text()));

				odchody.add(info);
			}

		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + link + ".");
			throw e;
		}
	}

	public List<InfoOOdchode> getOdchody() {
		return odchody;
	}
}
