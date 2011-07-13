package ch.suricatesolutions.driveboxmgmttool.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ch.suricatesolutions.dingdong.applications.Application;
import ch.suricatesolutions.dingdong.updates.DeviceStatus;
import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.dao.Dao;
import ch.suricatesolutions.driveboxmgmttool.service.AsteriskManager;

/**
 * Display a table of SIP devices
 * @author Maxime Reymond
 *
 */
public class SipDevices implements Application {
	private JTable jt;
	private JScrollPane js;
	private final int ROW_HEIGHT = 60;
	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean desinstall() {
		return true;
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public String getLastModification() {
		return null;
	}

	@Override
	public String getTitle() {
		return "Appareils SIP";
	}

	@Override
	public boolean launch(final JPanel panel) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setForeground(Color.WHITE);
		buttonPanel.setBackground(Color.BLACK);
		panel.setLayout(new BorderLayout());
		
		reloadTable(panel);
		
		JButton softUpdate = new JButton("soft update");
		softUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				reloadTable(panel);
			}
			
		});
		buttonPanel.add(softUpdate);
		buttonPanel.repaint();
		
		JButton hardUpdate = new JButton("hard update");
		hardUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					AsteriskManager.getInstance().reloadPeers(Dao.getInstance().getAllPeersName());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				reloadTable(panel);
			}
			
		});
		buttonPanel.add(hardUpdate);
		buttonPanel.repaint();
		
		panel.add(buttonPanel, BorderLayout.NORTH);
		panel.repaint();
		return true;
	}
	
	/**
	 * Reload the table of SIP devices and display it on the given Panel
	 * @param panel The panel on which display the information
	 */
	public void reloadTable(JPanel panel){
		List<SipDevice> lds = getSipDevices();
		final String[] header = new String[]{"Name", "Type", "Status"};
		String[][] content = new String[lds.size()][header.length];
		for(int i = 0; i<lds.size(); i++){
			SipDevice device = lds.get(i);
			content[i][0] = device.getName();
			content[i][1] = "";//device.getDeviceType();
			content[i][2] = device.getDeviceStatus().toString();
		}
		if(js != null){
			panel.remove(js);
			panel.repaint();
		}
		createTable(content, header);
		js = new JScrollPane(jt);
		js.setBackground(Color.BLACK);
		js.getViewport().setBackground(Color.BLACK);
		js.setPreferredSize(new Dimension(500, jt.getRowCount()*ROW_HEIGHT+53));
		panel.add(js, BorderLayout.CENTER);
		panel.repaint();
	}
	

	@Override
	public boolean updateParam(byte[] arg0, byte[] arg1) {
		return true;
	}
	
	/**
	 * Create a new table of SIP devices with the given content and header
	 * @param content The content of the table
	 * @param header The header of the table
	 */
	public void createTable(String[][] content, String[] header){
		jt = new JTable(content, header);
		jt.setBackground(new Color(220,220,220));
		jt.setForeground(Color.BLACK);
		jt.getTableHeader().setBackground(new Color (205, 235, 255));
		jt.setRowHeight(ROW_HEIGHT);
		jt.setFont(new Font("Arial", Font.PLAIN, 20));
		jt.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 20));
		jt.getTableHeader().setPreferredSize(new Dimension(jt.getTableHeader().getWidth(), 40));
		jt.setEnabled(false);
	}
	
	/**
	 * Get all the sip devices from asterisk
	 * @return A List containing all the SIP devices
	 */
	public List<SipDevice> getSipDevices(){
		List<SipDevice> lds = AsteriskManager.getInstance().getDeviceStatus();
		if(lds == null)
			lds = new ArrayList<SipDevice>();
		try {
			List<SipDevice> devices = Dao.getInstance().getAllDevices();
			for (SipDevice device : devices) {
				boolean found = false;
				for (SipDevice peer : lds) {
					if (peer.getId() == device.getId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					lds.add(new SipDevice(device.getName(), "",
							DeviceStatus.UNREACHABLE, device.getId()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lds;
	}

}
