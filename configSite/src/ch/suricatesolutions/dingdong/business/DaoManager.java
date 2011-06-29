package ch.suricatesolutions.dingdong.business;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ch.suricatesolutions.dingdong.model.TApplication;
import ch.suricatesolutions.dingdong.model.TDrivebox;
import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;
import ch.suricatesolutions.dingdong.model.TTransfertNumber;

@EJB
@Stateless
/**
 * EJB responsible for the database operations
 * @author Maxime Reymond
 */
public class DaoManager {

	public DaoManager() {

	}

	private static int staticCounter = 0;
	private final int nBits = 4;

	@PersistenceContext(unitName = "DingDong")
	private EntityManager em;

	/**
	 * Gets all the driveboxes of the user identified by his login
	 * 
	 * @param login
	 *            The login of the user
	 * @return A List containing all the driveboxes for the given user
	 */
	public List<TDrivebox> getDriveboxByLogin(String login) {
		em.flush();
		return em.createNamedQuery("TDrivebox.driveboxFromUser", TDrivebox.class).setParameter("login", login).getResultList();
	}

	/**
	 * Gets all the applications of the database
	 * 
	 * @return A List containing all the applications of the database
	 */
	public List<TApplication> getAllApps() {
		em.flush();
		return em.createNamedQuery("TApplication.allApps", TApplication.class).getResultList();
	}

	/**
	 * Gets all the applications already installed on the given drivebox
	 * 
	 * @param pkDrivebox
	 *            The primary key of the drivebox
	 * @return A List containing all the installed applications of the given
	 *         drivebox
	 */
	public List<TDriveboxHasApplication> getInstalledAppsFromPkDrivebox(int pkDrivebox) {
		em.flush();
		return em.createNamedQuery("TDriveboxHasApplication.installedAppsFromPkDrivebox", TDriveboxHasApplication.class)
				.setParameter("pk", pkDrivebox).getResultList();
	}

	/**
	 * Updates the configuration file of the installed application identified by
	 * the 2 primary keys parameters
	 * 
	 * @param pkApplication
	 *            The primary key of the application
	 * @param pkDrivebox
	 *            The primary key of the drivebox
	 * @param configFile
	 *            The configuration file to update
	 */
	public void updateInstalledApp(int pkApplication, int pkDrivebox, int x, int y) {
		em.flush();
		TDriveboxHasApplication t = getInstalledAppFromPks(pkApplication, pkDrivebox);
		if (t == null) {
			TApplication ta = getApplicationFromPk(pkApplication);
			em.createNamedQuery("TDriveboxHasApplication.insertNewOne", TDriveboxHasApplication.class)
					.setParameter(1, pkDrivebox).setParameter(2, pkApplication).setParameter(3, x).setParameter(4, y)
					.setParameter(5, true).setParameter(6, ta.getConfigurationSchema()).setParameter(7, new Date())
					.executeUpdate();
		} else {
			t.setxPosition(x);
			t.setyPosition(y);
			t.setLastModification(new Date());
			t.setEnabled(true);
			em.persist(t);
		}
		this.updateLastDashboardModificationTime(pkDrivebox, new Date());
	}

	private void updateLastDashboardModificationTime(int pkDrivebox, Date date) {
		em.flush();
		TDrivebox d = getDriveboxFromPk(pkDrivebox);
		if (d != null) {
			d.setLastDashboardModification(date);
			em.persist(d);
		}
	}

	/**
	 * Gets all the installed applications identified by the 2 primary keys
	 * parameters
	 * 
	 * @param pkApplication
	 *            The primary key of the application
	 * @param pkDrivebox
	 *            The primary key of the driveby
	 * @return A TDriveBoxHasApplication identified by the given parameters
	 */
	public TDriveboxHasApplication getInstalledAppFromPks(int pkApplication, int pkDrivebox) {
		em.flush();
		long count = em.createNamedQuery("TDriveboxHasApplication.countAppFromPks", Long.class)
				.setParameter("pkApplication", pkApplication).setParameter("pkDrivebox", pkDrivebox).getSingleResult();
		if (count > 0)
			return em.createNamedQuery("TDriveboxHasApplication.appFromPks", TDriveboxHasApplication.class)
					.setParameter("pkApplication", pkApplication).setParameter("pkDrivebox", pkDrivebox).getSingleResult();
		else
			return null;
	}

	/**
	 * Gets the primary key of a drivebox from his id
	 * 
	 * @param id
	 *            The id of the drivebox
	 * @return The primary key of the drivebox identified by the given id
	 */
	public int getDriveboxPkFromId(String id) {
		em.flush();
		try {
			int res = em.createNamedQuery("TDrivebox.pkFromId", Integer.class).setParameter("id", id).getSingleResult();
			return res;
		} catch (NoResultException e) {
			System.err.println("Bad driveboxId in getDriveboxPkFromId : " + id);
		}
		return -1;
	}

	/**
	 * Disables an installed application identified by the 2 primary keys
	 * parameters
	 * 
	 * @param pkApplication
	 *            The primary key of the application
	 * @param pkDrivebox
	 *            The primary key of the drivebox
	 */
	public void disableAppFromDrivebox(int pkApplication, int pkDrivebox) {
		em.flush();
		TDriveboxHasApplication dHA = getInstalledAppFromPks(pkApplication, pkDrivebox);
		if (dHA != null) {
			dHA.setEnabled(false);
			em.persist(dHA);
		}
	}

	/**
	 * Updates a drivebox with the given parameters
	 * 
	 * @param pkDrivebox
	 *            The primary key of the driveboy
	 * @param name
	 *            The new name of the drivebox
	 * @param telNum
	 *            The new transfert phone number of the drivebox
	 * @param mute
	 *            The new state of the mute mode
	 */
	public void updateDrivebox(int pkDrivebox, String name, String telNum, boolean mute) {
		em.flush();
		TDrivebox d = em.createNamedQuery("TDrivebox.getDriveboxFromPk", TDrivebox.class).setParameter("pkDrivebox", pkDrivebox)
				.getSingleResult();
		d.setName(name);
		d.setMute(mute);
		em.persist(d);
		try {
			TTransfertNumber tn = em.createNamedQuery("TTransfertNumber.getNumberForDrivebox", TTransfertNumber.class)
					.setParameter("pkDrivebox", pkDrivebox).getSingleResult();
			tn.setNumber(telNum);
			em.persist(tn);
		} catch (NoResultException e) {
			em.createNativeQuery("INSERT INTO t_transfert_number VALUES(?, ?, ?)").setParameter(1, 0).setParameter(2, telNum)
					.setParameter(3, pkDrivebox).executeUpdate();
		}
	}

	/**
	 * Gets the last core version number
	 * 
	 * @return Returns the last version number
	 */
	public String getLatestCoreVersionNumber() {
		em.flush();
		return em.createNamedQuery("TDriveboxCore.LatestCoreVersionNumber", String.class).setMaxResults(1).getSingleResult();
	}

	/**
	 * Gets the last core
	 * 
	 * @return Returns the last core
	 */
	public byte[] getLatestCore() {
		em.flush();
		return em.createNamedQuery("TDriveboxCore.LatestCore", byte[].class).setMaxResults(1).getSingleResult();
	}

	/**
	 * Gets all the configuration files of the enabled applications for the
	 * given drivebox
	 * 
	 * @param driveboxId
	 *            The id of the drivebox
	 * @return Returns a List containing all the configuration files
	 */
	// public List<byte[]> getAppConfigFilesFromDriveboxId(String driveboxId) {
	// int pkDrivebox = this.getDriveboxPkFromId(driveboxId);
	// if (pkDrivebox == -1)
	// return null;
	// List<TDriveboxHasApplication> list =
	// this.getInstalledAppsFromPkDrivebox(pkDrivebox);
	// List<byte[]> configFiles = new ArrayList<byte[]>();
	// for (TDriveboxHasApplication dha : list) {
	// configFiles.add(dha.getConfigurationXml());
	// }
	// return configFiles;
	// }

	/**
	 * Gets an application from its given id
	 * 
	 * @param applicationId
	 *            The id of the application
	 * @return The application
	 */
	public TApplication getApplicationFromId(String applicationId) {
		em.flush();
		TApplication ta = em.createNamedQuery("TApplication.AppFromId", TApplication.class).setParameter("id", applicationId)
				.getSingleResult();
		return ta;
	}

	/**
	 * Gets an application from its given primary key
	 * 
	 * @param pkApplication
	 *            The primary key of the application
	 * @return The application
	 */
	public TApplication getApplicationFromPk(int pkApplication) {
		em.flush();
		TApplication ta = em.createNamedQuery("TApplication.AppFromPk", TApplication.class).setParameter("pk", pkApplication)
				.getSingleResult();
		return ta;
	}

	/**
	 * Gets the application corresponding to the given id's
	 * 
	 * @param applicationId
	 *            The id of the application
	 * @param driveboxId
	 *            The id of the drivebox
	 * @return The TDriveboxHasApplication object
	 */
	public TDriveboxHasApplication getInstalledAppFromIds(String applicationId, String driveboxId) {
		int pkDrivebox = this.getDriveboxPkFromId(driveboxId);
		if (pkDrivebox == -1)
			return null;
		int pkApplication = this.getApplicationPkFromId(applicationId);
		if (pkApplication == -1)
			return null;
		return this.getInstalledAppFromPks(pkApplication, pkDrivebox);
	}

	/**
	 * Gets the primary key of the application identified by the given id
	 * 
	 * @param applicationId
	 *            The id of the application
	 * @return Returns the primary key
	 */
	private int getApplicationPkFromId(String applicationId) {
		em.flush();
		try {
			int res = em.createNamedQuery("TApplication.pkFromId", Integer.class).setParameter("id", applicationId)
					.getSingleResult();
			return res;
		} catch (NoResultException e) {
			System.err.println("Bad application id in getApplicationPkFromId : " + applicationId);
		}
		return -1;
	}

	/**
	 * Gets the last dashboard modification time from the given id
	 * 
	 * @param driveboxId
	 *            The drivebox id
	 * @return Returns the last modification Date
	 */
	public Date getLastDashboardModification(String driveboxId) {
		em.flush();
		try {
			Date d = em.createNamedQuery("TDrivebox.lastDashboardModification", Date.class).setParameter("id", driveboxId)
					.getSingleResult();
			return d;
		} catch (NoResultException e) {
			System.err.println("Bad drivebox id in getLastDashboardModification : " + driveboxId);
		}
		return null;
	}

	public void updateDriveboxIpAdress(String driveboxId, String remoteAddr) {
		em.flush();
		int pkDrivebox = this.getDriveboxPkFromId(driveboxId);
		if (pkDrivebox == -1)
			return;
		try {
			TDrivebox d = em.createNamedQuery("TDrivebox.getDriveboxFromPk", TDrivebox.class)
					.setParameter("pkDrivebox", pkDrivebox).getSingleResult();
			d.setIpAddress(remoteAddr);
			em.persist(d);
		} catch (NoResultException e) {
			System.err.println("Bad drivebox id in updateDriveboxIpAdress : " + driveboxId);
		}
	}

	public void insertNewApplication(String id, String name, String version, byte[] icon, byte[] execJar, byte[] confFile,
			String confPageName, byte[] confPage, String backBeanName, byte[] backBean) {
		em.flush();
		em.createNamedQuery("TApplication.insertNewApplication", TApplication.class).setParameter(1, 0).setParameter(2, confFile)
				.setParameter(3, execJar).setParameter(4, id).setParameter(5, version).setParameter(6, icon)
				.setParameter(7, name).setParameter(8, confPageName).setParameter(9, confPage).setParameter(10, backBeanName)
				.setParameter(11, backBean).executeUpdate();
	}

	public TDrivebox getDriveboxFromPk(int pkDrivebox) {
		em.flush();
		TDrivebox d = null;
		try {
			d = em.createNamedQuery("TDrivebox.getDriveboxFromPk", TDrivebox.class).setParameter("pkDrivebox", pkDrivebox)
					.getSingleResult();
		} catch (NoResultException e) {
			System.err.println("Bad drivebox pk in getDriveboxFromPk : " + pkDrivebox);
			return null;
		}
		return d;
	}

	public boolean deleteApplication(int pkApplication) {
		em.flush();
		TApplication app = getApplicationFromPk(pkApplication);
		if (app == null)
			return false;
		em.remove(app);
		em.flush();
		return true;
	}

	public long getUnique() {
		return (System.nanoTime() << nBits) | (staticCounter++ & 2 ^ nBits - 1);
	}

	public List<TDrivebox> getAllDriveboxes() {
		em.flush();
		return em.createNamedQuery("TDriveboxes.allDriveboxes", TDrivebox.class).getResultList();
	}

	public byte[] getAppParam(int pkDrivebox, int pkApplication) {
		em.flush();
		return em.createNamedQuery("TDriveboxHasApplication.appParam", byte[].class).setParameter("pkDrivebox", pkDrivebox)
				.setParameter("pkApplication", pkApplication).getSingleResult();
	}

	public void updateAppParam(int pkDrivebox, int pkApplication, byte[] newZip) {
		TDriveboxHasApplication dha = getInstalledAppFromPks(pkApplication, pkDrivebox);
		if (dha != null) {
			dha.setParameters(newZip);
			em.persist(dha);
		}
	}

	public int[] getAppPositionFromPk(int pkApplication, int pkDrivebox) {
		em.flush();
		int[] res = new int[2];
		try {
			Object[] resObject = em.createNamedQuery("TDriveboxHasApplication.positionFromPk", Object[].class).setParameter("pkApplication", pkApplication).setParameter("pkDrivebox", pkDrivebox)
					.getSingleResult();
			res[0] = (Integer)resObject[0];
			res[1] = (Integer)resObject[1];
		} catch (NoResultException e) {
			return null;
		}
		return res;
	}

	public void clearParams(int pkDrivebox, int pkApplication) {
		em.flush();
		TDriveboxHasApplication dha = this.getInstalledAppFromPks(pkApplication, pkDrivebox);
		dha.setParameters(null);
		em.persist(dha);
	}
}
