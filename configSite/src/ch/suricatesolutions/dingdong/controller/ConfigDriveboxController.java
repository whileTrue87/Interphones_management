package ch.suricatesolutions.dingdong.controller;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.dnd.Droppable;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.controller.app.ParamController;
import ch.suricatesolutions.dingdong.model.TApplication;
import ch.suricatesolutions.dingdong.model.TDrivebox;
import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;
import ch.suricatesolutions.dingdong.updates.Update;

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
	//
	// @EJB
	// private UpdateManager update;

	// @EJB
	// private XmlManager xml;

	// Indicates if an application is not present before
	// the loading of the page on the dashboard grid
	// If a cell is false, an app is present
	private boolean[][] notInstalledApp = new boolean[2][5];

	// Map containing the possible conflicts on the dashboard grid
	// Each entry(Point(x,y)) represent a list of present application
	private Map<Point, List<Integer>> conflicts = new HashMap<Point, List<Integer>>();

	private SelectItem[] booleanOptions = new SelectItem[3];

	private TDriveboxHasApplication app;

	// Binding properties
	private HtmlInputText nameInput;
	private HtmlInputText numInput;
	private HtmlSelectBooleanCheckbox muteInput;
	private Dialog dialog;
	private InputStream noApp;

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	public ConfigDriveboxController() throws FileNotFoundException {
		booleanOptions[0] = new SelectItem("");
		booleanOptions[1] = new SelectItem(String.valueOf(true));
		booleanOptions[2] = new SelectItem(String.valueOf(false));
		noApp = new FileInputStream("C:\\noApp.png");
	}

	/**
	 * Is called when an application is dropped on the dashboard grid, controls
	 * the database updates
	 * 
	 * @param event
	 *            The dropped application
	 */
	public void onDrop(DragDropEvent event) {
		TApplication app = (TApplication) event.getData();
		Droppable d = (Droppable) event.getSource();
		String id = d.getId();
		String[] tokens = id.split("_");
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
				addMessage(new FacesMessage("Il existe un conflit entre plusieurs applications, Veuillez le corriger"));
				return;
			}
		}
		if (isInstalledApp(x, y)) {
			dao.disableAppFromDrivebox(this.app.getId().getPfkApplication(), this.app.getId().getPfkDrivebox());
		}

		notInstalledApp[x][y] = true;
		dao.updateInstalledApp(app.getPkApplication(), this.pkDrivebox, x, y);

	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Is called when an application is dropped back from the dashboard to the
	 * applications grid Controls the database updates
	 * 
	 * @param event
	 *            The dropped application
	 */
	public void onDropBack(DragDropEvent event) {
		TApplication app = (TApplication) event.getData();
		// System.out.println("onDropBack : pkApplication=" +
		// app.getPkApplication() + " pkDrivebox=" + this.pkDrivebox);
		dao.disableAppFromDrivebox(app.getPkApplication(), this.pkDrivebox);
		Set<Point> keys = conflicts.keySet();
		for (Point p : keys) {
			List<Integer> lInt = conflicts.get(p);
			lInt.remove((Object) app.getPkApplication());
		}
	}

	/**
	 * Get all the applications in the databases
	 * 
	 * @return A List containing all the applications
	 */
	public List<TApplication> getlApp() {
		List<TApplication> lApp = dao.getAllApps();
		return lApp;
	}

	/**
	 * Get all the Driveboxes of a user (use his login)
	 * 
	 * @return A List containing all the Driveboxes
	 */
	public List<TDrivebox> getDriveboxList() {
		String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		return dao.getDriveboxByLogin(login);
	}

	/**
	 * Retrieve the selectionned Drivebox and loads the driveboxConfig page
	 * 
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
	 * Get the icon of the application installed on the (x:y) cell of the
	 * dashboard grid
	 * 
	 * @param x
	 *            The x coordinates
	 * @param y
	 *            The y coordinates
	 * @return The link to the icon
	 */
	public StreamedContent installedAppIcon(int x, int y) {
		if (isInstalledApp(x, y)) {
			return new DefaultStreamedContent(new ByteArrayInputStream(app.getTApplication().getIcone()), "image/png");
		}
		try {
			return new DefaultStreamedContent(new FileInputStream("c:\\noApp.png"), "image/png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the name of the application installed on the (x:y) cell of the
	 * dashboard grid
	 * 
	 * @param x
	 *            The x coordinates
	 * @param y
	 *            The y coordinates
	 * @return The name of the application
	 */
	public String installedAppName(int x, int y) {
		if (isInstalledApp(x, y)) {
			return app.getTApplication().getName();
		}
		return "";
	}

	/**
	 * Checks if an application is currently installed on the (x:y) cell of the
	 * dashboard
	 * 
	 * @param x
	 *            The x coordinates
	 * @param y
	 *            The y coordinates
	 * @return True if an application is currently installed
	 */
	public boolean isInstalledApp(int x, int y) {
		List<TDriveboxHasApplication> installedApps = dao.getInstalledAppsFromPkDrivebox(this.pkDrivebox);
		for (TDriveboxHasApplication d : installedApps) {
			if (d.getxPosition() == x && d.getyPosition() == y) {
				app = d;
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if an icon has to be displayed on the (x:y) cell of the dashboard
	 * 
	 * @param x
	 *            The x coordinates
	 * @param y
	 *            The y coordinates
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
	 * Saves the Drivebox parameters(name, transfert number and mute) in the
	 * database
	 */
	public void saveParams() {
		String name = (String) nameInput.getValue();
		String telNum = (String) numInput.getValue();
		boolean mute = (Boolean) muteInput.getValue();
		nameInput.setValue("");
		numInput.setValue("");
		muteInput.setValue(false);
		dao.updateDrivebox(this.pkDrivebox, name, telNum, mute);
		addMessage(new FacesMessage("Données mises à jour"));
		// initializeInstalledApps();
	}

	public String updateParam(int pkApplication) {
		TApplication app = dao.getApplicationFromPk(pkApplication);
		String nextPage = app.getConfigurationLink();
		ParamController param = (ParamController) getManagedBean("paramController");
		// System.out.println(app.getBackBeanName());
		param.setBackBeanName(app.getBackBeanName());
		param.setPkApplication(pkApplication);
		param.setPkDrivebox(pkDrivebox);
		// System.out.println("Next Page = " + nextPage);
		return "app/" + nextPage;
	}

	public StreamedContent getAppIconStream() {// String id){
		String pkStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pkApp");
		// System.out.println("Id de l'application : " + pkStr);
		if (pkStr == null || pkStr.equals(""))
			return new DefaultStreamedContent(noApp);
		int pk = Integer.parseInt(pkStr);

		byte[] icon = dao.getApplicationFromPk(pk).getIcone();
		// System.out.println(icon.length);
		return new DefaultStreamedContent(new ByteArrayInputStream(icon), "image/png");
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

	public Object getManagedBean(final String beanName) {
		FacesContext fc = FacesContext.getCurrentInstance();

		Object bean;
		try {
			ELContext elContext = fc.getELContext();
			bean = elContext.getELResolver().getValue(elContext, null, beanName);
		} catch (RuntimeException e) {
			throw new FacesException(e.getMessage(), e);
		}

		if (bean == null) {
			throw new FacesException("Managed bean with name '" + beanName
					+ "' was not found. Check your faces-config.xml or @ManagedBean annotation.");
		}
		return bean;
	}

	public void updateDrivebox() {
		InitialContext ic;
		System.out.println("update Drivebox");
		try {
			ic = new InitialContext();
			Update update = (Update) ic.lookup("java:global/Interphones_management/UpdateManager");
			boolean ok = update.updateDrivebox(this.pkDrivebox);
			if (ok)
				addMessage(new FacesMessage("La Drivebox a été avertie de la mise à jour"));
			else
				addMessage(new FacesMessage("La Drivebox n'a pas pu être contactée"));
		} catch (Exception e) {
			addMessage(new FacesMessage("La Drivebox n'a pas pu être contactée"));
			e.printStackTrace();
		}
	}
}