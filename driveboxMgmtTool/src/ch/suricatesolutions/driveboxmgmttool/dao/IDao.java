package ch.suricatesolutions.driveboxmgmttool.dao;

import java.util.List;

import ch.suricatesolutions.dingdong.updates.SipDevice;

public interface IDao {
	public void addSipDevice(SipDevice device) throws Exception;
	public void removeSipDevice(int id) throws Exception;
	public String[] getAllPeersName() throws Exception;
	public int getIdFromName(String objectName) throws Exception;
	public List<SipDevice> getAllDevices() throws Exception;
}
