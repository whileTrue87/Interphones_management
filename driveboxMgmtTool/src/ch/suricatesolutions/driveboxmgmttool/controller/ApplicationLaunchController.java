package ch.suricatesolutions.driveboxmgmttool.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.suricatesolutions.driveboxmgmttool.ihm.IDashboard;

public class ApplicationLaunchController implements ActionListener {

	private IDashboard dashboard;

	public ApplicationLaunchController(IDashboard dashboard) {
		this.dashboard = dashboard;
	}

	@Override
	/**
	 * Get the action command of the button and launch the
	 * corresponding application
	 */
	public void actionPerformed(ActionEvent e) {
		String aC = e.getActionCommand();
		String[] tok = aC.split("_");
		int i = Integer.parseInt(tok[0]);
		int j = Integer.parseInt(tok[1]);
		dashboard.getApps()[i][j].launch(dashboard.getAppPanel());
		dashboard.setCrtApp(dashboard.getApps()[i][j]);
	}

}
