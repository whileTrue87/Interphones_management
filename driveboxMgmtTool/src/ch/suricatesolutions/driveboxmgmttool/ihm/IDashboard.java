package ch.suricatesolutions.driveboxmgmttool.ihm;

import javax.swing.JPanel;

import ch.suricatesolutions.dingdong.applications.Application;

public interface IDashboard {
	public Application[][] getApps();
	public void setCrtApp(int i, int j);
	public Application getCrtApp();
	public void displayDashboard();
	public JPanel getAppPanel();
	public void showSipDevices();
}
