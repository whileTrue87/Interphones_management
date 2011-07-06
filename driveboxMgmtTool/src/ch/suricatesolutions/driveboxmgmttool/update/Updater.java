package ch.suricatesolutions.driveboxmgmttool.update;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.dingdong.updates.Update;
import ch.suricatesolutions.driveboxmgmttool.core.Core;

public class Updater implements DriveboxInfo{
	private static final long serialVersionUID = -1500777673140019426L;
	private static final int MAX_CONNECT_TRIES = 3;
	private static String core_config_filename = "core_config.xml";
	public static String drivebox_id;
	public final static String app_install_path = "app/";
	private static Updater instance;
	private Update server;

	static {
		File base_config = new File("base_config.xml");
		if (base_config.exists()) {
			try {
				SAXBuilder sxb = new SAXBuilder();
				Document doc = null;
				FileInputStream fis = new FileInputStream(base_config);
				doc = sxb.build(fis);
				Element root = doc.getRootElement();
				Element driveboxId = root.getChild("driveboxId");
				if(driveboxId != null)
				drivebox_id = driveboxId.getText();
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Updater() {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
		props.setProperty("org.omg.CORBA.ORBInitialHost", "192.168.56.1");
		props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
		InitialContext ic;
		for (int i = 0; i < MAX_CONNECT_TRIES; i++) {
			System.err.println("Try nb "+i);
			try {
				ic = new InitialContext(props);
				server = (Update) ic.lookup("java:global/Interphones_management/UpdateManager");
				System.err.println("Connection made");
				if (server != null)
					break;
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	public static Updater getInstance() {
		if (instance == null)
			instance = new Updater();
		return instance;
	}

	public Update getServer() {
		return this.server;
	}

	/**
	 * Updates the environnement if necessary and the DriveboxMgmtTool
	 * 
	 * @return The instance of the updated DriveboxMgmtTool
	 * @throws Exception
	 */
	public Core update() throws Exception {
//		Class<?> coreCls = Class.forName("ch.suricatesolutions.driveboxmgmttool.ToolCore");
//		Constructor<?> construct = coreCls.getConstructor();
//		Core core = (Core) construct.newInstance();
//		return core;
		if(server == null)
			return null;
//		 ----------------- Environnement updates ---------------------
		byte[] soft;
		while ((soft = server.needAnotherUpdate(drivebox_id)) != null) {
			String temp = "temp/";
			File tempDir = new File(temp);
			String tempFile = temp + "soft" + System.currentTimeMillis() + ".jar";
			File tempFileF = new File(tempFile);
			if (!tempDir.exists())
				tempDir.mkdirs();
			FileOutputStream baos = new FileOutputStream(tempFileF);
			baos.write(soft);
			baos.close();
			Process proc = Runtime.getRuntime().exec("java -jar " + tempFile);
			proc.waitFor();
			tempFileF.delete();
		}

		// ----------------- DriveboxMgmtTool updates ------------------

		File core_config = new File(core_config_filename);
		if (!core_config.exists())
			createCoreConfigFile(core_config);
		Process updateCore = this.checkUpdateCore();

		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		FileInputStream fis = new FileInputStream(core_config);
		doc = sxb.build(fis);
		Element root = doc.getRootElement();
		if (updateCore != null)
			updateCore.waitFor();

		Core core = getCore(root);
		fis.close();
		core.update();
		return core;
	}

	private void createCoreConfigFile(File core_config) throws IOException {
		core_config.createNewFile();
		Element root = new Element("core");
		Element version = new Element("version");
		Element mainClass = new Element("mainClass");
		root.addContent(version);
		root.addContent(mainClass);
		Document doc = new Document();
		doc.setRootElement(root);

		XMLOutputter xmlOut = new XMLOutputter();
		FileOutputStream out = new FileOutputStream(core_config);
		xmlOut.output(doc, out);
		out.close();
	}

	/**
	 * Checks if the core needs updates and updates it
	 * 
	 * @return The process of the update application
	 * @throws Exception
	 */
	private Process checkUpdateCore() throws Exception {
		File core_config = new File(core_config_filename);
		Process result = null;
		if (core_config.exists()) {
			String version = server.getLatestCoreVersionNumber();
			if (version == null)
				return null;
			SAXBuilder sxb = new SAXBuilder();
			Document doc = null;
			FileInputStream fis = new FileInputStream(core_config);
			doc = sxb.build(fis);
			Element root = doc.getRootElement();
			XPath xpa = XPath.newInstance("/core/version");
			Element crtVersion = (Element) xpa.selectSingleNode(root);
			if (!crtVersion.getText().equals(version)) {
				fis.close();
				Core core = getCore(root);
				if (core != null)
					core.desinstall();
				System.err.println("Install new core v." + version);
				result = installNewCore();
			} else {
				System.err.println("Core version up to date");
			}
		} else
			result = installNewCore();
		return result;
	}

	/**
	 * Gets an instance of the Core class described in the given root
	 * configuration file
	 * 
	 * @param root
	 *            The root of the configuration file
	 * @return Returns an instance of the Core application
	 * @throws Exception
	 */
	private Core getCore(Element root) throws Exception {
		XPath xpa = XPath.newInstance("/core/mainClass");
		String mainClass = ((Element) xpa.selectSingleNode(root)).getText();
		if (mainClass == null || mainClass.equals(""))
			return null;
		Class<?> coreCls = Class.forName(mainClass);
		Constructor<?> construct = coreCls.getConstructor();
		Core core = (Core) construct.newInstance();
		return core;
	}

	/**
	 * Install the new Core downloaded from the server
	 * 
	 * @return The process of the update routine
	 * @throws Exception
	 */
	private Process installNewCore() throws Exception {
		byte[] jar = server.getLatestCore();
		if (jar == null)
			return null;

		String temp = "temp/";
		File tempDir = new File(temp);
		String tempFile = temp + "core" + System.currentTimeMillis() + ".jar";
		File tempFileF = new File(tempFile);
		if (!tempDir.exists())
			tempDir.mkdirs();
		FileOutputStream fos = new FileOutputStream(tempFileF);
		fos.write(jar);
		fos.close();

		JarFile jf = new JarFile(tempFile);
		JarEntry configFile = jf.getJarEntry("core_config.xml");
		byte[] conf = new byte[(int) configFile.getSize()];
		jf.getInputStream(configFile).read(conf);
		jf.close();

		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(conf);
		doc = sxb.build(bais);

		File core_config = new File(core_config_filename);
		XMLOutputter xmlOut = new XMLOutputter();
		FileOutputStream out = new FileOutputStream(core_config);
		xmlOut.output(doc, out);
		out.close();

		Process proc = Runtime.getRuntime().exec("java -jar " + tempFile);
		proc.waitFor();
		tempFileF.delete();

		return proc;
	}

	public void updateIpAddress() throws IOException {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				System.err.println("send ip address");
				URL url;
				try {
					url = new URL("http://192.168.56.1:8080/Interphones_management/IP.xhtml?driveboxId=" + drivebox_id);
					url.openStream();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, 0, 300000);
	}

	@Override
	public void updateAvailable() {
	}

	@Override
	public List<SipDevice> getSipDevicesStatus() {
		return null;
	}

	@Override
	public boolean addSipDevice(SipDevice arg0) {
		return false;
	}

	@Override
	public boolean deleteSipDevice(int arg0) throws RemoteException {
		return false;
	}
}
