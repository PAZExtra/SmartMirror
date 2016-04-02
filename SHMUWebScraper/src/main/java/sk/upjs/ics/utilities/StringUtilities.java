package sk.upjs.ics.utilities;

public class StringUtilities {

	private StringUtilities() {
	}

	public static Integer parseStringToInt(String s) {

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

	public static String parseStringToNull(String s) {

		if (s.equals("null"))
			return null;

		return s;
	}

	public static String removeNonBreakingSpaces(String s) {
		return s.replaceAll("(^\\h*)|(\\h*$)", "");
	}
}
