package sk.upjs.ics.shmuscraper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileWorker {

	private FileWorker() {
	}

	public static void saveStationsToFile(Stations stations, File f) throws FileNotFoundException {

		try (PrintWriter pw = new PrintWriter(f)) {

			for (Station station : stations.getAllStations())
				pw.println(station.toString());
		}
	}

	public static Stations getStationsFromFile(File f) throws FileNotFoundException {

		try (Scanner sc = new Scanner(f)) {

			Stations result = new Stations();

			while (sc.hasNextLine())
				result.addStation(Station.getFromString(sc.nextLine()));

			return result;
		}
	}

	public static void saveStationToFile(Station station, File f) throws FileNotFoundException {

		try (PrintWriter pw = new PrintWriter(f)) {
			pw.println(station);
		}
	}

	public static Station getStationFromFile(File f) throws FileNotFoundException {

		try (Scanner sc = new Scanner(f)) {
			return Station.getFromString(sc.next());
		}
	}
}