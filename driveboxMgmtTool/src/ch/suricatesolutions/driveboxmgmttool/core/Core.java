package ch.suricatesolutions.driveboxmgmttool.core;

/**
 * This interface represents the base of the DriveboxMgmtTool
 * @author Maxime Reymond
 */
public interface Core {
	/**
	 * Launch the core of the DriveboxMgmtTool
	 */
	public void launch();
	
	/**
	 * Desinstall ALL the file of this version of the core
	 */
	public void desinstall();
	
	/**
	 * Launch the update routine
	 */
	public void update();
}
