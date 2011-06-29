package ch.suricatesolutions.dingdong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import ch.suricatesolutions.dingdong.business.MenuItem;

@ManagedBean
/**
 * Controls the display of the user menu
 */
public class MenuController {
	private static final String ADMINISTRATORS="administrators";
	private static final String USERS="users";

	private List<MenuItem> links = new ArrayList<MenuItem>();
	
	public MenuController(){
		ArrayList<String> admins = new ArrayList<String>();
		admins.add(ADMINISTRATORS);
		ArrayList<String> users = new ArrayList<String>();
		users.add(USERS);
		ArrayList<String> all = new ArrayList<String>();
		all.add(ADMINISTRATORS);
		all.add(USERS);
		
		links.add(new MenuItem("Accueil", "/pages/home.xhtml", all));
		links.add(new MenuItem("Configuration Driveboxes", "/pages/user/listDrivebox.xhtml", users));
		links.add(new MenuItem("Administration des Drivebox", "/pages/admin/allDriveboxes.xhtml", admins));
		links.add(new MenuItem("Gérer les applications", "/pages/admin/appAdding.xhtml", admins));
		links.add(new MenuItem("Test", "/pages/user/testDynAdd.xhtml", users));
	}

	public List<MenuItem> getLinks() {
		return links;
	}
	
	/**
	 * Destroy the session of the user
	 * @return The next page to display
	 */
	public String  logout(){
		Logger logger = Logger.getLogger("AuthenticationManager");
		final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		logger.log(Level.INFO,"Logout du user: "+request.getRemoteUser());
		request.getSession(false).invalidate();
		return "/pages/home.xhtml";
	}
	
	/**
	 * Checks if the users is authorized to see the given MenuItem
	 * @param mi The MenuItem to check with the user
	 * @return True if the user is authorized to see the MenuItem
	 */
	public boolean accepts(MenuItem mi){
		HttpServletRequest request =  ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
		return mi.accepts(request);
	}
	
}


