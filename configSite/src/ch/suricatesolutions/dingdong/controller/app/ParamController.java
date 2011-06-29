package ch.suricatesolutions.dingdong.controller.app;

import java.lang.reflect.Constructor;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.business.XmlManager;

@ManagedBean
@SessionScoped
public class ParamController {
	@EJB
	DaoManager dao;

	@EJB
	XmlManager xml;
	private String backBeanName;
	private int pkDrivebox;
	private int pkApplication;
	private ParamBaseClass backBean;

	public void handleFileUpload(FileUploadEvent event) {
//		System.out.println("backBeanName=" + backBeanName);
		try {
			backBean.handleFileUpload(event, pkDrivebox, pkApplication);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Le fichier "+event.getFile().getFileName()+" a été ajouté aux paramètres"));
		} catch (FacesException e) {
			String error = "Une erreur s'est produite lors de l'enregistrement de vos paramètres";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		}catch (Exception e) {
			String error = "Une erreur s'est produite lors de l'enregistrement de vos paramètres";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		}
	}

	public void handleParams() {
//		System.out.println("backBeanName=" + backBeanName);
		try {
			backBean.handleParam(pkDrivebox, pkApplication);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBackBean(String backBeanName) throws Exception {
		this.backBeanName = backBeanName;
		String mainClass = "ch.suricatesolutions.dingdong.controller.app." + backBeanName;
		Class<?> coreCls = Class.forName(mainClass);
		Constructor<?> construct;
		construct = coreCls.getConstructor();
		backBean = (ParamBaseClass) construct.newInstance();
		backBean.setDao(dao);
		backBean.setXml(xml);
	}
	
	public void clearParams(){
		dao.clearParams(pkDrivebox, pkApplication);
	}

	public String getBackBeanName() {
		return backBeanName;
	}

	public void setPkDrivebox(int pkDrivebox) {
		this.pkDrivebox = pkDrivebox;
	}

	public int getPkDrivebox() {
		return pkDrivebox;
	}

	public void setPkApplication(int pkApplication) {
		this.pkApplication = pkApplication;
	}

	public int getPkApplication() {
		return pkApplication;
	}
}
