package ch.suricatesolutions.dingdong.tests;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ch.suricatesolutions.dingdong.business.FileManager;

public class FileManagerTest {
	private FileManager file;

	@Before
	public void setUp() throws Exception {
		file = new FileManager();
	}

	@Test
	public void getLatestDashboardConfigurationBadId() throws IOException {
		byte[] b = new byte[]{33,33,33};
		file.copyBackBeanOnDisk("asdf", b);
//		assertEquals(result, null);
	}
}
