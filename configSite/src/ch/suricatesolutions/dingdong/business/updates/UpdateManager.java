package ch.suricatesolutions.dingdong.business.updates;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;

import org.jdom.JDOMException;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.business.XmlManager;
import ch.suricatesolutions.dingdong.model.TApplication;
import ch.suricatesolutions.dingdong.model.TDrivebox;
import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;
import ch.suricatesolutions.dingdong.updates.DriveboxInfo;
import ch.suricatesolutions.dingdong.updates.Update;

@Stateless
@ApplicationScoped
/**
 * This class is responsible to get the latest versions of
 * Programs in the database
 * @author Maxime Reymond
 */
public class UpdateManager implements Update {

	@EJB
	DaoManager dao;

	@EJB
	XmlManager xml;

	/**
	 * @return Returns the last version number of the drivebox core
	 */
	public String getLatestCoreVersionNumber() {
		return dao.getLatestCoreVersionNumber();
	}

	/**
	 * @return Returns the last version of the drivebox core
	 */
	public byte[] getLatestCore() {
		return dao.getLatestCore();
	}

	/**
	 * Gets all the configuration files of the installed application on the
	 * given drivebox and creates a global configuration file
	 * 
	 * @param driveboxId
	 *            The drivebox
	 * @return Returns the dashboard configuration file
	 */
	public byte[] getLatestDashboardConfiguration(String driveboxId) {
		if (driveboxId == null)
			return null;
		// List<byte[]> list = dao.getAppConfigFilesFromDriveboxId(driveboxId);
		Date lastDashboardModif = dao.getLastDashboardModification(driveboxId);
		int pkDrivebox = dao.getDriveboxPkFromId(driveboxId);
		List<TDriveboxHasApplication> list = dao.getInstalledAppsFromPkDrivebox(pkDrivebox);
		byte[] dashboardConfiguration = null;
		try {
			dashboardConfiguration = xml.createXmlDashboardConfigurationFile(lastDashboardModif, list);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dashboardConfiguration;
	}

	/**
	 * Gets the application identified by the given appId and its configuration
	 * file
	 * 
	 * @param applicationId
	 *            The id of the application
	 * @param driveboxId
	 *            The id of the drivebox on which the application is installed
	 * @return Returns the application and its configuration file
	 */
	public byte[] getApplication(String applicationId, String driveboxId) {
		if (applicationId == null || driveboxId == null)
			return null;
		TApplication app = dao.getApplicationFromId(applicationId);
		TDriveboxHasApplication dha = dao.getInstalledAppFromIds(applicationId, driveboxId);
		byte[] config = null;
		try {
			config = xml.createXmlAppConfiguration(dha);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.createJar(app.getDriveboxApplication(), config, dha.getParameters());
	}

	/**
	 * Create a new jar containing the drivebox application, its configuration
	 * file and its parameters
	 * 
	 * @param driveboxApplication
	 *            The jar containing the application
	 * @param configurationXml
	 *            The xml configuration file
	 * @param appParameters
	 *            The zip containing all the parameters for the application
	 * @return The new created jar
	 */
	private byte[] createJar(byte[] driveboxApplication, byte[] configurationXml, byte[] appParameters) {
		if (driveboxApplication == null || configurationXml == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			JarOutputStream jos = new JarOutputStream(baos);

			JarEntry applic = new JarEntry("application.jar");
			applic.setTime(System.currentTimeMillis());
			applic.setSize(driveboxApplication.length);
			jos.putNextEntry(applic);
			jos.write(driveboxApplication);
			jos.closeEntry();
			System.out.println(applic.getSize());

			JarEntry confFile = new JarEntry("configuration.xml");
			confFile.setTime(System.currentTimeMillis());
			confFile.setSize(configurationXml.length);
			jos.putNextEntry(confFile);
			jos.write(configurationXml);
			jos.closeEntry();

			if (appParameters != null) {
				JarEntry parameters = new JarEntry("parameters.zip");
				parameters.setSize(appParameters.length);
				parameters.setTime(System.currentTimeMillis());
				jos.putNextEntry(parameters);
				jos.write(appParameters);
				jos.closeEntry();
			}

			jos.flush();
			jos.finish();
			jos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public byte[] needAnotherUpdate(String driveboxId) {
		return null;
	}

	@Override
	public boolean updateDrivebox(int pkDrivebox) throws Exception {
		System.out.println("update drivebox " + pkDrivebox);
		TDrivebox drivebox = dao.getDriveboxFromPk(pkDrivebox);
		if (drivebox == null)
			return false;
		String ip = drivebox.getIpAddress();
		DriveboxInfo di;
		di = (DriveboxInfo) Naming.lookup("//" + ip + ":1099/Updater");
		di.updateAvailable();
		return true;
	}
}