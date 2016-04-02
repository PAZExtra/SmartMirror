package sk.upjs.ics.mhdscraper;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sk.upjs.ics.utilities.Citac;

public class CitacZastavky extends Citac {

	private static CitacZastavky instance = new CitacZastavky();

	private Zastavka info;

	void doplnZastavkuOInfo(Zastavka info) throws Exception {
		this.info = info;
		read(info.getUrl());
	}

	@Override
	protected void update() {
		precitajOdchody();
		precitajPoznamky();
		info.setNaZnamenie(vratCiJeNaZnamenie());
	}

	private void precitajOdchody() {

		Elements tabulky = doc.select(".cp_obsah").select("table").first().children();

		Map<Integer, String> indexyNaTabulky = new HashMap<>();

		Elements tdElements = tabulky.select("tr").first().select("td");

		for (int i = 0; i < tdElements.size(); i++)
			indexyNaTabulky.put(i, tdElements.get(i).text());

		for (String nazovTabulky : indexyNaTabulky.values())
			info.pridajTypOdchodov(nazovTabulky);

		Elements jednotliveTabulky = tabulky.select("table");

		for (int i = 0; i < jednotliveTabulky.size(); i++) {

			for (Element trElement : jednotliveTabulky.get(i).select("tr")) {

				String hodina = trElement.select(".cp_hodina").text();

				for (Element tdElement : trElement.select("td")) {

					if (tdElement.className().equals("cp_hodina"))
						continue;

					if (!tdElement.hasText())
						continue;

					if (tdElement.className().contains("nadpis"))
						continue;

					Odchod odchod = new Odchod();

					odchod.setNizkopodlazne(tdElement.hasClass("nizkopodlazne"));

					doplnInfo(odchod, hodina, tdElement.text());

					info.pridajOdchodDanyTypom(indexyNaTabulky.get(i), odchod);
				}
			}
		}
	}

	private void precitajPoznamky() {

		String poznamky = doc.select(".poznamky").select("td").html();

		String[] riadky = poznamky.split("<br>");

		for (String string : riadky) {

			String textRiadku = Jsoup.parse(string).text();

			int index = textRiadku.indexOf("-");

			if (index == -1 || textRiadku.contains("Zastávka na znamenie") || textRiadku.contains("nízkopodlažným"))
				continue;

			String id = textRiadku.substring(0, index).trim();
			String popis = textRiadku.substring(index + 1).trim();

			info.pridajPoznamku(id, popis);
		}
	}

	private boolean vratCiJeNaZnamenie() {

		if (url == null || url.isEmpty())
			return false;

		return doc.select(".zastavky_aktualna").select(".symbol").text().contains("z");
	}

	private void doplnInfo(Odchod info, String hodina, String zvysokTextu) {

		String minuta = zvysokTextu.substring(0, 2);

		info.setCasOdchodu(LocalTime.parse(hodina + ":" + minuta));

		if (zvysokTextu.length() == 2)
			return;

		info.setPoznamka(zvysokTextu.substring(2));
	}

	static CitacZastavky getInstance() {
		return instance;
	}
}