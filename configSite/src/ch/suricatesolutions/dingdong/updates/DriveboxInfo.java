package ch.suricatesolutions.dingdong.updates;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;

public interface DriveboxInfo extends Remote, Serializable{
	public void updateAvailable();
	public List<SipDevice> getSipDevicesStatus();
}
