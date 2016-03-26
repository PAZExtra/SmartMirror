package mhdscraper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CitacTabulky {

	private List<String> zastavky = new ArrayList<>();

	private List<LocalTime> casoveUdajePracovneDni = new ArrayList<>();

	public void najdiZastavky(String link) throws Exception {

		Document doc;

		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + link + " sa nepodarilo.");
			throw e;
		}

		try {
			Elements zastavky = doc.select(".zastavky_riadok");

			for (Element zastavkaTr : zastavky) {

				Element zastavka = zastavkaTr.getElementsByTag("td").get(2);

				Elements aElements = zastavka.getElementsByTag("a");

				String nazovZastavky = null;

				if (!aElements.isEmpty())
					nazovZastavky = aElements.text();
				else
					nazovZastavky = zastavka.getElementsByTag("b").text();

				this.zastavky.add(nazovZastavky);
			}

		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + link + ".");
			throw e;
		}
	}

	public void najdiCasoveUdaje(String link) throws Exception {

		Document doc;

		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + link + " sa nepodarilo.");
			throw e;
		}

		try {
			Elements tabulka = doc.select(".cp_odchody_tabulka").first().select(".cp_odchody");

			for (Element element : tabulka) {

				String hodina = element.select(".cp_hodina").text();

				for (Element tdElement : element.select("td")) {

					if (tdElement.className().equals("cp_hodina"))
						continue;

					if (!tdElement.hasText())
						continue;

					String minuta = getMinutaFromString(tdElement.text());

					LocalTime dt = LocalTime.parse(hodina + ":" + minuta);

					casoveUdajePracovneDni.add(dt);
				}
			}

		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + link + ".");
			throw e;
		}
	}

	private String getMinutaFromString(String text) {

		if (text.isEmpty())
			return null;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);

			if (Character.isDigit(c))
				sb.append(c);
		}

		return sb.toString();
	}

	public List<String> getZastavky() {
		return zastavky;
	}

	public List<LocalTime> getCasoveUdaje() {
		return casoveUdajePracovneDni;
	}
}
