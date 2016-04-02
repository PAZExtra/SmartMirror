package sk.upjs.ics.mhdscraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Zastavka {

	private String nazov;

	private boolean naZnamenie;

	private boolean obcasna;

	private String url;

	private Map<String, List<Odchod>> odchody;

	private Map<String, String> poznamky;

	Zastavka() {
		odchody = new HashMap<>();
		poznamky = new HashMap<>();
	}

	void setNazov(String nazov) {
		this.nazov = nazov;
	}

	void setNaZnamenie(boolean naZnamenie) {
		this.naZnamenie = naZnamenie;
	}

	void setObcasna(boolean obcasna) {
		this.obcasna = obcasna;
	}

	void setUrl(String url) {
		this.url = url;
	}

	void pridajTypOdchodov(String typ) {
		odchody.put(typ, new ArrayList<>());
	}

	void pridajOdchodDanyTypom(String typ, Odchod odchod) {
		odchody.get(typ).add(odchod);
	}

	public Set<String> getTypyOdchodov() {
		return new HashSet<>(odchody.keySet());
	}

	public List<Odchod> vratOdchodyPreDanyTyp(String typ) {
		return new ArrayList<>(odchody.get(typ));
	}

	void pridajPoznamku(String legenda, String znenie) {
		poznamky.put(legenda, znenie);
	}

	public Map<String, String> getPoznamky() {
		return new HashMap<>(poznamky);
	}

	public String getNazov() {
		return nazov;
	}

	public boolean isNaZnamenie() {
		return naZnamenie;
	}

	public boolean isObcasna() {
		return obcasna;
	}

	String getUrl() {
		return url;
	}
}
