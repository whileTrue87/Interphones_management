package ch.suricatesolutions.driveboxmgmttool.app;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.suricatesolutions.dingdong.applications.Application;

public class Writer1 implements Application{

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

	@Override
	public boolean launch(final JPanel appPanel) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				appPanel.add(new JLabel("Writer 1"));
				appPanel.repaint();
			}
			
		});
		
		System.out.println("Application " + getTitle() + " launched");
		return true;
	}
	
	public String getTitle(){
		return "Writer1";
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
