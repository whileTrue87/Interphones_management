package ch.suricatesolutions.dingdong.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import ch.suricatesolutions.dingdong.business.DaoManager;

@ManagedBean
@SessionScoped
public class IpController {
	
	@EJB
	private DaoManager dao;

	public String getIpAddress(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		System.out.println("IP:"+request.getRemoteAddr());
		System.out.println("driveboxId:"+request.getParameter("driveboxId"));
		dao.updateDriveboxIpAdress(request.getParameter("driveboxId"),request.getRemoteAddr());
		return request.getRemoteAddr();
	}
}
