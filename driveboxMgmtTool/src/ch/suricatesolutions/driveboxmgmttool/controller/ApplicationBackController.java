package ch.suricatesolutions.driveboxmgmttool.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.suricatesolutions.driveboxmgmttool.ihm.IDashboard;

public class ApplicationBackController implements ActionListener{
	IDashboard dashboard;
	
	public ApplicationBackController(IDashboard dashboard){
		this.dashboard = dashboard;
	}

	@Override
	/**
	 * Close the current application and display the dashboard
	 */
	public void actionPerformed(ActionEvent e) {
		if(dashboard.getCrtApp() != null)
			dashboard.getCrtApp().close();
		dashboard.displayDashboard();
	}

}