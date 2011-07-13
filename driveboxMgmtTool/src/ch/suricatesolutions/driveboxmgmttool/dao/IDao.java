package ch.suricatesolutions.driveboxmgmttool.dao;

import java.util.List;

import ch.suricatesolutions.dingdong.updates.SipDevice;

/**
 * Represents the Data Access Object used to interact with the database
 * @author Maxime Reymond
 *
 */
public interface IDao {
	/**
	 * Add the given SIP device in the database
	 * @param device The device to be added
	 * @throws Exception
	 */
	public void addSipDevice(SipDevice device) throws Exception;
	
	/**
	 * Remove the given SIP device from the database
	 * @param id The id of the SIP device
	 * @throws Exception
	 */
	public void removeSipDevice(int id) throws Exception;
	
	/**
	 * Get all the peer names from the database
	 * @return An array containing all the peer names
	 * @throws Exception
	 */
	public String[] getAllPeersName() throws Exception;
	
	/**
	 * Get the id from the peer's name
	 * @param name The name of the peer
	 * @return The id of the given peer
	 * @throws Exception
	 */
	public int getIdFromName(String name) throws Exception;
	
	/**
	 * Get all the SIP devices from the database
	 * @return A List containing all the SIP devices
	 * @throws Exception
	 */
	public List<SipDevice> getAllDevices() throws Exception;
	
	/**
	 * Update the transfert number in the database
	 * @param number The new transfert number
	 * @return True if no problem occurred
	 * @throws Exception
	 */
	public boolean updateTransfertNumber(String number) throws Exception;
}
