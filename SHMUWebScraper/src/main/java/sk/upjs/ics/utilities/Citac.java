package sk.upjs.ics.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Citac {

	protected String url;

	protected Document doc;

	protected Citac() {
	}

	protected final void read(String url) throws Exception {

		if (url == null || url.isEmpty())
			return;

		this.url = url;

		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu z url " + url + " sa nepodarilo.");
			throw e;
		}

		try {
			update();
		} catch (Exception e) {
			System.err.println("Neocakavana struktura stranky " + url + ".");
			throw e;
		}
	}

	protected abstract void update();
}