package sk.upjs.ics.infopanel.core;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Zakladna trieda pre vytvaranie widgetov.
 */
public abstract class Widget {

	/**
	 * Referencia na kontext, s ktorym je asociovany tento widget.
	 */
	Context context;

	/**
	 * Identifikator widgetu v kontexte.
	 */
	String id;

	/**
	 * Indikuje, ci widget je zobrazeny.
	 */
	private boolean visible;

	/**
	 * Cas v milisekundav, v akom sa realizuje refresh obsahu zobrazeneho
	 * widgetu.
	 */
	private long refreshInterval;

	/**
	 * Runnable realizujuci vykonanie refreshu.
	 */
	private final Runnable refreshAction;

	/**
	 * Cakajuce vykonanie refresh akcie.
	 */
	private ScheduledFuture<?> pendingRefresh;

	/**
	 * Vytvori widget.
	 */
	public Widget() {
		refreshAction = () -> {
			Platform.runLater(this::executeRefresh);
		};
	}

	/**
	 * Vrati kontext, s ktorym je asociovany tento widget.
	 * 
	 * @return
	 */
	protected Context getContext() {
		return context;
	}

	/**
	 * Vrati identifikator widgetu, ktorym je widget identifikovany v ramci
	 * kontextu.
	 * 
	 * @return identifikator widgetu.
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Vrati interval aktualizacie widgetu.
	 * 
	 * @return perioda aktualizacie v milisekundach.
	 */
	public long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * Nastavi interval aktualizacie widgetu.
	 * 
	 * @param refreshInterval
	 *            perioda aktualizacie v milisekundach.
	 */
	public void setRefreshInterval(long refreshInterval) {
		refreshInterval = Math.max(refreshInterval, -1);
		if (refreshInterval == this.refreshInterval) {
			return;
		}

		this.refreshInterval = refreshInterval;
		scheduleRefresh();
	}

	/**
	 * Nacita vizualnu reprezentaciu (view) z fxml suboru.
	 * 
	 * @param resourceName
	 *            cesta k resource suboru relativna k triede widgetu.
	 * @return view widgetu alebo null, ak nacitanie zlyhalo.
	 */
	protected Parent loadFxmlResource(String resourceName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
		loader.setController(this);
		try {
			return loader.load();
		} catch (IOException e) {
			System.err
					.println("Loading of FXML resource " + loader.getLocation() + " failed (" + e.getMessage() + ").");
			return null;
		}
	}

	/**
	 * Nastavi zobrazenie widgetu.
	 */
	void show() {
		visible = true;
		onShow();
		scheduleRefresh();
	}

	/**
	 * Nastavi ukoncenie widgetu
	 */
	void hide() {
		onHide();
		visible = false;
		scheduleRefresh();
	}

	/**
	 * Naplanuje dalsi refresh
	 */
	private void scheduleRefresh() {
		if (pendingRefresh != null) {
			if (!pendingRefresh.isDone()) {
				pendingRefresh.cancel(true);
			}
			pendingRefresh = null;
		}

		if (visible && (refreshInterval > 0)) {
			pendingRefresh = context.executor.schedule(refreshAction, refreshInterval, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Vykona refresh akciu.
	 */
	private void executeRefresh() {
		if (visible && (refreshInterval > 0)) {
			onRefresh();
		}

		scheduleRefresh();
	}

	// --------------------------------------------------------
	// Life-cycle metody
	// --------------------------------------------------------

	/**
	 * Metoda volana pri inicializovani widgetu.
	 */
	protected void onInitialize() {

	}

	/**
	 * Vytvori node sluziaci ako vizualna implementacia widgetu.
	 * 
	 * @return vizualna reprezentacia widgetu.
	 */
	protected abstract Node onCreateView();

	/**
	 * Metoda volana pri zobrazeni widgetu.
	 */
	protected void onShow() {

	}

	/**
	 * Metoda periodicky volana na obnovenie obsahu widgetu.
	 */
	protected void onRefresh() {

	}

	/**
	 * Metoda volana pri ukonceni zobrazenia widgetu.
	 */
	protected void onHide() {

	}
}
