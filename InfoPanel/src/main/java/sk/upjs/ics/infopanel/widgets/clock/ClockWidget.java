package sk.upjs.ics.infopanel.widgets.clock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import sk.upjs.ics.infopanel.core.Widget;

/**
 * Jednoduchy widget zobrazujuci aktualny cas.
 */
public class ClockWidget extends Widget {

	@FXML
	private Label timeLabel;

	/**
	 * Format casu.
	 */
	private DateTimeFormatter timeFormatter;

	/**
	 * Referencia na view widgetu.
	 */
	private Pane container;

	@Override
	protected void onInitialize() {
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		setRefreshInterval(500);
	}
	

	@Override
	protected Node onCreateView() {
		container = (Pane) loadFxmlResource("ClockWidget.fxml");
		container.setStyle("-fx-background-color: red;");
		container.heightProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable observable) {
				updateFontSize();
			}
		});

		updateFontSize();
		updateTime();
		return container;
	}

	@Override
	protected void onRefresh() {
		updateTime();
		
	}

	private void updateFontSize() {
		timeLabel.setFont(
				Font.loadFont(getClass().getResource("DS-DIGIB.TTF").toExternalForm(), container.getHeight() / 2));
	}

	private void updateTime() {
		LocalDateTime now = LocalDateTime.now();
		timeLabel.setText(now.format(timeFormatter));
		
	}
}
