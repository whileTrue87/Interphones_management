package ch.suricatesolutions.driveboxmgmttool.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import ch.suricatesolutions.dingdong.applications.Application;

public class EAlbum implements Application {

	private static String rootDir = "ch.suricatesolutions.driveboxmgmttool.app.EAlbum";

	// décommenter ce ligne pour le déployement et commenter celle d'en dessous
	// private static String mainClassPath =
	// "ch/suricatesolutions/driveboxmgmttool/app";
	private static String mainClassPath = "bin/ch/suricatesolutions/driveboxmgmttool/app";

	private static String configFileName = rootDir + "/application.xml";
	private static String imagesDir = rootDir + "/images";

	private Timer t;

	@Override
	public boolean close() {
		t.stop();
		return true;
	}

	@Override
	public boolean desinstall() {
		desinstallDir(new File(rootDir));
		File mainClass = new File(mainClassPath + "/EAlbum.class");
		mainClass.delete();
		return true;
	}

	private boolean desinstallDir(File dir) {
		boolean ret = true;
		File[] resources = dir.listFiles();
		if (resources == null)
			dir.delete();
		else {
			for (File res : resources) {
				if (res.isDirectory())
					ret &= desinstallDir(res);
				else
					res.delete();
			}
		}
		return true;
	}

	@Override
	public String getIcon() {
		return rootDir + "/icon.png";
	}

	@Override
	public String getLastModification() {
		return "2011-07-11T12:00:00";
	}

	@Override
	public String getTitle() {
		return "E-Album";
	}

	@Override
	public boolean launch(JPanel panel) {
		panel.setBackground(Color.BLACK);
		File imageDir = new File(imagesDir);
		File[] imgs = imageDir.listFiles();
		List<BufferedImage> lbi = new ArrayList<BufferedImage>();
		for (File img : imgs) {
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
			lbi.add(bi);
		}
		panel.setLayout(new BorderLayout());
		panel.add(new MyPanel(lbi), BorderLayout.CENTER);
		return true;
	}

	class MyPanel extends JPanel {
		private static final long serialVersionUID = 1143657105806996371L;

		private List<BufferedImage> lbi;
		private BufferedImage bi;
		private int i = 0;

		public MyPanel(List<BufferedImage> lbi) {
			setBackground(Color.BLACK);
			this.lbi = lbi;
			t = new Timer(2000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(i);
					bi = MyPanel.this.lbi.get((i++) % MyPanel.this.lbi.size());
					MyPanel.this.repaint();
				}

			});
			t.start();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (bi != null) {
				int x = (getWidth()-bi.getWidth())/2;
				int y = (getHeight()-bi.getHeight())/2;
				int width = bi.getWidth();
				int height = bi.getHeight();
				while(width> getWidth()/2 || height>getHeight()/2){
					width/=1.5;
					height/=1.5;
					x = (getWidth()-width)/2;
					y = (getHeight()-height)/2;
				}
				g.drawImage(bi, x, y, width, height, null);
			}
		}
	}

	@Override
	public boolean updateParam(byte[] configFile, byte[] params) {
		FileOutputStream baos = null;
		try {
			baos = new FileOutputStream(new File(configFileName));
			baos.write(configFile);

			String tempDir = rootDir + "/temp";
			File tempDirF = new File(tempDir);
			if (!tempDirF.exists())
				tempDirF.mkdirs();
			String tempZip = tempDir + "/" + System.currentTimeMillis()
					+ ".zip";
			File tempZipF = new File(tempZip);
			FileOutputStream fos = new FileOutputStream(tempZipF);
			fos.write(params);
			fos.close();

			ZipFile zf = new ZipFile(tempZip);
			Enumeration<? extends ZipEntry> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();
				byte[] b = new byte[1000];
				InputStream zis = zf.getInputStream(ze);
				int nread = 0;
				fos = new FileOutputStream(new File(imagesDir + "/"
						+ ze.getName()));
				while ((nread = zis.read(b)) > 0) {
					fos.write(b, 0, nread);
				}
				zis.close();
				fos.flush();
				fos.close();
			}
			tempZipF.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
