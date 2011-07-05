package ch.suricatesolutions.driveboxmgmttool.app;

import javax.swing.JPanel;

import ch.suricatesolutions.dingdong.applications.Application;


public class Text1 implements Application{
	private String name;
	private String icone;
	
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
		return "Text1";
	}

	@Override
	public boolean launch(JPanel appPanel) {
		System.out.println("Application " + getName() + " launched");
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
	public String getIcon() {
		return "appWriter.png";
	}

	@Override
	public String getLastModification() {
		return "2010-02-02T12:00:00";
	}
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}
}
