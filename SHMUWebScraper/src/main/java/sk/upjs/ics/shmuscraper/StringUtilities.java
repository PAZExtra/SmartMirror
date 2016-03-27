package sk.upjs.ics.shmuscraper;

public class StringUtilities {

	private StringUtilities() {
	}

	public static Integer parseString(String s) {

		if (s.equals("null"))
			return null;

		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			System.err.println("Nepodarilo sa zmenit " + s + " na Integer");
		}

		return null;
	}

	public static String parseEmptyStringToNull(String s) {

		if (s.isEmpty())
			return null;

		return s;
	}
}
