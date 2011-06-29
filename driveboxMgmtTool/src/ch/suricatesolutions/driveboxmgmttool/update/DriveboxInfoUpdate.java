package ch.suricatesolutions.driveboxmgmttool.update;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import ch.suricatesolutions.dingdong.updates.DeviceStatus;
import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.dingdong.updates.SipDevice;

public class DriveboxInfoUpdate extends UnicastRemoteObject implements
		DriveboxInfo {

	public DriveboxInfoUpdate() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 2279177960337660023L;

	@Override
	public List<SipDevice> getSipDevicesStatus() throws RemoteException {
		System.out.println("getSipDeviceStatus");
		SipDevice d = new SipDevice("1234DDE", "Interphone", DeviceStatus.ON);
		SipDevice d2 = new SipDevice("ABCDEF12", "Téléphone", DeviceStatus.ON);
		List<SipDevice> lSD = new ArrayList<SipDevice>();
		lSD.add(d);
		lSD.add(d2);
		return lSD;
	}

	@Override
	public void updateAvailable() throws RemoteException {
		System.out.println("updateAvailable");
	}
}
