package ch.suricatesolutions.dingdong.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;

import ch.suricatesolutions.dingdong.business.XmlManager;

public class XmlManagerTest {

	private XmlManager xml;

	@Before
	public void setUp() throws Exception {
		xml = new XmlManager();
	}

//	@Test
//	public void testIsAtPositionNull() throws JDOMException, IOException {
//		boolean result = xml.isAtPosition(null, 5, 5);
//		assertEquals(false, result);
//	}

//	@Test
//	public void noIsAtPositionDataTest() throws IOException {
//		RandomAccessFile raf = new RandomAccessFile("xmlTests/noData.xml", "r");
//		raf.seek(0);
//		byte[] tab = new byte[(int) raf.length()];
//		raf.read(tab);
//		try {
//			xml.isAtPosition(tab, 5, 5);
//		} catch (JDOMException e) {
//
//		}
//	}

//	@Test
//	public void noPositionIsAtPositionTest() throws JDOMException, IOException {
//		RandomAccessFile raf = new RandomAccessFile("xmlTests/noPosition.xml", "r");
//		raf.seek(0);
//		byte[] tab = new byte[(int) raf.length()];
//		raf.read(tab);
//		boolean result = xml.isAtPosition(tab, 5, 5);
//		assertEquals(false, result);
//	}

//	@Test
//	public void malformedIsAtPositionFileTest() throws JDOMException, IOException {
//		RandomAccessFile raf = new RandomAccessFile("xmlTests/malformedFile.xml", "r");
//		raf.seek(0);
//		byte[] tab = new byte[(int) raf.length()];
//		raf.read(tab);
//		try {
//			xml.isAtPosition(tab, 5, 5);
//		} catch (JDOMException e) {
//		}
//	}

//	@Test
//	public void okIsAtPositionTest() throws JDOMException, IOException {
//		RandomAccessFile raf = new RandomAccessFile("xmlTests/ok.xml", "r");
//		raf.seek(0);
//		byte[] tab = new byte[(int) raf.length()];
//		raf.read(tab);
//		boolean result = xml.isAtPosition(tab, 5, 5);
//		assertEquals(true, result);
//	}

	@Test
	public void testUpdateConfigurationFileNull() throws JDOMException, IOException {
		byte[] result = xml.updateConfigurationFile(null, 5, 5);
		assertEquals(null, result);
	}

	@Test
	public void noDataUpdateConfigurationFileTest() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("xmlTests/noData.xml", "r");
		raf.seek(0);
		byte[] tab = new byte[(int) raf.length()];
		raf.read(tab);
		try {
			byte[] result = xml.updateConfigurationFile(tab, 5, 5);
		} catch (JDOMException e) {

		}
	}

	@Test
	public void noPositionUpdateConfigurationFileTest() throws JDOMException, IOException {
		RandomAccessFile raf = new RandomAccessFile("xmlTests/noPosition.xml", "r");
		raf.seek(0);
		byte[] tab = new byte[(int) raf.length()];
		raf.read(tab);
		byte[] result = xml.updateConfigurationFile(tab, 5, 5);

		assertEquals(tab.length, result.length);
		for (int i = 0; i < tab.length; i++) {
			assertEquals(tab[i], result[i]);
		}
	}

	@Test
	public void malformedUpdateConfigurationFileTest() throws JDOMException, IOException {
		RandomAccessFile raf = new RandomAccessFile("xmlTests/malformedFile.xml", "r");
		raf.seek(0);
		byte[] tab = new byte[(int) raf.length()];
		raf.read(tab);
		try {
			xml.updateConfigurationFile(tab, 5, 5);
		} catch (JDOMException e) {
		}
	}

//	@Test
//	public void okUpdateConfigurationFileTest() throws JDOMException, IOException {
//		RandomAccessFile raf = new RandomAccessFile("xmlTests/ok.xml", "r");
//		raf.seek(0);
//		byte[] tab = new byte[(int) raf.length()];
//		raf.read(tab);
//		byte[] result = xml.updateConfigurationFile(tab, 5, 5);
//		assertEquals(true, xml.isAtPosition(result, 5, 5));
//	}

	@Test
	public void testCreateXmlDashboardConfigurationFileNull() throws JDOMException, IOException {
		byte[] result = xml.createXmlDashboardConfigurationFile(null, null);
		assertEquals(result, null);
	}
}
