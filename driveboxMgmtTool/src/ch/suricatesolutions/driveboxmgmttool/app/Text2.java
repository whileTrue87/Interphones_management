package ch.suricatesolutions.driveboxmgmttool.app;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class Text2 implements ch.suricatesolutions.dingdong.applications.Application{
	private String icone;
	
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getIcone() {
		return icone;
	}

	public String getTitle() {
		return "Text2";
	}

	@Override
	public boolean launch(final JPanel appPanel) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				JLabel label = new JLabel(Text2.this.getTitle());
				label.setForeground(Color.WHITE);
				appPanel.add(label);
				appPanel.repaint();
			}
			
		});
		
		System.out.println("Application " + getTitle() + " launched");
		return true;
	}

	@Override
	public boolean desinstall() {
		return false;
	}

	@Override
	public boolean updateParam(byte[] configFile, byte[] params) {
		return false;
	}

	@Override
	public String getIcon() {
		return "appWriter.png";
	}

	@Override
	public String getLastModification() {
		return "2010-02-02T12:00:00";
	}
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}
}
