package sk.upjs.ics.shmuscraper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stations implements Iterable<Station> {

	private List<Station> stations = new ArrayList<>();

	Stations() {
	}

	void addStation(Station station) {
		stations.add(station);
	}

	void clear() {
		stations.clear();
	}

	Station getStationFromCode(Integer iiCode) {

		for (Station station : stations)
			if (station.getIiCode().equals(iiCode))
				return station;

		return null;
	}

	public Station getByName(String name) {

		for (Station station : stations)
			if (station.getName().equals(name))
				return station;

		return null;
	}

	public List<Station> getAllStations() {
		return stations;
	}

	@Override
	public Iterator<Station> iterator() {
		return stations.iterator();
	}
}