package ch.suricatesolutions.driveboxmgmttool.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Installer {

	private static String rootDir = "ch.suricatesolutions.driveboxmgmttool.app.EAlbum";

	// décommenter ce ligne pour le déployement et commenter celle d'en dessous
	// private static String mainClassPath =
	// "ch/suricatesolutions/driveboxmgmttool/app";
	private static String mainClassPath = "bin/ch/suricatesolutions/driveboxmgmttool/app";
	private static String mainClassPathInJar = "ch/suricatesolutions/driveboxmgmttool/app";

	private static String configFileName = rootDir + "/application.xml";
	private static String imagesDir = rootDir + "/images";

	public void install(byte[] configFile, ZipFile params) {
		try {
			File imageDir = new File(imagesDir);
			if (!imageDir.exists())
				imageDir.mkdirs();

			InputStream input = getClass().getResourceAsStream(
					"/" + mainClassPathInJar + "/EAlbum.class");
			FileOutputStream fos = new FileOutputStream(mainClassPath
					+ "/EAlbum.class");
			byte[] b = new byte[1024];
			int nread = 0;
			while ((nread = input.read(b)) > 0) {
				fos.write(b, 0, nread);
			}
			input.close();
			fos.close();
			
			input = getClass().getResourceAsStream(
					"/" + "icon.png");
			fos = new FileOutputStream(rootDir+"/icon.png");
			b = new byte[1024];
			nread = 0;
			while ((nread = input.read(b)) > 0) {
				fos.write(b, 0, nread);
			}
			input.close();
			fos.close();

			fos = new FileOutputStream(configFileName);
			fos.write(configFile);
			fos.close();

			if (params != null)
				copyImages(params);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copyImages(ZipFile zf) throws IOException {
		Enumeration<? extends ZipEntry> entries = zf.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			byte[] b = new byte[1000];
			InputStream zis = zf.getInputStream(ze);
			int nread = 0;
			FileOutputStream fos = new FileOutputStream(new File(imagesDir
					+ "/" + ze.getName()));
			while((nread = zis.read(b))>0){
				fos.write(b,0,nread);
			}
			zis.close();
			fos.flush();
			fos.close();
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(args.length);
		System.out.println(Arrays.toString(args));
		// args = {confFilename [, parameters] }
		File f = new File(rootDir);
		if (!f.exists())
			f.mkdirs();
		f = new File(mainClassPath);
		if (!f.exists())
			f.mkdirs();

		Installer i = new Installer();
		RandomAccessFile raf = new RandomAccessFile(new File(args[0]), "r");
		byte[] confFile = new byte[(int) raf.length()];
		raf.read(confFile);
		raf.close();

		ZipFile params = null;
		if (args.length > 1) {
			params = new ZipFile(args[1]);
		}

		i.install(confFile, params);
		params.close();
	}
}
