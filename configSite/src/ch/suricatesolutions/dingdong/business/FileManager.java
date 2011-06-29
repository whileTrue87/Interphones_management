package ch.suricatesolutions.dingdong.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@EJB
@Stateless
public class FileManager {

	public void copyBackBeanOnDisk(String backBeanName, byte[] backBean) throws IOException{
		if(backBeanName == null || backBeanName.equals("") || backBean==null || backBean.length==0){
			return;
		}
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String backBeanPath = ec.getRealPath("/WEB-INF/classes/ch/suricatesolutions/dingdong/controller/app");
		File backBeanFile = new File(backBeanPath+File.separator+backBeanName+".class");
		FileOutputStream fos = new FileOutputStream(backBeanFile);
		fos.write(backBean);
		fos.close();
	}
	
	public void copyPageOnDisk(String pageName, byte[] page) throws IOException{
		if(pageName == null || pageName.equals("") || page==null || page.length==0){
			return;
		}
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String appPagesPath = ec.getRealPath("/pages/user/app/");
		File confPageFile = new File(appPagesPath+File.separator+pageName);
		FileOutputStream fos = new FileOutputStream(confPageFile);
		fos.write(page);
		fos.close();
	}
}
