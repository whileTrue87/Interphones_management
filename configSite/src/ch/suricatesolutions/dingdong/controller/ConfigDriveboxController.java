package ch.suricatesolutions.dingdong.controller;

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

import com.sun.faces.taglib.html_basic.SelectBooleanCheckboxTag;

@ManagedBean
@SessionScoped
public class ConfigDriveboxController implements Serializable {

	private int pkDrivebox;
	private static final long serialVersionUID = 1L;

	@EJB
	private DaoManager dao;

	@EJB
	private XmlManager xml;

	private boolean[][] installedApp = new boolean[2][5];

	private Map<MyPoint, List<Integer>> conflicts = new HashMap<MyPoint, List<Integer>>();

	private SelectItem[] booleanOptions = new SelectItem[3];

	private TDriveboxHasApplication app;

	private HtmlInputText nameInput;
	private HtmlInputText numInput;
	private HtmlSelectBooleanCheckbox muteInput;

	public ConfigDriveboxController() {
		booleanOptions[0] = new SelectItem("");
		booleanOptions[1] = new SelectItem(String.valueOf(true));
		booleanOptions[2] = new SelectItem(String.valueOf(false));
	}

	public void onDrop(DragDropEvent event) {
		try {
			TApplication app = (TApplication) event.getData();
			Droppable d = (Droppable) event.getSource();
			String id = d.getId();
			// System.out.println("id="+id);
			String[] tokens = id.split("_");
			byte[] configFile = null;
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);

			Set<MyPoint> keys = conflicts.keySet();
			for (MyPoint p : keys) {
				List<Integer> lInt = conflicts.get(p);
				lInt.remove((Object) app.getPkApplication());
			}

			List<Integer> lInt = conflicts.get(new MyPoint(x, y));
			if (lInt == null) {
				lInt = new ArrayList<Integer>();
			}
			lInt.add(app.getPkApplication());
			conflicts.put(new MyPoint(x, y), lInt);

			keys = conflicts.keySet();
			for (MyPoint p : keys) {
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
			installedApp[x][y] = true;
			// System.out.println("IsInstalled=" + isInstalled);
			if (isInstalled) {
				TDriveboxHasApplication dHA = dao.getInstalledAppFromPks(app.getPkApplication(), this.pkDrivebox);
				if (dHA != null)
					configFile = xml.UpdateConfigurationFile(dHA.getConfigurationXml(), x, y);
			} else {

				configFile = xml.UpdateConfigurationFile(app.getConfigurationSchema(), x, y);
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

	public void onDropBack(DragDropEvent event) {
		TApplication app = (TApplication) event.getData();
		System.out.println("onDropBack : pkApplication=" + app.getPkApplication() + " pkDrivebox=" + this.pkDrivebox);
		dao.disableAppFromDrivebox(app.getPkApplication(), this.pkDrivebox);
		Set<MyPoint> keys = conflicts.keySet();
		for (MyPoint p : keys) {
			List<Integer> lInt = conflicts.get(p);
			lInt.remove((Object) app.getPkApplication());
		}
	}

	public List<TApplication> getlApp() {
		List<TApplication> lApp = dao.getAllApps();
		// System.out.println(selectedApps.size());
		return lApp;
	}

	public List<TDrivebox> getDriveboxList() {
		String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		// System.out.println("getDriveboxList");
		return dao.getDriveboxByLogin(login);
	}

	public String editDrivebox() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idStr = params.get("id");
		int pk = Integer.parseInt(idStr);
		this.pkDrivebox = pk;
		initializeInstalledApps();
		return "driveboxConfig.xhtml";
	}

	public void showParams() {// int pk){
		initializeInstalledApps();
		// System.out.println("id=" + driveboxId);
		// System.out.println("pk="+pk);
	}

	public String installedAppIcon(int x, int y) {
		if (isInstalledApp(x, y)) {
			return app.getTApplication().getIcone();
		}
		return "";
	}
	
	public String installedAppName(int x, int y){
		if (isInstalledApp(x, y)) {
			return app.getTApplication().getName();
		}
		return "";
	}

	public boolean isInstalledApp(int x, int y) {
		try {
			List<TDriveboxHasApplication> installedApps = dao.getInstalledAppsFromPkDrivebox(this.pkDrivebox);
			// System.out.println("Nombre d'app installées : "
			// + installedApps.size());
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

	public boolean iconHasToBeDisplayed(int x, int y) {
		return !installedApp[x][y];
	}

	public boolean isInstalledApp(int pkDrivebox) {
		// System.out.println("id=" + pkDrivebox);
		List<TDriveboxHasApplication> installedApps = dao.getInstalledAppsFromPkDrivebox(this.pkDrivebox);
		// System.out.println("Nombre d'app installées : " +
		// installedApps.size());
		for (TDriveboxHasApplication d : installedApps) {
			if (d.getTApplication().getPkApplication() == pkDrivebox)
				return true;
		}
		return false;
	}

	public boolean isNotInstalledApp(int pk) {
		// System.out.println("Is installed = " + isInstalledApp(id));
		return !isInstalledApp(pk);
	}

	public void initializeInstalledApps() {
		for (int i = 0; i < installedApp.length; i++) {
			for (int j = 0; j < installedApp[i].length; j++) {
				installedApp[i][j] = false;
			}
		}
		conflicts.clear();
	}

	public void saveParams() {
		System.out.println("saveParams");
		String name = (String) nameInput.getValue();
		String telNum = (String) numInput.getValue();
		boolean mute = (Boolean) muteInput.getValue();
		
		nameInput.setValue("");
		numInput.setValue("");
		muteInput.setValue(false);
//		System.out.println("name=" + name + " tel=" + telNum + " mute=" + mute);
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

class MyPoint {
	private int x;
	private int y;

	public MyPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyPoint other = (MyPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
