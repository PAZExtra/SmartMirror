package sk.upjs.ics.mhdscraper;

import java.time.LocalTime;

class Odchod {

	private LocalTime casOdchodu;

	private String poznamka;

	private boolean nizkopodlazne;

	Odchod() {
	}

	void setCasOdchodu(LocalTime casOdchodu) {
		this.casOdchodu = casOdchodu;
	}

	void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	void setNizkopodlazne(boolean nizkopodlazne) {
		this.nizkopodlazne = nizkopodlazne;
	}

	public LocalTime getCasOdchodu() {
		return casOdchodu;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public boolean getNizkopodlazne() {
		return nizkopodlazne;
	}
}