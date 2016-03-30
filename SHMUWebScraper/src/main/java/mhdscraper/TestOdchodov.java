package mhdscraper;

public class TestOdchodov {

	public static void main(String[] args) {

		String link = "http://imhd.sk/ke/cestovny-poriadok/zastavka/1347/Radnica-Stareho-mesta";

		NajblizsieOdchody no = new NajblizsieOdchody();

		try {
			no.najdiOdchody(link);

			for (InfoOOdchode info : no.getOdchody()) {
				System.out.println(info);
			}

		} catch (Exception e) {
			System.out.println("Nepodarilo sa");
		}
	}

}
