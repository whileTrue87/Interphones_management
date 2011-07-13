package ch.suricatesolutions.driveboxmgmttool.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgiLink extends Remote{
	public void ring() throws RemoteException;
}
