package sk.upjs.ics.shmuscraper;

public class CurrentWeather {

	private Stations stanice = new Stations();

	public static void main(String[] args) {

		CurrentWeather weather = new CurrentWeather();

		weather.update();

		System.out.println(weather.getStations().getCasAktualizacie());

		for (Station station : weather.getStations()) {
			System.out.println(station);
		}
	}

	/**
	 * Aktualizuje stav pocasia a vrati, ci sa aktualizacia podarila.
	 * 
	 * @return true, ak sa aktualizacia podarila, false inak.
	 */
	public boolean update() {

		stanice.clear();

		try {
			stanice = CitacTabulky.getInstance().nacitajPocasie();
			CitacMapy.getInstance().doplnStaniceOIkony(stanice);
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	public Stations getStations() {
		return stanice;
	}
}