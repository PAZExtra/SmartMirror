package sk.upjs.ics.shmuscraper;

import sk.upjs.ics.shmuscraper.CurrentWeather.Station;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		CurrentWeather weather = new CurrentWeather();
		weather.update();
		for (Station s : weather.getStations()) {
			System.out.println(s.name);
		}
	}
}
