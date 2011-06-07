package ch.suricatesolutions.dingdong.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.component.dnd.Droppable;
import org.primefaces.event.DragDropEvent;

import ch.suricatesolutions.dingdong.business.Application;

@ManagedBean
public class ConfigDriveboxController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static List<Application> lApp = new ArrayList<Application>();
	private static List<Application> selectedApps = new ArrayList<Application>();

	static {
		lApp.add(new Application("App1", "appVideo.png"));
		lApp.add(new Application("App2", "appWriter.png"));
		lApp.add(new Application("App3", "appText.png"));
	}

	public ConfigDriveboxController() {

		System.out.println("constructor");
	}

	public void onDrop(DragDropEvent event) {
		Application app = (Application) event.getData();
		Droppable d = (Droppable) event.getSource();

		System.out.println(d.getParent().getClass());
		System.out.println(d.getParent().getFacetCount());
		if (!selectedApps.contains(app))
			selectedApps.add(app);
		System.out.println(d.getClass());
	}

	public void onDropBack(DragDropEvent event) {
		Application app = (Application) event.getData();
		selectedApps.remove(app);
	}

	public List<Application> getlApp() {
		return ConfigDriveboxController.lApp;
	}

	public List<Application> getSelectedApps() {
		return ConfigDriveboxController.selectedApps;
	}
}
