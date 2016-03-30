package mhdscraper;

import java.time.LocalTime;

public class InfoOOdchode {

	private String linka;
	private String smer;
	private LocalTime odchod;

	public String getLinka() {
		return linka;
	}

	public String getSmer() {
		return smer;
	}

	public LocalTime getOdchod() {
		return odchod;
	}

	void setLinka(String linka) {
		this.linka = linka;
	}

	void setSmer(String smer) {
		this.smer = smer;
	}

	void setOdchod(LocalTime odchod) {
		this.odchod = odchod;
	}

	@Override
	public String toString() {
		return linka + "\t" + smer + "\t" + odchod;
	}
}