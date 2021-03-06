package ch.suricatesolutions.driveboxmgmttool.app;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.suricatesolutions.dingdong.applications.Application;

public class Writer2 implements Application {

	@Override
	public boolean desinstall() {
		return false;
	}

	@Override
	public String getIcon() {
		return "appText.png";
	}

	@Override
	public String getLastModification() {
		return null;
	}

	public String getTitle() {
		return "Writer2";
	}

	@Override
	public boolean launch(final JPanel appPanel) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				JLabel label = new JLabel(Writer2.this.getTitle());
				label.setForeground(Color.WHITE);
				appPanel.add(label);
				appPanel.repaint();
			}
			
		});
		
		System.out.println("Application " + getTitle() + " launched");
		return true;
	}

	@Override
	public boolean updateParam(byte[] arg0, byte[] arg1) {
		return false;
	}

	@Override
	public boolean close() {
		return false;
	}
}
