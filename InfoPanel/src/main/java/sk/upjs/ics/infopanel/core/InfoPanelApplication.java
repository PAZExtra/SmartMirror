package sk.upjs.ics.infopanel.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class InfoPanelApplication extends Application {

	/**
	 * Kontext aplikacie.
	 */
	private Context context;

	/**
	 * Inicialny layout.
	 */
	private String initialLayout;

	/**
	 * Fullscreen rezim.
	 */
	private boolean fullscreen;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane mainPane = new GridPane();
		context = new Context(mainPane);

		configure();
		context.initializeWidgets();
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		
		primaryStage.setFullScreen(fullscreen);
		primaryStage.show();

		context.changeLayout(initialLayout);
	}

	/**
	 * Prida do aplikacie widget pristupny pod zadanym identifikatorom.
	 * 
	 * @param id
	 *            identifikator widgetu.
	 * @param widget
	 *            widget.
	 */
	protected void addWidget(String id, Widget widget) {
		context.addWidget(id, widget);
	}

	/**
	 * Prida do aplikacie rozlozenie widgetov (layout) pod zadanym
	 * identifikatorom.
	 * 
	 * @param id
	 *            identifikator layoutu.
	 * @param layout
	 *            popis rozlozenia widgetov.
	 */
	protected void addLayout(String id, Layout layout) {
		context.addLayout(id, layout);
	}

	protected String getInitialLayout() {
		return initialLayout;
	}

	protected void setInitialLayout(String initialLayout) {
		this.initialLayout = initialLayout;
	}

	protected boolean isFullscreen() {
		return fullscreen;
	}

	protected void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	/**
	 * Nakonfiguruje aplikaciu zobrazujucu informacne panely.
	 */
	protected abstract void configure();
}
