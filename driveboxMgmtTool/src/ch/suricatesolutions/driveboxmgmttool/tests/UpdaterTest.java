package ch.suricatesolutions.driveboxmgmttool.tests;

import org.junit.Before;
import org.junit.Test;

import ch.suricatesolutions.driveboxmgmttool.update.Updater;

public class UpdaterTest {
	private Updater update;

	@Before
	public void setUp() throws Exception {
		update = Updater.getInstance();
	}
	
	@Test
	public void getLatestDashboardConfigurationNull(){
//		byte[] result = update.getLatestDashboardConfiguration(null);
//		assertEquals(result, null);
	}
}
