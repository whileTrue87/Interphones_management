package ch.suricatesolutions.dingdong.updates;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DriveboxInfo extends Remote, Serializable{
	public void updateAvailable() throws RemoteException;
	public List<SipDevice> getSipDevicesStatus() throws RemoteException;
}
