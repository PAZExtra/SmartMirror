package sk.upjs.ics.infopanel.core;

import java.util.*;

/**
 * Rozlozenie widgetov v zobrazovacom paneli.
 */
public class Layout {

	/**
	 * Konfiguracia umiestnenia widgetu.
	 */
	public static class LayoutPosition {
		public final int row;
		public final int col;
		public final int colspan;
		public final int rowspan;

		public LayoutPosition(int col, int row, int colspan, int rowspan) {
			this.col = col;
			this.row = row;
			this.colspan = colspan;
			this.rowspan = rowspan;
		}
	}

	/**
	 * Pocet stlpcov layoutu.
	 */
	private final int cols;

	/**
	 * Pocet riadkov layoutu.
	 */
	private final int rows;

	/**
	 * Widgety zobrazene v layoute.
	 */
	final Map<String, LayoutPosition> widgets = new HashMap<String, LayoutPosition>();

	/**
	 * Vytvori layout so zadanym poctom riadkov a stlpcov, ktore definuju
	 * mriezku, v ktorej sa budu widgety umiestnovat.
	 * 
	 * @param cols
	 * @param rows
	 */
	public Layout(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}

	/**
	 * Definuje umiestnenie a velkost widgetu so zadanym id.
	 * 
	 * @param widgetId
	 * @param col
	 * @param row
	 * @param colspan
	 * @param rowspan
	 */
	public void addWidget(String widgetId, LayoutPosition config) {
		widgets.put(widgetId, config);
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}
}
