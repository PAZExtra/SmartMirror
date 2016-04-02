package sk.upjs.ics.mhdscraper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sk.upjs.ics.utilities.Citac;
import sk.upjs.ics.utilities.StringUtilities;

public class CitacOdchodov extends Citac {

	private static CitacOdchodov instance = new CitacOdchodov();

	private List<Linka> linky;

	List<Linka> stiahniVsetkyLinky() throws Exception {
		linky = new ArrayList<>();
		read("http://imhd.sk/ke/cestovne-poriadky");
		return linky;
	}

	@Override
	protected void update() {
		najdiLinky();
	}

	private void najdiLinky() {

		Element table = doc.getElementsByTag("table").first();

		for (Element trElement : table.getElementsByTag("tr")) {

			Elements tdElements = trElement.getElementsByTag("td");

			String typSpoja = getTypSpoja(tdElements.get(0).text());

			for (Element aElement : tdElements.get(1).getElementsByTag("a")) {
				String odkaz = "http://imhd.sk" + aElement.attr("href");
				String id = aElement.text();
				Linka info = new Linka();
				info.setId(id);
				info.setTyp(typSpoja);
				info.setUrl(odkaz);
				linky.add(info);
			}
		}
	}

	private String getTypSpoja(String text) {

		text = StringUtilities.removeNonBreakingSpaces(text);

		if (text.equals("Električky"))
			return "Električka";

		if (text.equals("Autobusy"))
			return "Autobus";

		if (text.equals("Nočné spoje"))
			return "Nočný spoj";

		return null;
	}

	static CitacOdchodov getInstance() {
		return instance;
	}
}