package ch.suricatesolutions.driveboxmgmttool.update;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import ch.suricatesolutions.dingdong.updates.DeviceStatus;
import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;
import ch.suricatesolutions.driveboxmgmttool.dao.IDao;
import ch.suricatesolutions.driveboxmgmttool.service.SipDeviceManager;

public class DriveboxInfoUpdate extends UnicastRemoteObject implements
		DriveboxInfo {

	public DriveboxInfoUpdate() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 2279177960337660023L;

	@Override
	public List<SipDevice> getSipDevicesStatus() throws RemoteException {
		List<SipDevice> peers = SipDeviceManager.getInstance()
				.getDeviceStatus();
		try {
			List<SipDevice> devices = Dao.getInstance().getAllDevices();

			for (SipDevice device : devices) {
				boolean found = false;
				for (SipDevice peer : peers) {
					if (peer.getId() == device.getId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					peers.add(new SipDevice(device.getName(), "",
							DeviceStatus.UNREACHABLE, device.getId()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peers;
		// System.err.println("getSipDeviceStatus");
		// SipDevice d = new SipDevice("1234DDE", "Interphone", DeviceStatus.ON,
		// 4);
		// SipDevice d2 = new SipDevice("ABCDEF12", "Téléphone",
		// DeviceStatus.ON, 4);
		// List<SipDevice> lSD = new ArrayList<SipDevice>();
		// lSD.add(d);
		// lSD.add(d2);
		// return lSD;
	}

	@Override
	public void updateAvailable() throws RemoteException {
		System.err.println("updateAvailable");
	}

	@Override
	public boolean addSipDevice(SipDevice device) throws RemoteException {
		System.err.println("Add sip device " + device.getName());
		try {
			Dao.getInstance().addSipDevice(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean deleteSipDevice(int idDevice) throws RemoteException {
		try {
			System.out.println(idDevice);
			Dao.getInstance().removeSipDevice(idDevice);
			String[] peers = Dao.getInstance().getAllPeersName();
			SipDeviceManager.getInstance().reloadPeers(peers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
