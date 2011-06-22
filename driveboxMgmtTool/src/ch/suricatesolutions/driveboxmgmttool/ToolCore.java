package ch.suricatesolutions.driveboxmgmttool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import ch.suricatesolutions.dingdong.applications.Application;
import ch.suricatesolutions.driveboxmgmttool.core.Core;
import ch.suricatesolutions.driveboxmgmttool.update.Updater;

@SuppressWarnings("unchecked")
public class ToolCore implements Core {

	private static String dashboard_config_filename = "dashboard_config.xml";

	@Override
	/**
	 * Launchs the application
	 */
	public void launch() {
		JFrame jf = new JFrame();
		jf.getContentPane().add(new JLabel("coucou"));
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	@Override
	/**
	 * Desinstalls the application (remove all the files) 
	 */
	public void desinstall() {
		System.err.println("Desinstall");
	}

	@Override
	/**
	 * Updates the dashboard
	 */
	public void update() {
		if(Updater.getInstance().getServer() == null)
			return;
		byte[] dashboard_config = Updater.getInstance().getServer().getLatestDashboardConfiguration(Updater.drivebox_id);
		if (dashboard_config == null)
			return;
		try {
			List<Process> lProc = checkUpdate();
			if (lProc != null) {
				for (Process proc : lProc) {
					proc.waitFor();
				}
			}
			FileOutputStream baos = new FileOutputStream(new File(dashboard_config_filename));
			baos.write(dashboard_config);
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if dashboard updates are needed and then install them
	 * 
	 * @return A List containing all the process of the application's updates
	 * @throws Exception
	 */
	private List<Process> checkUpdate() throws Exception {
		File dashboard_config = new File(dashboard_config_filename);
		List<Process> result = new ArrayList<Process>();
		byte[] newConfig = Updater.getInstance().getServer().getLatestDashboardConfiguration(Updater.drivebox_id);
		if (newConfig == null)
			return null;
		SAXBuilder sxb = new SAXBuilder();
		ByteArrayInputStream baos_newConfig = new ByteArrayInputStream(newConfig);
		Document newDoc = sxb.build(baos_newConfig);
		Element newRoot = newDoc.getRootElement();
		if (dashboard_config.exists()) {
			FileInputStream fis_oldConfig = new FileInputStream(dashboard_config);
			Document oldDoc = sxb.build(fis_oldConfig);
			Element oldRoot = oldDoc.getRootElement();
			XPath xpa = XPath.newInstance("/dashboard/lastModification");
			xpa = XPath.newInstance("/dashboard/applications/application");
			List<Element> newApplications = xpa.selectNodes(newRoot);
			for (Element newApp : newApplications) {
				String id = newApp.getAttributeValue("className");
				xpa = XPath.newInstance("/dashboard/applications/application[@className=\"" + id + "\"]");
				Element oldApp = (Element) xpa.selectSingleNode(oldRoot);
				String newAppVersion = newApp.getAttributeValue("version");
				if (oldApp == null) {
					System.err.println("New application " + id + " v." + newAppVersion + " installed");
					result.add(installApp(id));
				} else {
					String newLastModDate = newApp.getAttributeValue("lastModification");
					String oldLastModDate = oldApp.getAttributeValue("lastModification");
					Application oldJavaApp = getApplication(id);
					String oldAppVersion = oldApp.getAttributeValue("version");
					if (!oldAppVersion.equals(newAppVersion)) {
						System.err.println("Application" + id + " Updated from version v." + oldAppVersion + " to version v."
								+ newAppVersion);
						oldJavaApp.desinstall();
						result.add(installApp(id));
					} else if (!newLastModDate.equals(oldLastModDate)) {
						System.err.println("Application" + id + " parameters updated");
						BeanApp ba = new BeanApp(Updater.getInstance().getServer().getApplication(id, Updater.drivebox_id));
						oldJavaApp.updateParam(ba.getConfigFile(), ba.getParameters());
					} else {
						System.err.println("Application " + id + " don't need any update");
					}
				}
			}

			// Desinstallation of application that are not in the dashboard
			// anymore
			xpa = XPath.newInstance("/dashboard/applications/application");
			List<Element> oldApplications = xpa.selectNodes(oldRoot);
			for (Element oldApp : oldApplications) {
				String id = oldApp.getAttributeValue("className");
				xpa = XPath.newInstance("/dashboard/applications/application[@className=\"" + id + "\"]");
				Element newApp = (Element) xpa.selectSingleNode(newRoot);
				if (newApp == null) {
					System.err.println("Desinstallation of application " + id);
					Application oldJavaApp = getApplication(id);
					oldJavaApp.desinstall();
				}

			}
			fis_oldConfig.close();
		} else {
			System.err.println("No dashboard configuration file found --> installation of all applications");
			result.addAll(installAllApps(newRoot));
		}
		baos_newConfig.close();
		return result;
	}

	/**
	 * Install all the apps containing in the given configuration file
	 * 
	 * @param root
	 *            The root of the configuration file
	 * @return A List containing all the process of the application's updates
	 * @throws Exception
	 */
	private List<Process> installAllApps(Element root) throws Exception {
		XPath xpa = XPath.newInstance("/dashboard/applications/application");
		List<Element> newApplications = xpa.selectNodes(root);
		List<Process> lProc = new ArrayList<Process>();
		for (Element newApp : newApplications) {
			System.err.println("New application " + newApp.getAttributeValue("className") + " v."
					+ newApp.getAttributeValue("version") + " installed");
			lProc.add(installApp(newApp.getAttributeValue("className")));
		}
		return lProc;
	}

	/**
	 * Creates a new Application object from the given id
	 * 
	 * @param id
	 *            The id (class name) of the application
	 * @return The instance of the application
	 * @throws Exception
	 */
	private Application getApplication(String id) throws Exception {
		Class<?> appCls = Class.forName(id);
		Constructor<?> construct = appCls.getConstructor();
		Application app = (Application) construct.newInstance();
		return app;
	}

	/**
	 * Install the application identified by the given id
	 * 
	 * @param appId
	 *            The id of the application
	 * @return The process of the installation
	 * @throws Exception
	 */
	private Process installApp(String appId) throws Exception {
		byte[] app = Updater.getInstance().getServer().getApplication(appId, Updater.drivebox_id);
		BeanApp ba = new BeanApp(app);

		String tempDir = "temp/app/" + System.currentTimeMillis() + "/";
		File tempDirF = new File(tempDir);
		if (!tempDirF.exists())
			tempDirF.mkdirs();

		String appName = tempDir + "application.jar";
		String confFilename = tempDir + "configuration.xml";
		String paramFilename = tempDir + "parameters.zip";
		File appFile = new File(appName);
		File configFile = new File(confFilename);
		File paramFile = new File(paramFilename);

		FileOutputStream baos = new FileOutputStream(appFile);
		baos.write(ba.getAppJar());
		baos.close();
		baos = new FileOutputStream(configFile);
		baos.write(ba.getConfigFile());
		baos.close();
		if (ba.getParameters() != null) {
			baos = new FileOutputStream(paramFile);
			baos.write(ba.getParameters());
			baos.close();
		}

		String cmd = "java -jar " + appName + " " + Updater.app_install_path + " " + confFilename
				+ (ba.getParameters() == null ? "" : (" " + paramFilename));
		Process proc = Runtime.getRuntime().exec(cmd);
		proc.waitFor();
		appFile.delete();
		paramFile.delete();
		configFile.delete();
		tempDirF.delete();
		return proc;
	}
}

class BeanApp {
	private byte[] appJar;
	private byte[] configFile;
	private byte[] parameters;

	public BeanApp(byte[] completeJar) throws Exception {
		if (completeJar == null)
			return;
		String tempDir = "temp/app/" + System.currentTimeMillis() + "/";
		File tempDirF = new File(tempDir);
		if (!tempDirF.exists())
			tempDirF.mkdirs();

		String tempJar = tempDir + "tempApp" + System.currentTimeMillis() + ".jar";
		File tempJarF = new File(tempJar);
		FileOutputStream fos = new FileOutputStream(tempJarF);
		fos.write(completeJar);
		fos.close();

		JarFile jf = new JarFile(tempJar);

		JarEntry applicationJar = jf.getJarEntry("application.jar");
		appJar = new byte[(int) applicationJar.getSize()];
		jf.getInputStream(applicationJar).read(appJar);

		JarEntry configurationXml = jf.getJarEntry("configuration.xml");
		configFile = new byte[(int) configurationXml.getSize()];
		jf.getInputStream(configurationXml).read(configFile);

		JarEntry parametersZip = jf.getJarEntry("parameters.zip");
		if (parametersZip != null) {
			parameters = new byte[(int) parametersZip.getSize()];
			jf.getInputStream(parametersZip).read(parameters);
		}
		jf.close();
		tempJarF.delete();
		tempDirF.delete();
	}

	public byte[] getAppJar() {
		return appJar;
	}

	public void setAppJar(byte[] appJar) {
		this.appJar = appJar;
	}

	public byte[] getConfigFile() {
		return configFile;
	}

	public void setConfigFile(byte[] configFile) {
		this.configFile = configFile;
	}

	public byte[] getParameters() {
		return parameters;
	}

	public void setParameters(byte[] parameters) {
		this.parameters = parameters;
	}
}
