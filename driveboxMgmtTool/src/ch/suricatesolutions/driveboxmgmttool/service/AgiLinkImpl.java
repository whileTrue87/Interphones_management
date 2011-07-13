package ch.suricatesolutions.driveboxmgmttool.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import org.jdom.JDOMException;

import javazoom.jl.player.Player;

public class AgiLinkImpl extends UnicastRemoteObject implements AgiLink {

	private static final long serialVersionUID = -6142438185463073789L;

	public AgiLinkImpl() throws RemoteException {
		super();
	}

	@Override
	public void ring() throws RemoteException {
		boolean mute = true;
		try {
			mute = ConfigFileManager.getInstance().getMuteOption();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!mute) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					play();
					JOptionPane.showConfirmDialog(null,
							"Un appel est en cours", "Appel",
							JOptionPane.OK_CANCEL_OPTION);
					close();
				}

			}).start();
		}

	}

	private String filename = "test.mp3";
	private Player player;

	public void close() {
		if (player != null)
			player.close();
	}

	// play the MP3 file to the sound card
	public void play() {
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		} catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

		// run in new thread to play in background
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}.start();

	}

}
