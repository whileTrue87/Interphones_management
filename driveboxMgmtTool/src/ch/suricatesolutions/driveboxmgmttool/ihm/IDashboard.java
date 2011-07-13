package ch.suricatesolutions.driveboxmgmttool.ihm;

import javax.swing.JPanel;

import ch.suricatesolutions.dingdong.applications.Application;

public interface IDashboard {
	/**
	 * Gets all the application from the dashboard
	 * @return An array containing all the applications
	 */
	public Application[][] getApps();
	
	/**
	 * Set the current application
	 * @param app The current application
	 */
	public void setCrtApp(Application app);
	
	/**
	 * Get the current application
	 * @return The current application
	 */
	public Application getCrtApp();
	
	/**
	 * Display the dashboard on screen
	 */
	public void displayDashboard();
	
	/**
	 * Get the panel on which display the launched applications
	 * @return The JPanel
	 */
	public JPanel getAppPanel();
}
