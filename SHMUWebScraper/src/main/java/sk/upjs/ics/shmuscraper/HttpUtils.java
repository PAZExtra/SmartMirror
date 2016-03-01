package sk.upjs.ics.shmuscraper;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpUtils {

	/**
	 * Charset pre utf-8
	 */
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	/**
	 * Ziska obsah na zadanej url v UTF-8 kodovani.
	 * 
	 * @param url
	 * 
	 * @return ziskany obsah alebo null, ak sa obsah nepodarilo ziskat.
	 */
	public static String retrieveHtmlContentForUrl(String url) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream(), UTF8_CHARSET))) {
			StringBuilder sb = new StringBuilder();

			char[] buffer = new char[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) >= 0) {
				sb.append(buffer, 0, bytesRead);
			}

			return sb.toString();
		} catch (Exception e) {
			System.err.println("Ziskanie obsahu " + url + " zlyhalo.");
			return null;
		}
	}

	/**
	 * Zapise obsah do zadaneho suboru.
	 * 
	 * @param output
	 * @param content
	 */
	public static void saveStringToFile(File output, String content) {
		try (Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(output)),
				UTF8_CHARSET)) {
			writer.write(content);
		} catch (Exception e) {
			throw new RuntimeException("Zapis do suboru " + output + " zlyhal.");
		}
	}

	public static void main(String[] args) {
		System.out.println(HttpUtils.retrieveHtmlContentForUrl("http://www.shmu.sk/sk/?page=1&id=meteo_apocasie_sk"));
	}
}
