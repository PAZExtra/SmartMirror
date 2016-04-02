package sk.upjs.ics.shmuscraper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stations implements Iterable<Station> {

	private List<Station> stations = new ArrayList<>();

	private LocalDateTime casAktualizacie;

	Stations() {
	}

	void addStation(Station station) {
		stations.add(station);
	}

	void clear() {
		stations.clear();
		casAktualizacie = null;
	}

	void setCasAktualizacie(LocalDateTime casAktualizacie2) {
		this.casAktualizacie = casAktualizacie2;
	}

	public LocalDateTime getCasAktualizacie() {
		return casAktualizacie;
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
		return new ArrayList<>(stations);
	}

	@Override
	public Iterator<Station> iterator() {
		return stations.iterator();
	}

	public Station getKosice() {
		return getByName("Košice");
	}
}