package ch.suricatesolutions.dingdong.controller.app;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

public class ParamEAlbum extends ParamBaseClass {

	@Override
	public void handleParam(int pkDrivebox, int pkApplication) {
	}

	@Override
	public void handleFileUpload(FileUploadEvent event, int pkDrivebox, int pkApplication) throws FacesException{
		byte[] zip = dao.getAppParam(pkDrivebox, pkApplication);
		byte[] newZip;
		try {
			newZip = xml.updateZip(zip, event.getFile().getContents(), event.getFile().getFileName());
			dao.updateAppParam(pkDrivebox, pkApplication, newZip);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
