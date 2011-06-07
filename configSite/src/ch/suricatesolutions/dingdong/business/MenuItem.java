package ch.suricatesolutions.dingdong.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class MenuItem {

	private String name;
	private String link;
	private List<String> acceptedRoles;

	// private static final String LOGGER = "Edit Contract managed bean";

	public MenuItem(String name, String link, List<String> acceptedRoles) {
		super();
		this.name = name;
		this.link = link;
		this.acceptedRoles = acceptedRoles;
	}

	public boolean accepts(HttpServletRequest request) {
		for (String role : acceptedRoles) {
			if (request.isUserInRole(role))
				return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String linkTarget(){
		return link;
	}
}
