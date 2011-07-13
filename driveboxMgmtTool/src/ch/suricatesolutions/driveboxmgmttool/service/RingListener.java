package ch.suricatesolutions.driveboxmgmttool.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

public class RingListener extends BaseAgiScript {

	@Override
	public void service(AgiRequest arg0, AgiChannel arg1) throws AgiException {
		System.out.println("kaka");
		try {
			AgiLink agi = (AgiLink)Naming.lookup("//localhost:1099/Agi");
			agi.ring();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
