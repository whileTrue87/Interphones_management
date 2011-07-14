package ch.suricatesolutions.driveboxmgmttool.update;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import ch.suricatesolutions.dingdong.updates.DeviceStatus;
import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;
import ch.suricatesolutions.driveboxmgmttool.service.AsteriskManager;
import ch.suricatesolutions.driveboxmgmttool.service.ConfigFileManager;

public class DriveboxInfoUpdate extends UnicastRemoteObject implements
		DriveboxInfo {

	public DriveboxInfoUpdate() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 2279177960337660023L;

	@Override
	public List<SipDevice> getSipDevicesStatus() throws RemoteException {
		List<SipDevice> peers = AsteriskManager.getInstance()
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
			System.err.println("Delete sip device" + idDevice);
		try {
			Dao.getInstance().removeSipDevice(idDevice);
			String[] peers = Dao.getInstance().getAllPeersName();
			AsteriskManager.getInstance().reloadPeers(peers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean updateParameters(String number, boolean mute) throws RemoteException {
		System.err.println("Update transfert number" + number);
		boolean res = false;
		try {
			res = ConfigFileManager.getInstance().updateMuteOption(mute);
			if(number.startsWith("PSTN")){
				number = "SIP"+number.substring(4)+"@sipcallout";
			}
			res &= Dao.getInstance().updateTransfertNumber(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
}
