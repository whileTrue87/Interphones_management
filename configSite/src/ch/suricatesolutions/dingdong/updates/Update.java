package ch.suricatesolutions.dingdong.updates;

import javax.ejb.Remote;


@Remote
public interface Update{
	public String getLatestCoreVersionNumber();

	public byte[] getLatestCore();

	public byte[] getLatestDashboardConfiguration(String driveboxId);

	public byte[] getApplication(String applicationId, String driveboxId);
	
	public void updateIpAdress();
}
