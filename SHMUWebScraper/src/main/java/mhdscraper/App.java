package mhdscraper;

public class App {

	public static void main(String[] args) {

		String link = "http://imhd.sk/ke/cestovny-poriadok/linka/16/smer/Stanicne-nam-Podhradova/zastavka/Zelezniky/838861036591";

		CitacTabulky c = new CitacTabulky();

		try {
			c.najdiCasoveUdaje(link);
			c.najdiZastavky(link);
			System.out.println(c.getCasoveUdaje());
			System.out.println(c.getZastavky());
		}

		catch (Exception e) {
			System.err.println("Nepodarilo sa");
		}
	}

}
