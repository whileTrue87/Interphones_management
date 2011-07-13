package ch.suricatesolutions.driveboxmgmttool.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.AgiAction;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.response.ManagerResponse;

import ch.suricatesolutions.dingdong.updates.DeviceStatus;
import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;

/**
 * Handle all the SIP interactions with Asterisk 
 * @author Maxime Reymond
 *
 */
public class AsteriskManager implements ManagerEventListener {
	private List<SipDevice> lDevice;
	private static AsteriskManager instance;
	ManagerConnectionFactory factory = new ManagerConnectionFactory(
			"localhost", "manager", "dingdong");
	ManagerConnection managerConnection = factory.createManagerConnection();
	
	private AsteriskManager(){
	}
	
	public static AsteriskManager getInstance(){
		if(instance == null){
			instance = new AsteriskManager();
		}
		return instance;
	}

	/**
	 * Get all the peers from Asterisk
	 * @return A List containing all the peers with their status
	 */
	public List<SipDevice> getDeviceStatus() {
		lDevice = new ArrayList<SipDevice>();
		
		ManagerResponse originateResponse;
		SipPeersAction originateAction = new SipPeersAction();
		managerConnection.addEventListener(this);
		try {
			managerConnection.login();
			originateResponse = managerConnection.sendAction(originateAction,
					30000);
			System.err.println(originateResponse.getResponse());
			synchronized (lDevice) {
				lDevice.wait();
			}
			managerConnection.logoff();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return lDevice;
	}

	@Override
	public void onManagerEvent(ManagerEvent event) {
		// System.out.println(event.getClass());
		if (event instanceof PeerEntryEvent) {
			PeerEntryEvent peerEvent = (PeerEntryEvent) event;
			int id = -1;
			try {
				id = Dao.getInstance().getIdFromName(peerEvent.getObjectName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(id== -1)
				return;
			SipDevice sip = new SipDevice(peerEvent.getObjectName(), "",
					DeviceStatus.ON,id);
//			System.out.println("status="+peerEvent.getStatus());
			if (peerEvent.getStatus() == null) {
				sip.setDeviceStatus(DeviceStatus.NOT_REGISTRED);
				InetAddress inet;
				try {
					inet = InetAddress.getByName(peerEvent.getIpAddress());
					boolean b = inet.isReachable(2000);
					if (!b)
						sip.setDeviceStatus(DeviceStatus.UNREACHABLE);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(peerEvent.getStatus().toUpperCase().contains("UNREACHABLE"))
				sip.setDeviceStatus(DeviceStatus.UNREACHABLE);
			lDevice.add(sip);
		} else if (event instanceof PeerlistCompleteEvent) {
			synchronized (lDevice) {
				lDevice.notifyAll();
			}
		}
	}

	/**
	 * Reload the sip parameters of Asterik and send a notify to the given peers
	 * @param peers The peers to be notified
	 */
	public void reloadPeers(String[] peers) {
		AgiAction aa = new AgiAction();
		aa.setActionId("sip reload");
		try {
			managerConnection.login();
			managerConnection.sendAction(aa, 30000);
			for(String peer : peers){
				aa.setActionId("sip notify aastra-check-cfg "+peer);
				managerConnection.sendAction(aa, 30000);
			}
			managerConnection.logoff();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
