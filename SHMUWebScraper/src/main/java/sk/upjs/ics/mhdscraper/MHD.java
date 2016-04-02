package sk.upjs.ics.mhdscraper;

import java.util.List;

public class MHD {

	private List<Linka> linky;

	public static void main(String[] args) {

		MHD mhd = new MHD();

		try {
			vypisZastavku(mhd.vratPrvuZastavkuSMenom("Senný trh"));
			vypisZastavku(mhd.vratPrvuZastavkuSMenom("Poľov"));
			vypisZastavku(mhd.vratPrvuZastavkuSMenom("Šebastovce"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nacitajLinky() throws Exception {
		linky = CitacOdchodov.getInstance().stiahniVsetkyLinky();
	}

	public void nacitajZastavkyLiniek() throws Exception {
		for (Linka linka : linky)
			CitacLinky.getInstance().doplnInfo(linka);
	}

	public Zastavka vratPrvuZastavkuSMenom(String meno) throws Exception {

		nacitajLinky();
		nacitajZastavkyLiniek();

		for (Linka linka : linky) {
			for (Zastavka zastavka : linka.getVsetkyZastavky()) {
				if (zastavka.getNazov().equals(meno)) {
					CitacZastavky.getInstance().doplnZastavkuOInfo(zastavka);
					return zastavka;
				}
			}
		}

		return null;
	}

	public static void vypisZastavku(Zastavka z) {

		System.out.println(z.getNazov());
		System.out.println("Na znamenie: " + z.isNaZnamenie());
		System.out.println("Občasná: " + z.isObcasna());
		System.out.println(z.getUrl());

		if (z.getPoznamky().size() != 0)
			System.out.println(z.getPoznamky());

		System.out.println();

		for (String s : z.getTypyOdchodov()) {

			System.out.println(s);
			
			for (Odchod o : z.vratOdchodyPreDanyTyp(s)) {

				System.out.print(o.getCasOdchodu());

				if (o.getPoznamka() != null)
					System.out.print(o.getPoznamka());

				System.out.println(", nízkopodlažné spoje: " + o.getNizkopodlazne());
			}

			System.out.println();
		}
	}
}