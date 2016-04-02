package sk.upjs.ics.shmuscraper;

import java.util.Scanner;

import sk.upjs.ics.utilities.StringUtilities;

public class Station {

	/**
	 * Nazov stanice
	 */
	private String name;

	/**
	 * Teplota
	 */
	private Integer temperature;

	/**
	 * Smer vetra
	 */
	private String windDirection;

	/**
	 * Rychlost vetra
	 */
	private Integer windSpeed;

	/**
	 * Rychlost narazov
	 */
	private Integer gustSpeed;

	/**
	 * Oblacnost
	 */
	private String cloudiness;

	/**
	 * Stav pocasia (napr. Jasno)
	 */
	private String weatherSpecification;

	/**
	 * Konkretny odkaz na stranku s obrazkom, staci len pridat prefix
	 * www.shmu.sk
	 */
	private String iconLink;

	/**
	 * Interny identifikator stanice (vyskytuje sa v URL ako parameter ii)
	 */
	private Integer iiCode;

	Station() {
	}

	public String getName() {
		return name;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public Integer getWindSpeed() {
		return windSpeed;
	}

	public Integer getGustSpeed() {
		return gustSpeed;
	}

	public String getCloudiness() {
		return cloudiness;
	}

	public String getWeatherSpecification() {
		return weatherSpecification;
	}

	public String getIconLink() {
		return iconLink;
	}

	public Integer getIiCode() {
		return iiCode;
	}

	void setName(String name) {
		this.name = name;
	}

	void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	void setWindSpeed(Integer windSpeed) {
		this.windSpeed = windSpeed;
	}

	void setGustSpeed(Integer gustSpeed) {
		this.gustSpeed = gustSpeed;
	}

	void setCloudiness(String cloudiness) {
		this.cloudiness = cloudiness;
	}

	void setWeatherSpecification(String weatherSpecification) {
		this.weatherSpecification = weatherSpecification;
	}

	void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	void setIiCode(Integer iiCode) {
		this.iiCode = iiCode;
	}

	static Station getFromString(String string) {

		try (Scanner sc = new Scanner(string)) {

			sc.useDelimiter("\t");

			Station s = new Station();

			s.name = StringUtilities.parseStringToNull(sc.next());
			s.temperature = StringUtilities.parseStringToInt(sc.next());
			s.windDirection = StringUtilities.parseStringToNull(sc.next());
			s.windSpeed = StringUtilities.parseStringToInt(sc.next());
			s.gustSpeed = StringUtilities.parseStringToInt(sc.next());
			s.cloudiness = StringUtilities.parseStringToNull(sc.next());
			s.iconLink = StringUtilities.parseStringToNull(sc.next());
			s.weatherSpecification = StringUtilities.parseStringToNull(sc.next());

			return s;

		} catch (RuntimeException e) {
			System.err.println("Nepodarilo sa nacitac stanicu zo stringu");
		}

		return null;
	}

	@Override
	public String toString() {
		return name + "\t" + temperature + "\t" + windDirection + "\t" + windSpeed + "\t" + gustSpeed + "\t"
				+ cloudiness + "\t" + iconLink + "\t" + weatherSpecification;
	}
}