package ch.suricatesolutions.dingdong.controller.app;

import javax.ejb.Remote;

import org.primefaces.event.FileUploadEvent;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.business.XmlManager;

@Remote
public abstract class ParamBaseClass{
	DaoManager dao;
	XmlManager xml;
	
	public DaoManager getDao() {
		return dao;
	}
	public void setDao(DaoManager dao) {
		this.dao = dao;
	}
	public XmlManager getXml() {
		return xml;
	}
	public void setXml(XmlManager xml) {
		this.xml = xml;
	}
	
	public abstract void handleParam(int pkDrivebox, int pkApplication);
	public abstract void handleFileUpload(FileUploadEvent event, int pkDrivebox, int pkApplication);
	
}
