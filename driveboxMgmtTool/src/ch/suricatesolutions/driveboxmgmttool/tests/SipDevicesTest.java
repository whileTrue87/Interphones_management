package ch.suricatesolutions.driveboxmgmttool.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;
import ch.suricatesolutions.driveboxmgmttool.dao.IDao;

public class SipDevicesTest {
	private IDao dao;
	private final String DEVICE_NAME="testAddDeviceOk";

	@Before
	public void setUp() throws Exception {
		dao = Dao.getInstance();
	}
	
	@Test
	public void testAddDeviceNull() throws Exception{
		SipDevice d = null;
		try {
			dao.addSipDevice(d);
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void testAddDeviceNameNull() throws Exception{
		SipDevice d = new SipDevice(null, "pass", null, 0);
		try {
			dao.addSipDevice(d);
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void testAddDevicePasswordNull() throws Exception{
		SipDevice d = new SipDevice("testAddDevicePasswordNull", null, null, 0);
		try {
			dao.addSipDevice(d);
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void testAddDeviceOk() throws Exception{
		SipDevice d = new SipDevice(DEVICE_NAME, "pass", null, 0);
		List<SipDevice> lBefore = dao.getAllDevices();
		dao.addSipDevice(d);
		List<SipDevice> lAfter = dao.getAllDevices();
		assertEquals(lBefore.size(), lAfter.size()-1);
	}
	
	@Test
	public void testIdFromNameNull() throws Exception{
		String name = null;
		int retId = dao.getIdFromName(name);
		assertEquals(retId, -1);
	}
	
	@Test
	public void testIdFromNameNOK() throws Exception{
		String name = "";
		int retId = dao.getIdFromName(name);
		assertEquals(retId, -1);
	}
	
	@Test
	public void testIdFromNameOK() throws Exception{
		int retId = dao.getIdFromName(DEVICE_NAME);
		assertTrue(retId!=-1);
	}
	
	@Test
	public void testRemoveDeviceNOk() throws Exception{
		int id = -1;
		List<SipDevice> lBefore = dao.getAllDevices();
		dao.removeSipDevice(id);
		List<SipDevice> lAfter = dao.getAllDevices();
		assertEquals(lBefore.size(), lAfter.size());
	}
	
	@Test
	public void testRemoveDeviceOk() throws Exception{
		List<SipDevice> lBefore = dao.getAllDevices();
		int id = dao.getIdFromName(DEVICE_NAME);
		dao.removeSipDevice(id);
		List<SipDevice> lAfter = dao.getAllDevices();
		assertEquals(lBefore.size(), lAfter.size()+1);
	}
	
	
}
