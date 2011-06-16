package ch.suricatesolutions.dingdong.business.updates;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;
import ch.suricatesolutions.dingdong.updates.Update;

@Stateless
@ApplicationScoped
public class UpdateManager implements Update {

	// private static final long serialVersionUID = 4281602726571026683L;

	@EJB
	DaoManager dao;

	@EJB
	XmlManager xml;

	public UpdateManager() {

	}

	public void init() {
		// dao.getClass();
		// xml.getClass();
		// try {
		// Naming.rebind("//localhost/UpdateManager", this);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
	}

	public String getLatestCoreVersionNumber() {
		// return null;
		return dao.getLatestCoreVersionNumber();
	}

	public byte[] getLatestCore() {
		// return null;
		return dao.getLatestCore();
	}

	public byte[] getLatestDashboardConfiguration(String driveboxId) {
		List<byte[]> list = dao.getAppConfigFilesFromDriveboxId(driveboxId);
		Date lastDashboardModif = dao.getLastDashboardModification(driveboxId);
		byte[] dashboardConfiguration = null;
		try {
			dashboardConfiguration = xml.createXmlDashboardConfigurationFile(lastDashboardModif, list);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dashboardConfiguration;
		// return null;
	}

	public byte[] getApplication(String applicationId, String driveboxId) {
		TApplication app = dao.getApplicationFromId(applicationId);
		TDriveboxHasApplication dha = dao.getInstalledAppFromIds(applicationId, driveboxId);
		return this.updateJar(app.getDriveboxApplication(), dha.getConfigurationXml(), dha.getParameters());
		// return null;
	}

	public byte[] updateJar(byte[] driveboxApplication, byte[] configurationXml, byte[] appParameters) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			JarOutputStream jos = new JarOutputStream(baos);

			JarEntry applic = new JarEntry("application.jar");
			applic.setTime(System.currentTimeMillis());
			jos.putNextEntry(applic);
			jos.write(driveboxApplication);

			JarEntry confFile = new JarEntry("configuration.xml");
			confFile.setTime(System.currentTimeMillis());
			jos.putNextEntry(confFile);
			jos.write(configurationXml);

			JarEntry parameters = new JarEntry("parameters.zip");
			parameters.setTime(System.currentTimeMillis());
			jos.putNextEntry(parameters);
			jos.write(appParameters);

			jos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
		// return null;
	}

	@Override
	public void updateIpAdress() {
		// TODO Auto-generated method stub
		
	}
}