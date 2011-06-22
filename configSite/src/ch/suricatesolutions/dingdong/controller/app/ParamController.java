package ch.suricatesolutions.dingdong.controller.app;

import java.lang.reflect.Constructor;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println("backBeanName=" + backBeanName);
		String mainClass = "ch.suricatesolutions.dingdong.controller.app." + backBeanName;
		try {
			Class<?> coreCls = Class.forName(mainClass);
			Constructor<?> construct;
			construct = coreCls.getConstructor();
			ParamBaseClass paramClass = (ParamBaseClass) construct.newInstance();
			paramClass.setDao(dao);
			paramClass.setXml(xml);
			paramClass.handleFileUpload(event, pkDrivebox, pkApplication);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleParams() {
		System.out.println("backBeanName=" + backBeanName);
		String mainClass = "ch.suricatesolutions.dingdong.controller.app." + backBeanName;
		try {
			Class<?> coreCls = Class.forName(mainClass);
			Constructor<?> construct;
			construct = coreCls.getConstructor();
			ParamBaseClass paramClass = (ParamBaseClass) construct.newInstance();
			paramClass.setDao(dao);
			paramClass.setXml(xml);
			paramClass.handleParam(pkDrivebox, pkApplication);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBackBeanName(String backBeanName) {
		this.backBeanName = backBeanName;
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
