package sk.upjs.ics.infopanel.core;

import java.util.*;
import java.util.concurrent.*;

import javafx.scene.Node;
import javafx.scene.layout.*;
import sk.upjs.ics.infopanel.core.Layout.LayoutPosition;

/**
 * Kontext info panelu. Uchovava stav info panelu a zaroven zabezpecuje
 * kooperaciu medzi widgetmi a zobrazovacim panelom.
 */
public class Context {

	/**
	 * Komplexny zaznam widgetu.
	 */
	private static class WidgetBundle {
		/**
		 * Widget
		 */
		final Widget widget;

		/**
		 * Vizualne reprezentacia widgetu.
		 */
		Node view;

		/**
		 * Skontruuje zaznam pre widget.
		 * 
		 * @param widget
		 *            widget.
		 */
		public WidgetBundle(Widget widget) {
			this.widget = widget;
		}
	}

	/**
	 * Map vsetkych widgetov.
	 */
	private final Map<String, WidgetBundle> widgets = new HashMap<String, WidgetBundle>();

	/**
	 * Map definovanych layoutov zobrazenia.
	 */
	private final Map<String, Layout> layouts = new HashMap<String, Layout>();

	/**
	 * Zoznam zobrazenych widgetov.
	 */
	private final List<Widget> displayedWidgets = new ArrayList<Widget>();

	/**
	 * Mriezkovy panel, v ktorom sa zobrazuju widgety.
	 */
	private final GridPane gridPane;

	/**
	 * Vykovanatel s rozvrhovanim.
	 */
	final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	/**
	 * Konstruktor kontextu
	 */
	Context(GridPane gridPane) {
		this.gridPane = gridPane;
	}

	/**
	 * Prida layout pristupny pod zadanym identifikatorom.
	 * 
	 * @param id
	 *            identifikator rozlozenia widgetov
	 * @param layout
	 *            rozlozenie widgetov.
	 */
	void addLayout(String id, Layout layout) {
		layouts.put(id, layout);
	}

	/**
	 * Prida widget pristupny pod zadanym identifikatorom.
	 * 
	 * @param id
	 *            identifikator widgetu.
	 * @param widget
	 *            widget.
	 */
	void addWidget(String id, Widget widget) {
		widgets.put(id, new WidgetBundle(widget));
	}

	/**
	 * Vrati widhet so zadanym identifikatorom.
	 * 
	 * @param id
	 *            identifikator widgetu.
	 * @return widget alebo null, ak widget so zadanym identifikatorom
	 *         neexistuje.
	 */
	public Widget getWidget(String id) {
		WidgetBundle widgetBundle = widgets.get(id);
		if (widgetBundle == null) {
			return null;
		}

		return widgetBundle.widget;
	}

	/**
	 * Inicializuje widgety a vytvori ich GUI elementy.
	 */
	void initializeWidgets() {
		// Nastavime widgetom kontext.
		for (Map.Entry<String, WidgetBundle> entry : widgets.entrySet()) {
			Widget widget = entry.getValue().widget;
			widget.context = this;
			widget.id = entry.getKey();
		}

		// Oznamime widgetom, ze ich inicializacia bola ukoncena
		for (WidgetBundle wb : widgets.values()) {
			wb.widget.onInitialize();
		}

		// Nechame kazdy widget pripravit vizualne reprezentacie.
		for (WidgetBundle wb : widgets.values()) {
			wb.view = wb.widget.onCreateView();
		}
	}

	/**
	 * Zmeni layout, ktory bol asociovany so zadanym identifikatorom.
	 * 
	 * @param layoutId
	 *            identifikator layoutu.
	 */
	public void changeLayout(String layoutId) {
		changeLayout(layouts.get(layoutId));
	}

	/**
	 * Zmeni layout.
	 */
	void changeLayout(Layout layout) {
		// Oznamime zobrazenym widgetom ukoncenie ich zobrazovania.
		for (Widget widget : displayedWidgets) {
			widget.hide();
		}
		displayedWidgets.clear();

		// Vyprazdnime zobrazovaci pane
		gridPane.getChildren().clear();
		gridPane.getColumnConstraints().clear();
		gridPane.getRowConstraints().clear();

		if (layout == null) {
			return;
		}

		// Nastavime obmedzenia na stlpce
		int cols = layout.getCols();
		ArrayList<ColumnConstraints> colConstraints = new ArrayList<ColumnConstraints>();
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100);
		for (int i = 0; i < cols; i++) {
			colConstraints.add(cc);
		}
		gridPane.getColumnConstraints().addAll(colConstraints);

		// Nastavime obmedzenia na riadky
		int rows = layout.getRows();
		ArrayList<RowConstraints> rowConstraints = new ArrayList<RowConstraints>();
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100);
		for (int i = 0; i < rows; i++) {
			rowConstraints.add(rc);
		}
		gridPane.getRowConstraints().addAll(rowConstraints);

		// Umiestnime zobrazovane komponenty
		for (Map.Entry<String, LayoutPosition> entry : layout.widgets.entrySet()) {
			WidgetBundle widgetBundle = widgets.get(entry.getKey());
			if (widgetBundle == null) {
				System.err.println("Layout references unknown widget with id: " + entry.getKey());
				continue;
			}

			if (widgetBundle.view == null) {
				continue;
			}

			LayoutPosition spec = entry.getValue();
			gridPane.add(widgetBundle.view, spec.col, spec.row, spec.colspan, spec.rowspan);
			displayedWidgets.add(widgetBundle.widget);
		}

		// Oznamime widgetom, ze boli zobrazene
		for (Widget widget : displayedWidgets) {
			widget.show();
		}
	}
}
