package ch.suricatesolutions.driveboxmgmttool.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import ch.suricatesolutions.dingdong.applications.Application;
import ch.suricatesolutions.driveboxmgmttool.controller.ApplicationBackController;
import ch.suricatesolutions.driveboxmgmttool.controller.ApplicationLaunchController;
import ch.suricatesolutions.driveboxmgmttool.controller.SipDevicesController;
import ch.suricatesolutions.driveboxmgmttool.service.ConfigFileManager;

public class Dashboard extends JFrame implements IDashboard {

	private static final long serialVersionUID = -320770075704949753L;
	private Application[][] apps;
	private Application crtApp;
	private JPanel infoPanel;
	private Timer time;
	private JPanel dashPanel;
	private JButton backButton;
	private JPanel appPanel;
	private JLabel lLeft;

	public Application[][] getApps() {
		return apps;
	}

	private ConfigFileManager file;

	public Dashboard() {
		setSize(1500, 950);
		setTitle("Drivebox Management Tool");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		BorderLayout bl = new BorderLayout();
		getContentPane().setLayout(bl);
		createInfoPanel();
		getContentPane().add(infoPanel, BorderLayout.NORTH);
		try {
			createDashboard(BorderLayout.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

	private void createDashboard(String location) throws Exception {
		file = ConfigFileManager.getInstance();
		apps = file.getAllApplications();
		if (apps == null)
			return;
		dashPanel = new JPanel();
		BorderLayout bl = new BorderLayout();
		dashPanel.setLayout(bl);
		
		JPanel gridPanel = new JPanel();
		GridLayout gl = new GridLayout(apps.length, apps[0].length);
		gridPanel.setLayout(gl);
		gridPanel.setBackground(Color.BLACK);
		gridPanel.setForeground(Color.WHITE);
		ApplicationLaunchController ac = new ApplicationLaunchController(this);
		for (int i = 0; i < apps.length; i++) {
			for (int j = 0; j < apps[i].length; j++) {
				Application app = apps[i][j];
				JPanel appPanel = new JPanel();
				BoxLayout b = new BoxLayout(appPanel, BoxLayout.Y_AXIS);
				appPanel.setLayout(b);
				appPanel.setBackground(Color.BLACK);
				appPanel.setForeground(Color.WHITE);
				
				if (app == null) {
					gridPanel.add(appPanel);
					continue;
				}
				ImageIcon ii = new ImageIcon(app.getIcon());
				JButton jb = new JButton(ii);
				jb.setAlignmentX(Component.CENTER_ALIGNMENT);
				jb.setBackground(Color.BLACK);
				jb.setBorderPainted(false);
				appPanel.add(jb);
				JLabel labApp = new JLabel(app.getTitle());
				labApp.setAlignmentX(Component.CENTER_ALIGNMENT);
				labApp.setForeground(Color.WHITE);
				appPanel.add(labApp);
				jb.setActionCommand(i + "_" + j);
				jb.addActionListener(ac);
				gridPanel.add(appPanel);
			}
		}
		dashPanel.add(gridPanel, BorderLayout.CENTER);
		
		JPanel footerPanel = new JPanel();
		JButton showDeviceButton = new JButton("Show SIP devices");
		showDeviceButton.addActionListener(new SipDevicesController(this));
		footerPanel.add(showDeviceButton);
		dashPanel.add(footerPanel, BorderLayout.SOUTH);
				
		getContentPane().add(dashPanel, location);
	}

	@Override
	public void setCrtApp(Application app) {
		displayBackButton();
		crtApp = app;
		lLeft.setText(crtApp.getTitle());
	}
	
	private void displayBackButton(){
		if(backButton == null){
			backButton = new JButton("Back");
			backButton.setLocation(10, 30);
			backButton.setSize(100, 40);
			backButton.addActionListener(new ApplicationBackController(this));
		}
		infoPanel.add(backButton);
		infoPanel.repaint();
	}

	private JPanel createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLayout(null);
		infoPanel.setPreferredSize(new Dimension(infoPanel.getWidth(), 100));
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		infoPanel.setBackground(Color.BLACK);
		infoPanel.setForeground(Color.WHITE);
		lLeft = new JLabel("Drivebox");
		lLeft.setFont(new Font("Arial", Font.BOLD, 32));
		lLeft.setForeground(Color.WHITE);
		final JLabel lRight = new JLabel();
		lRight.setForeground(Color.WHITE);
		if (time != null)
			time.stop();
		time = new Timer(0, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				lRight.setText(sdf.format(new Date()));
				infoPanel.repaint();
			}

		});
		time.setDelay(1000);
		time.start();

		lLeft.setLocation(200, 0);
		lLeft.setSize(1100, 100);
		lLeft.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lLeft);
		lRight.setLocation(1300, 0);
		lRight.setSize(200, 100);
		infoPanel.add(lRight);
		return infoPanel;
	}

	@Override
	public Application getCrtApp() {
		return crtApp;
	}

	@Override
	public void displayDashboard() {
		getContentPane().remove(appPanel);
		repaint();
		infoPanel.remove(backButton);
		lLeft.setText("Drivebox");
		repaint();
		getContentPane().add(dashPanel, BorderLayout.CENTER);
		repaint();
	}

	@Override
	public JPanel getAppPanel() {
		appPanel = new JPanel();
		appPanel.setBackground(Color.BLACK);
		appPanel.setForeground(Color.WHITE);
		getContentPane().remove(dashPanel);
		repaint();
		getContentPane().add(appPanel, BorderLayout.CENTER);
		repaint();
		return appPanel;
	}
}
