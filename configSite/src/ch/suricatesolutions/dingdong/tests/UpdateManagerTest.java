package ch.suricatesolutions.dingdong.tests;

import static org.junit.Assert.assertEquals;

import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.Test;

import ch.suricatesolutions.dingdong.updates.Update;

public class UpdateManagerTest {
	private Update update;

	@Before
	public void setUp() throws Exception {
		InitialContext ic = new InitialContext();
		update = (Update) ic.lookup("java:global/Interphones_management/UpdateManager");
	}
	
	@Test
	public void getLatestDashboardConfigurationBadId(){
		byte[] result = update.getLatestDashboardConfiguration("asddff");
		assertEquals(result, null);
	}
	
	@Test
	public void getLatestDashboardConfigurationNull(){
		byte[] result = update.getLatestDashboardConfiguration(null);
		assertEquals(result, null);
	}
	
	@Test
	public void getApplicationNull(){
		byte[] result = update.getApplication(null, null);
		assertEquals(result, null);
	}
	
	@Test
	public void getApplicationBadAppId(){
		byte[] result = update.getApplication("asdfdf", null);
		assertEquals(result, null);
	}
	
	@Test
	public void getApplicationBadDriveboxId(){
		byte[] result = update.getApplication(null, "asdsf");
		assertEquals(result, null);
	}
}
