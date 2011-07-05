package ch.suricatesolutions.driveboxmgmttool.app;

import javax.swing.JPanel;



public class Text2 implements ch.suricatesolutions.dingdong.applications.Application{
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
		return "Text2";
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
