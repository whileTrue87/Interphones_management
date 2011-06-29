package ch.suricatesolutions.dingdong.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.model.TDrivebox;

@ManagedBean
@SessionScoped
public class AllDriveboxesController {
	@EJB
	DaoManager dao;
	
	public List<TDrivebox> getDriveboxesList(){
		return dao.getAllDriveboxes();
	}
}
