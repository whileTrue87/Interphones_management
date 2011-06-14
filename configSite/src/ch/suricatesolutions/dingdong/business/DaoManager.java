package ch.suricatesolutions.dingdong.business;

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

	@PersistenceContext(unitName = "DingDong")
	private EntityManager em;

	/**
	 * Get all the driveboxes of the user identified by his login
	 * @param login The login of the user
	 * @return A List containing all the driveboxes for the given user
	 */
	public List<TDrivebox> getDriveboxByLogin(String login) {
		em.flush();
		return em.createNamedQuery("TDrivebox.driveboxFromUser", TDrivebox.class).setParameter("login", login).getResultList();
	}

	/**
	 * Get all the applications of the database
	 * @return A List containing all the applications of the database
	 */
	public List<TApplication> getAllApps() {
		em.flush();
		return em.createNamedQuery("TApplication.allApps", TApplication.class).getResultList();
	}

	/**
	 * Get all the applications already installed on the given drivebox
	 * @param pkDrivebox The primary key of the driveboy
	 * @return A List containing all the installed applications of the given drivebox
	 */
	public List<TDriveboxHasApplication> getInstalledAppsFromPkDrivebox(int pkDrivebox) {
		em.flush();
		return em.createNamedQuery("TDriveboxHasApplication.installedAppsFromPkDrivebox", TDriveboxHasApplication.class)
				.setParameter("pk", pkDrivebox).getResultList();
	}

	/**
	 * Update the configuration file of the installed application identified by the 2 primary keys parameters
	 * @param pkApplication The primary key of the application
	 * @param pkDrivebox The primary key of the drivebox
	 * @param configFile The configuration file to update
	 */
	public void updateInstalledApp(int pkApplication, int pkDrivebox, byte[] configFile) {
		em.flush();
		TDriveboxHasApplication t = getInstalledAppFromPks(pkApplication, pkDrivebox);
		if (t == null) {
			// System.out.println("pkApp=" + pkApplication);
			// System.out.println("pkDrivebox=" + pkDrivebox);
			em.createNativeQuery("INSERT INTO t_drivebox_has_application VALUES(?, ?, ?, ?)").setParameter(1, pkDrivebox)
					.setParameter(2, pkApplication).setParameter(3, configFile).setParameter(4, true).executeUpdate();
		} else {
			t.setConfigurationXml(configFile);
			t.setEnabled(true);
			em.persist(t);
		}
		// System.out.printf("Update app:%d of drivebox:%d\n", pkApplication,
		// pkDrivebox);
	}

	/**
	 * Get all the installed applications identified by the 2 primary keys parameters
	 * @param pkApplication The primary key of the application
	 * @param pkDrivebox The primary key of the driveby
	 * @return A TDriveBoxHasApplication identified by the given parameters
	 */
	public TDriveboxHasApplication getInstalledAppFromPks(int pkApplication, int pkDrivebox) {
		// System.out.printf("get installed app:%d of drivebox:%d\n",
		// pkApplication, pkDrivebox);
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
	 * Get the primary key of a drivebox from his id
	 * @param id The id of the drivebox
	 * @return The primary key of the drivebox identified by the given id
	 */
	public int getDriveboxPkFromId(String id) {
		em.flush();
		return em.createNamedQuery("TApplication.pkFromId", Integer.class).setParameter(":id", id).getSingleResult();
	}

	/**
	 * Disable an installed application identified by the 2 primary keys parameters
	 * @param pkApplication The primary key of the application
	 * @param pkDrivebox The primary key of the drivebox
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
	 * Update a drivebox with the given parameters
	 * @param pkDrivebox The primary key of the driveboy
	 * @param name The new name of the drivebox
	 * @param telNum The new transfert phone number of the drivebox
	 * @param mute The new state of the mute mode
	 */
	public void updateDrivebox(int pkDrivebox, String name, String telNum, boolean mute) {
		em.flush();
		TDrivebox d = em.createNamedQuery("TDrivebox.getDriveboxFromPk", TDrivebox.class).setParameter("pkDrivebox", pkDrivebox).getSingleResult();
		d.setName(name);
		d.setMute(mute);
		em.persist(d);
		try {
			TTransfertNumber tn = em.createNamedQuery("TTransfertNumber.getNumberForDrivebox", TTransfertNumber.class)
					.setParameter("pkDrivebox", pkDrivebox).getSingleResult();
			tn.setNumber(telNum);
			em.persist(tn);
		} catch (NoResultException e) {
			em.createNativeQuery("INSERT INTO t_transfert_number VALUES(?, ?, ?)").setParameter(1, 0).setParameter(2, telNum).setParameter(3, pkDrivebox)
					.executeUpdate();
		}
	}
}
