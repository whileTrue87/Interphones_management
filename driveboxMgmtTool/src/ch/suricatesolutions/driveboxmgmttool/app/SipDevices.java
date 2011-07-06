package ch.suricatesolutions.driveboxmgmttool.app;

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
import ch.suricatesolutions.driveboxmgmttool.service.SipDeviceManager;


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
		
		reloadTable(panel);
		
		JButton softUpdate = new JButton("soft update");
		softUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				reloadTable(panel);
			}
			
		});
		panel.add(softUpdate);
		panel.repaint();
		
		JButton hardUpdate = new JButton("hard update");
		hardUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SipDeviceManager.getInstance().reloadPeers(Dao.getInstance().getAllPeersName());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				reloadTable(panel);
			}
			
		});
		panel.add(hardUpdate);
		panel.repaint();
		//bouton refresh soft --> SipDeviceManager.getInstance().getDeviceStatus() ... comme en haut de la méthode
		//bouton refresh hard --> SipDeviceManager.getInstance().reloadPeers
		return true;
	}
	
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
		js.setPreferredSize(new Dimension(500, jt.getRowCount()*ROW_HEIGHT+53));
		panel.add(js);
		panel.repaint();
	}
	

	@Override
	public boolean updateParam(byte[] arg0, byte[] arg1) {
		return true;
	}
	
	public void createTable(String[][] content, String[] header){
		jt = new JTable(content, header);
		jt.setRowHeight(ROW_HEIGHT);
		jt.setFont(new Font("Arial", Font.PLAIN, 20));
		jt.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 20));
		jt.getTableHeader().setPreferredSize(new Dimension(jt.getTableHeader().getWidth(), 40));
		jt.setEnabled(false);
	}
	
	public List<SipDevice> getSipDevices(){
		List<SipDevice> lds = SipDeviceManager.getInstance().getDeviceStatus();
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
