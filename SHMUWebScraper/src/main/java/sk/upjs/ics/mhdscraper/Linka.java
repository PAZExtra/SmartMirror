package sk.upjs.ics.mhdscraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Linka {

	private String id;

	private String typ;

	private String url;

	private Map<String, List<Zastavka>> zastavkyVSmere;

	Linka() {
		zastavkyVSmere = new HashMap<>();
	}

	void setId(String id) {
		this.id = id;
	}

	void setTyp(String typ) {
		this.typ = typ;
	}

	void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getTyp() {
		return typ;
	}

	String getUrl() {
		return url;
	}

	void pridajSmer(String smer) {
		zastavkyVSmere.put(smer, new ArrayList<>());
	}

	void pridajZastavkuSmeru(String smer, Zastavka zastavka) {
		zastavkyVSmere.get(smer).add(zastavka);
	}

	public Set<String> getSmery() {
		return new HashSet<>(zastavkyVSmere.keySet());
	}

	public List<Zastavka> getZastavkyPreSmer(String smer) {
		return new ArrayList<>(zastavkyVSmere.get(smer));
	}

	public List<Zastavka> getVsetkyZastavky() {

		List<Zastavka> result = new ArrayList<>();

		for (String smer : getSmery())
			result.addAll(zastavkyVSmere.get(smer));

		return result;
	}
}
