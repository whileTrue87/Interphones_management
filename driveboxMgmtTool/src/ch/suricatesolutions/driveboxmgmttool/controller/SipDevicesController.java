package ch.suricatesolutions.driveboxmgmttool.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.suricatesolutions.driveboxmgmttool.ihm.IDashboard;

public class SipDevicesController implements ActionListener{
	
	private IDashboard dashboard;
	
	public SipDevicesController(IDashboard dashboard){
		this.dashboard = dashboard;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dashboard.showSipDevices();
	}

}
