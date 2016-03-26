package sk.upjs.ics.infopanel.widgets.weather;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import sk.upjs.ics.infopanel.core.Widget;

public class WeatherWidget extends Widget {


	@FXML
	private Label weatherStationNameLabel;

	@FXML
	private Label weatherDirectionWindLabel;

	@FXML
	private Label weatherTemperatureLabel;

	@FXML
	private Label weatherStatusLabel;

	@FXML
	private ImageView weatherIconImage;

	@FXML
	private Label weatherTitleLabel;

	
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
	 * Referencia na view widgetu.
	 */
	private Pane container;

	public WeatherWidget(String urlFile) {
	this.urlFile = urlFile;	
	}
	
	
	@Override
	protected void onInitialize() {
		// kazdu minutu spravy refresh
		setRefreshInterval(60000);
	}

	@Override
	protected Node onCreateView() {
		container = (Pane) loadFxmlResource("WeatherWidget.fxml");

		container.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		
		container.heightProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable observable) {
				updateFont();
			}
		});
		refreshInfo();
		updateFont();
		onRefresh();
		return container;
	}

	@Override
	protected void onRefresh() {

		refreshInfo();
		updateIcon();
		updateTemperature();
		updateStatus();
		updateNameStation();
		updateWind();

	}

	private void updateFont() {
		String fontString = getClass().getResource("FogtwoNo5.ttf").toExternalForm();
		double size = container.getHeight() / 10;
		Font font = Font.loadFont(fontString, size);
		weatherStationNameLabel.setFont(font);
		weatherDirectionWindLabel.setFont(font);
		weatherStatusLabel.setFont(font);
		weatherTemperatureLabel.setFont(font);
		weatherTitleLabel.setFont(font);
		
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

	private void updateIcon(){
		String s = "."+urlIcon.replace('/', '\\');
		System.out.println(s);
		File file = new File(s);
		Image image = new Image(file.toURI().toString());
		weatherIconImage.setImage(image);
	}

	private void updateTemperature() {
		weatherTemperatureLabel.setText(actualTemperature);
	}

	private void updateStatus() {
		weatherStatusLabel.setText(actualStatus);
	}
	private void updateWind() {
		weatherDirectionWindLabel.setText("Smer vetra: "+directionWind);
	}
	private void updateNameStation() {
		weatherStationNameLabel.setText(nameStation);
	}
}
