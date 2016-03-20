package sk.upjs.ics.infopanel.widgets.clock;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	//cely tento widget som sa snazil dokopirovat od androidu (nieco take ako je na linku)
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

	/**
	 * Format casu.
	 */
	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;

	/**
	 * Referencia na view widgetu.
	 */
	private Pane container;

	@Override
	protected void onInitialize() {
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		setRefreshInterval(500);
	}

	@Override
	protected Node onCreateView() {
		container = (Pane) loadFxmlResource("WeatherWidget.fxml");
		
		container.setStyle("-fx-background-color: white;");
		container.heightProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable observable) {
				updateFontSize();
			}
		});
		
		updateFontSize();
		onRefresh();
		return container;
	}

	@Override
	protected void onRefresh() {
		updateBackground();
		updateTime();
		updateDate();
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

	private void updateTime() {
		LocalDateTime now = LocalDateTime.now();
		weatherTimeLabel.setText(now.format(timeFormatter));
	}

	private void updateDate() {
		LocalDateTime now = LocalDateTime.now();
		weatherDateLabel.setText(now.format(dateFormatter));
	}

	private void updateIcon() {
		// tato url sa nastavi predpokladam podla nejakeho gettera od misa podla
		// aktualneho pocasia
		String URLImage = "http://www.shmu.sk/img/pocasie/pocasie2/1.gif";
		Image icon = new Image(URLImage);
		
		weatherIconImage.setImage(icon);
	}

	private void updateTemperature() {
		// tato teplota sa tiez zrejme nastaci podla nejakeho gettera od misa
		String actualTemperature = "20";
		actualTemperature += " °C";
		weatherTemperatureLabel.setText(actualTemperature);
	}

	private void updateStatus() {
		// status sa ma nastavit podla teploty??
		// ze napriklad ak je medzi 10-20 °C tak je polooblacno??
		// alebo tiez nejaky getter od misa co vrati aktualny status??
		String actualStatus = "slnečno";
		weatherStatusLabel.setText(actualStatus);
	}
	
	private void updateBackground(){
		String URLImage = "https://t1.ftcdn.net/jpg/00/61/98/06/240_F_61980602_XRRlIjuVusgoaN7MbVJHL4mOgToYEm8b.jpg";
		Image background = new Image(URLImage,container.getWidth(),container.getHeight(),false,false);

		weatherBackgroundImage.setImage(background);
		
	}
}
