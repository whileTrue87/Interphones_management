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

	private List<MenuItem> links = new ArrayList<MenuItem>();
	
	public MenuController(){
//		FacesContext context = FacesContext.getCurrentInstance();
//		Principal p = context.getExternalContext().getUserPrincipal();
//		String cl = "type ="+;
		ArrayList<String> admins = new ArrayList<String>();
		admins.add("administrators");
		ArrayList<String> users = new ArrayList<String>();
		users.add("users");
		ArrayList<String> all = new ArrayList<String>();
		all.add("administrators");
		all.add("users");
		links.add(new MenuItem("Accueil", "/pages/home.xhtml", all));
		links.add(new MenuItem("Configuration Drivebox", "/pages/user/driveboxConfig.xhtml", users));
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
//		return true;
//		return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("administrators");
//		Principal p = context.getExternalContext().getUserPrincipal();
		
//		RoleServiceUtil r;
//		String cl = "type ="+p.getClass();
//		links.get(0).setName(cl);
//		if(p instanceof GenericPrincipal){
//			
//		}
		return mi.accepts(request);//context.getExternalContext().isUserInRole("administrator");//mi.accepts(context.getExternalContext());
	}
	
}


