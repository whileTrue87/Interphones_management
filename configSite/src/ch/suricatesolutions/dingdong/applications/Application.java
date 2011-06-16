package ch.suricatesolutions.dingdong.applications;

import java.io.File;
import java.util.List;

public interface Application {
	public boolean launch();
	public boolean desinstall();
	public boolean updateParam(byte[] configFile, List<Object> params);
	public File getIcon();
}
