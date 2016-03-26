package sk.upjs.ics.infopanel.widgets.weather;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import sk.upjs.ics.infopanel.core.Widget;

public class WeatherWidget extends Widget {

	// cely tento widget som sa snazil dokopirovat od androidu (nieco take ako
	// je na linku)
	// http://droidviews.com/wp-content/uploads/2013/03/galaxy-s3-weather-widget1.jpg

	@FXML
	private Label weatherTimeLabel;

	@FXML
	private Label weatherDateLabel;

	@FXML
	private Label weatherTemperatureLabel;

	@FXML
	private Label weatherStatusLabel;

	@FXML
	private ImageView weatherIconImage;

	@FXML
	private ImageView weatherBackgroundImage;

	
	private String urlFile;
	
	//jednotlive udaje zaradom ako su zapisane v txt
	private String nameStation;
	private String actualTemperature;
	private String directionWind;
	private String neznamyUdaj1;
	private String neznamyUdaj2;
	private String actualStatus;
	private String urlIcon;
	

	/**
	 * Format casu.
	 */
	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;

	/**
	 * Referencia na view widgetu.
	 */
	private Pane container;

	public WeatherWidget(String urlFile) {
	this.urlFile = urlFile;	
	}
	
	
	@Override
	protected void onInitialize() {
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		// kazdu minutu spravy refresh
		setRefreshInterval(60000);
	}

	@Override
	protected Node onCreateView() {
		container = (Pane) loadFxmlResource("WeatherWidget.fxml");

		container.setStyle("-fx-background-color: black;");
		container.heightProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable observable) {
				updateFontSize();
			}
		});
		refreshInfo();
		updateFontSize();
		onRefresh();
		return container;
	}

	@Override
	protected void onRefresh() {

		/*
		 * System.out.println(nameStation);
		 * System.out.println(actualTemperature);
		 * System.out.println(directionWind); System.out.println(neznamyUdaj1);
		 * System.out.println(neznamyUdaj2); System.out.println(actualStatus);
		 * System.out.println(urlIcon);
		 */
		refreshInfo();
		updateIcon();
		updateTemperature();
		updateStatus();

	}

	private void updateFontSize() {
		String font = getClass().getResource("FogtwoNo5.ttf").toExternalForm();
		double size = container.getHeight() / 10;
		weatherTimeLabel.setFont(Font.loadFont(font, size));
		weatherDateLabel.setFont(Font.loadFont(font, size));
		weatherStatusLabel.setFont(Font.loadFont(font, size));
		weatherTemperatureLabel.setFont(Font.loadFont(font, size));

	}

	private void refreshInfo() {
		try (Scanner skener = new Scanner(new File(urlFile))) {
			nameStation = skener.next();
			actualTemperature = skener.next();
			directionWind = skener.next();
			neznamyUdaj1 = skener.next();
			neznamyUdaj2 = skener.next();
			actualStatus = skener.next();
			urlIcon = skener.next();

		} catch (Exception e) {
			System.out.println("nepodarilo sa nacitat subor");
		}

	}

	private void updateIcon() {
		// tato url sa nastavi predpokladam podla nejakeho gettera od misa podla
		// aktualneho pocasia
		
		String s = "C:\\GitHub\\SmartMirror\\InfoPanel"+ urlIcon.replace('/', '\\');
		System.out.println(new File(".").getAbsolutePath());
		System.out.println(s);
		Image icon = new Image(s);

		weatherIconImage.setImage(icon);
	}

	private void updateTemperature() {
		// tato teplota sa tiez zrejme nastaci podla nejakeho gettera od misa

		weatherTemperatureLabel.setText(actualTemperature);
	}

	private void updateStatus() {
		// status sa ma nastavit podla teploty??
		// ze napriklad ak je medzi 10-20 °C tak je polooblacno??
		// alebo tiez nejaky getter od misa co vrati aktualny status??

		weatherStatusLabel.setText(actualStatus);
	}

}
