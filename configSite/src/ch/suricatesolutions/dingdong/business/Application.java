package ch.suricatesolutions.dingdong.business;

import java.io.File;


public class Application implements ch.suricatesolutions.dingdong.applications.Application{
	private String name;
	private String icone;
	
	public Application(String name, String icone) {
		super();
		this.name = name;
		this.icone = icone;
	}
	
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getIcone() {
		return icone;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	@Override
	public boolean launch() {
		return false;
	}

	@Override
	public boolean desinstall() {
		return false;
	}

	@Override
	public boolean updateParam(byte[] configFile, byte[] params) {
		return false;
	}

	@Override
	public File getIcon() {
		return null;
	}

	@Override
	public String getLastModification() {
		return "2010-02-02T12:00:00";
	}
}
