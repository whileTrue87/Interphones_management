package ch.suricatesolutions.driveboxmgmttool;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import ch.suricatesolutions.driveboxmgmttool.core.Core;
import ch.suricatesolutions.driveboxmgmttool.update.Updater;

public class Main {
	public static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(1099);
		Updater upd = Updater.getInstance();
		upd.updateIpAddress();
		Naming.rebind("//localhost:1099/Updater", upd); 
		Core c = upd.update();
		if(c!= null)
			c.launch();
	}
}
