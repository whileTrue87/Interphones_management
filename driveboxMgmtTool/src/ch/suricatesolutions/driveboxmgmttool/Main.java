package ch.suricatesolutions.driveboxmgmttool;

import java.rmi.Naming;

import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.driveboxmgmttool.core.Core;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;
import ch.suricatesolutions.driveboxmgmttool.dao.IDao;
import ch.suricatesolutions.driveboxmgmttool.service.SipDeviceManager;
import ch.suricatesolutions.driveboxmgmttool.update.DriveboxInfoUpdate;
import ch.suricatesolutions.driveboxmgmttool.update.Updater;

public class Main {
	public static void main(String[] args) throws Exception {
		Updater upd = Updater.getInstance();
		upd.updateIpAddress();
		DriveboxInfo diu = new DriveboxInfoUpdate();
		Naming.rebind("//192.168.56.101:1099/Updater", diu); 
		Core c = upd.update();
		if(c!= null)
			c.launch();
		
//		Properties props = new Properties();
//		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
//		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
//		props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
//		props.setProperty("org.omg.CORBA.ORBInitialHost", "confServer");
//		props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
//		InitialContext ic;l
//			try {
//				System.out.println("trying to connect");
//				ic = new InitialContext(props);
//				Update server = (Update) ic.lookup("java:global/Interphones_management/UpdateManager");
//			} catch (NamingException e) {
//				e.printStackTrace();
//			}
	}
}
