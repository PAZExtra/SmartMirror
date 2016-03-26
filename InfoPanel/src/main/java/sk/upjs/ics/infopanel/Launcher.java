package sk.upjs.ics.infopanel;

import sk.upjs.ics.infopanel.core.InfoPanelApplication;
import sk.upjs.ics.infopanel.core.Layout;
import sk.upjs.ics.infopanel.core.Layout.LayoutPosition;
import sk.upjs.ics.infopanel.widgets.clock.ClockWidget;
import sk.upjs.ics.infopanel.widgets.weather.WeatherWidget;

public class Launcher extends InfoPanelApplication {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void configure() {
		addWidget("clock", new ClockWidget());
		String kosiceTxt = "C:\\GitHub\\SmartMirror\\SHMUWebScraper\\kosice.txt";
		addWidget("weather", new WeatherWidget(kosiceTxt));

		Layout basicLaout = new Layout(5, 5);

		basicLaout.addWidget("clock", new LayoutPosition(3, 0, 2, 1));
		basicLaout.addWidget("weather", new LayoutPosition(1, 2, 3, 2));
		addLayout("basic", basicLaout);
		setInitialLayout("basic");

		//setFullscreen(true);
	}
}
