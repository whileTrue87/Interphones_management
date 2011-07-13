package ch.suricatesolutions.driveboxmgmttool.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.suricatesolutions.driveboxmgmttool.app.SipDevices;
import ch.suricatesolutions.driveboxmgmttool.ihm.IDashboard;

public class SipDevicesController implements ActionListener{
	
	private IDashboard dashboard;
	
	public SipDevicesController(IDashboard dashboard){
		this.dashboard = dashboard;
	}

	@Override
	/**
	 * Launch the "SIP devices application" 
	 */
	public void actionPerformed(ActionEvent e) {
		SipDevices sip = new SipDevices();
		sip.launch(dashboard.getAppPanel());
		dashboard.setCrtApp(sip);
	}

}
