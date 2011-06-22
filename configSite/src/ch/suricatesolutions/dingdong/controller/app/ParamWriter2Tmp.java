package ch.suricatesolutions.dingdong.controller.app;

import org.primefaces.event.FileUploadEvent;

public class ParamWriter2Tmp extends ParamBaseClass{

	@Override
	public void handleParam(int pkDrivebox, int pkApplication) {
//		System.out.println("Handle param drivebox:"+pkDrivebox+" & application:"+pkApplication);
	}

	@Override
	public void handleFileUpload(FileUploadEvent event, int pkDrivebox, int pkApplication) {
//		System.out.println("Handle file upload file:"+event.getFile().getFileName()+" drivebox:"+pkDrivebox+" & application:"+pkApplication);
	}

}
