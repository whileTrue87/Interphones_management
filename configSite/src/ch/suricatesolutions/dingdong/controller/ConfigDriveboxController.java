package ch.suricatesolutions.dingdong.controller;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jdom.JDOMException;
import org.primefaces.component.dnd.Droppable;
import org.primefaces.event.DragDropEvent;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.business.XmlManager;
import ch.suricatesolutions.dingdong.model.TApplication;
import ch.suricatesolutions.dingdong.model.TDrivebox;
import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;

@ManagedBean
@SessionScoped
/**
 * Controls the display of the Drivebox configuration
 * @author Maxime Reymond
 */
public class ConfigDriveboxController implements Serializable {

	private int pkDrivebox;
	private static final long serialVersionUID = 1L;

	@EJB
	private DaoManager dao;

	@EJB
	private XmlManager xml;

	//Indicates if an application is not present before
	//the loading of the page on the dashboard grid
	//If a cell is false, an app is present
	private boolean[][] notInstalledApp = new boolean[2][5];

	//Map containing the possible conflicts on the dashboard grid
	//Each entry(Point(x,y)) represent a list of present application
	private Map<Point, List<Integer>> conflicts = new HashMap<Point, List<Integer>>();

	private SelectItem[] booleanOptions = new SelectItem[3];

	private TDriveboxHasApplication app;

	//Binding properties
	private HtmlInputText nameInput;
	private HtmlInputText numInput;
	private HtmlSelectBooleanCheckbox muteInput;

	public ConfigDriveboxController() {
		booleanOptions[0] = new SelectItem("");
		booleanOptions[1] = new SelectItem(String.valueOf(true));
		booleanOptions[2] = new SelectItem(String.valueOf(false));
	}

	/**
	 * Is called when an application is dropped
	 * on the dashboard grid, controls the database updates
	 * @param event The dropped application
	 */
	public void onDrop(DragDropEvent event) {
		try {
			TApplication app = (TApplication) event.getData();
			Droppable d = (Droppable) event.getSource();
			String id = d.getId();
			String[] tokens = id.split("_");
			byte[] configFile = null;
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);

			Set<Point> keys = conflicts.keySet();
			for (Point p : keys) {
				List<Integer> lInt = conflicts.get(p);
				lInt.remove((Object) app.getPkApplication());
			}

			List<Integer> lInt = conflicts.get(new Point(x, y));
			if (lInt == null) {
				lInt = new ArrayList<Integer>();
			}
			lInt.add(app.getPkApplication());
			conflicts.put(new Point(x, y), lInt);

			keys = conflicts.keySet();
			for (Point p : keys) {
				if (conflicts.get(p).size() > 1) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Il existe un conflit entre plusieurs applications, Veuillez le corriger"));
					return;
				}
			}
			if (isInstalledApp(x, y)) {
				dao.disableAppFromDrivebox(this.app.getId().getPfkApplication(), this.app.getId().getPfkDrivebox());
			}

			boolean isInstalled = isInstalledApp(app.getPkApplication());
			notInstalledApp[x][y] = true;
			if (isInstalled) {
				TDriveboxHasApplication dHA = dao.getInstalledAppFromPks(app.getPkApplication(), this.pkDrivebox);
				if (dHA != null)
					configFile = xml.updateConfigurationFile(dHA.getConfigurationXml(), x, y);
			} else {

				configFile = xml.updateConfigurationFile(app.getConfigurationSchema(), x, y);
			}
			dao.updateInstalledApp(app.getPkApplication(), this.pkDrivebox, configFile);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Is called when an application is dropped
	 * back from the dashboard to the applications grid
	 * Controls the database updates
	 * @param event The dropped application
	 */
	public void onDropBack(DragDropEvent event) {
		TApplication app = (TApplication) event.getData();
		System.out.println("onDropBack : pkApplication=" + app.getPkApplication() + " pkDrivebox=" + this.pkDrivebox);
		dao.disableAppFromDrivebox(app.getPkApplication(), this.pkDrivebox);
		Set<Point> keys = conflicts.keySet();
		for (Point p : keys) {
			List<Integer> lInt = conflicts.get(p);
			lInt.remove((Object) app.getPkApplication());
		}
	}

	/**
	 * Get all the applications in the databases
	 * @return A List containing all the applications 
	 */
	public List<TApplication> getlApp() {
		List<TApplication> lApp = dao.getAllApps();
		return lApp;
	}

	/**
	 * Get all the Driveboxes of a user (use his login)
	 * @return A List containing all the Driveboxes
	 */
	public List<TDrivebox> getDriveboxList() {
		String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		// System.out.println("getDriveboxList");
		return dao.getDriveboxByLogin(login);
	}

	/**
	 * Retrieve the selectionned Drivebox and loads
	 * the driveboxConfig page 
	 * @return
	 */
	public String editDrivebox() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idStr = params.get("id");
		int pk = Integer.parseInt(idStr);
		this.pkDrivebox = pk;
		initializeInstalledApps();
		return "driveboxConfig.xhtml";
	}

	/**
	 * Get the icon of the application installed on the (x:y) cell of the dashboard grid
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @return The link to the icon
	 */
	public String installedAppIcon(int x, int y) {
		if (isInstalledApp(x, y)) {
			return app.getTApplication().getIcone();
		}
		return "noApp.png";
	}
	
	/**
	 * Get the name of the application installed on the (x:y) cell of the dashboard grid
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @return The name of the application
	 */
	public String installedAppName(int x, int y){
		if (isInstalledApp(x, y)) {
			return app.getTApplication().getName();
		}
		return "";
	}

	/**
	 * Checks if an application is currently installed on the (x:y) cell of the dashboard
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @return True if an application is currently installed
	 */
	public boolean isInstalledApp(int x, int y) {
		try {
			List<TDriveboxHasApplication> installedApps = dao.getInstalledAppsFromPkDrivebox(this.pkDrivebox);
			for (TDriveboxHasApplication d : installedApps) {
				if (xml.isAtPosition(d.getConfigurationXml(), x, y)) {
					app = d;
					return true;
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if an icon has to be displayed on the (x:y) cell of the dashboard
	 * @param x The x coordinates
	 * @param y The y coordinates
	 * @return True if an icon has to be displayed 
	 */
	public boolean iconHasToBeDisplayed(int x, int y) {
		return !notInstalledApp[x][y];
	}

	/**
	 * 
	 * @param pkDrivebox
	 * @return
	 */
	public boolean isInstalledApp(int pkApplication) {
		List<TDriveboxHasApplication> installedApps = dao.getInstalledAppsFromPkDrivebox(this.pkDrivebox);
		for (TDriveboxHasApplication d : installedApps) {
			if (d.getTApplication().getPkApplication() == pkApplication)
				return true;
		}
		return false;
	}

	/**
	 * Reinitialize the notInstalledApp tab and the conflicts Map
	 */
	public void initializeInstalledApps() {
		for (int i = 0; i < notInstalledApp.length; i++) {
			for (int j = 0; j < notInstalledApp[i].length; j++) {
				notInstalledApp[i][j] = false;
			}
		}
		conflicts.clear();
	}

	/**
	 * Saves the Drivebox parameters(name, transfert number and mute) in the database
	 */
	public void saveParams() {
		String name = (String) nameInput.getValue();
		String telNum = (String) numInput.getValue();
		boolean mute = (Boolean) muteInput.getValue();
		nameInput.setValue("");
		numInput.setValue("");
		muteInput.setValue(false);
		dao.updateDrivebox(this.pkDrivebox, name, telNum, mute);
		initializeInstalledApps();
	}

	public SelectItem[] getBooleanOptions() {
		return booleanOptions;
	}

	public void setMuteInput(HtmlSelectBooleanCheckbox muteInput) {
		this.muteInput = muteInput;
	}

	public HtmlSelectBooleanCheckbox getMuteInput() {
		return muteInput;
	}

	public void setNumInput(HtmlInputText numInput) {
		this.numInput = numInput;
	}

	public HtmlInputText getNumInput() {
		return numInput;
	}

	public void setNameInput(HtmlInputText nameInput) {
		this.nameInput = nameInput;
	}

	public HtmlInputText getNameInput() {
		return nameInput;
	}
}