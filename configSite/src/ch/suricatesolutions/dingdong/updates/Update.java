package ch.suricatesolutions.dingdong.updates;

import java.util.List;

import javax.ejb.Remote;


@Remote
/**
 * This interface represents the update manager, accessible by RMI
 */
public interface Update{
	public String getLatestCoreVersionNumber();

	public byte[] getLatestCore();

	public byte[] getLatestDashboardConfiguration(String driveboxId);

	public byte[] getApplication(String applicationId, String driveboxId);
	
	public byte[] needAnotherUpdate(String driveboxId);
	
	public boolean updateDrivebox(int pkDrivebox) throws Exception;
	
	public List<SipDevice> getDeviceStatus(int pkDrivebox) throws Exception;
}
