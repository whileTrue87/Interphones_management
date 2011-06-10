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
		links.add(new MenuItem("Configuration Drivebox", "/pages/user/listDrivebox.xhtml", users));
	}

	public List<MenuItem> getLinks() {
		return links;
	}
	
	public String  logout(){
		Logger logger = Logger.getLogger("AuthenticationManager");
		final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		logger.log(Level.INFO,"Logout du user: "+request.getRemoteUser());
		request.getSession(false).invalidate();
		return "/pages/home.xhtml";
	}
	
	public boolean accepts(MenuItem mi){
		HttpServletRequest request =  ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
		return mi.accepts(request);
	}
	
}


