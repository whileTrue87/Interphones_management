package ch.suricatesolutions.dingdong.applications;

import java.io.File;
import java.util.List;

/**
 * This interface represents the main methods of a drivebox "widget" application
 * @author Maxime Reymond
 *
 */
public interface Application {
	public boolean launch();
	public boolean desinstall();
	public boolean updateParam(byte[] configFile, byte[] params);
	public File getIcon();
	public String getLastModification();
}
