package ch.suricatesolutions.driveboxmgmttool.app;

import javax.swing.JPanel;



public class Video implements ch.suricatesolutions.dingdong.applications.Application{
	private String icone;
	
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getIcone() {
		return icone;
	}
	public String getTitle() {
		return "Video";
	}

	@Override
	public boolean launch(JPanel appPanel) {
		System.out.println("Application " + getTitle() + " launched");
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
		return "appVideo.png";
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
