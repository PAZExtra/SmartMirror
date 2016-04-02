package sk.upjs.ics.mhdscraper;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sk.upjs.ics.utilities.Citac;
import sk.upjs.ics.utilities.StringUtilities;

class CitacLinky extends Citac {

	private static CitacLinky instance = new CitacLinky();

	private Linka info;

	@Override
	protected void update() {
		precitajLinku();
	}

	void doplnInfo(Linka info) throws Exception {
		this.info = info;
		read(info.getUrl());
	}

	private void precitajLinku() {

		Elements tabulky = doc.getElementsByTag("table").first().children().select("table");

		for (int i = 0; i < tabulky.size(); i++) {

			Elements trElements = tabulky.get(i).select("tr");

			String smer = trElements.first().text().replace("► ", "");

			info.pridajSmer(smer);

			for (int j = 2; j < trElements.size(); j++) {

				Element tdElement = trElements.get(j).select("td").first();

				String nazovZastavky = StringUtilities.removeNonBreakingSpaces(tdElement.text());

				String odkaz = null;

				Elements aElements = tdElement.select("a");

				if (!aElements.isEmpty())
					odkaz = "http://imhd.sk" + aElements.attr("href");

				Zastavka zastavka = new Zastavka();

				zastavka.setObcasna(!tdElement.select("i").text().isEmpty());
				zastavka.setUrl(odkaz);
				zastavka.setNazov(nazovZastavky);

				info.pridajZastavkuSmeru(smer, zastavka);
			}
		}
	}

	static CitacLinky getInstance() {
		return instance;
	}
}