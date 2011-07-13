package ch.suricatesolutions.driveboxmgmttool;

import java.rmi.Naming;

import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.driveboxmgmttool.core.Core;
import ch.suricatesolutions.driveboxmgmttool.service.AgiLink;
import ch.suricatesolutions.driveboxmgmttool.service.AgiLinkImpl;
import ch.suricatesolutions.driveboxmgmttool.update.DriveboxInfoUpdate;
import ch.suricatesolutions.driveboxmgmttool.update.Updater;

public class Main {
	public static void main(String[] args) throws Exception {
		Updater upd = Updater.getInstance();
		upd.updateIpAddress();
		DriveboxInfo diu = new DriveboxInfoUpdate();
		Naming.rebind("//localhost:1099/Updater", diu); 
		AgiLink al = new AgiLinkImpl();
		Naming.rebind("//localhost:1099/Agi", al); 
		Core c = upd.update();
		if(c!= null)
			c.launch();
	}
}
