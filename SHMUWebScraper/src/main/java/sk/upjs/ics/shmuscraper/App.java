package sk.upjs.ics.shmuscraper;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		CurrentWeather weather = new CurrentWeather();
		
		weather.update();
		
		for (Station station : weather.getStations()) {
			System.out.println(station);
		}
		
		System.out.println(weather.getDateTime());

	}
}
