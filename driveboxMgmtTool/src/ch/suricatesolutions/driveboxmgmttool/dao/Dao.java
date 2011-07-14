package ch.suricatesolutions.driveboxmgmttool.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.suricatesolutions.dingdong.updates.SipDevice;
import ch.suricatesolutions.driveboxmgmttool.update.Updater;

public class Dao implements IDao {

	private static IDao instance;

	private Dao() {

	}

	public static IDao getInstance() {
		if (instance == null) {
			instance = new Dao();
		}
		return instance;
	}

	@Override
	public void addSipDevice(SipDevice device) throws Exception {
		if (device != null && device.getName() != null
				&& device.getPassword() != null) {
			Connection con = null;
			try {
				con = Dao.getConnection();
				// System.err.println(con.toString());
				String addQuery = "insert into sip_devices(name, secret, context, type, host, callerid) values (?,?,?,?,?,?)";
				PreparedStatement addDevice = con.prepareStatement(addQuery);
				addDevice.setString(1, device.getName());
				addDevice.setString(2, device.getPassword());
				addDevice.setString(3, Updater.context);
				addDevice.setString(4, "friend");
				addDevice.setString(5, "dynamic");
				addDevice.setString(6, device.getName());
				addDevice.execute();
			} finally {
				con.close();
			}
		}
	}

	@Override
	public void removeSipDevice(int id) throws Exception {
		Connection con = null;
		try {
			con = Dao.getConnection();
			String addQuery = "delete from sip_devices where id=?";
			PreparedStatement addDevice = con.prepareStatement(addQuery);
			addDevice.setInt(1, id);
			addDevice.execute();
		} finally {
			con.close();
		}
	}

	private static Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/asterisk";
		Connection con = DriverManager.getConnection(url, "root", "root");
		return con;
	}

	@Override
	public String[] getAllPeersName() throws Exception {
		Connection con = null;
		List<String> l = new ArrayList<String>();
		try {
			con = Dao.getConnection();
			String allPeersQuery = "select name from sip_devices";
			PreparedStatement allPeers = con.prepareStatement(allPeersQuery);
			ResultSet rs = allPeers.executeQuery();
			while (rs.next()) {
				l.add(rs.getString("name"));
				// System.out.println(rs.getString("name"));
			}
		} finally {
			con.close();
		}
		return l.toArray(new String[0]);
	}

	@Override
	public int getIdFromName(String name) throws Exception {
		if (name == null)
			return -1;
		Connection con = null;
		int id = -1;
		try {
			con = Dao.getConnection();
			String idQuery = "select id from sip_devices where name=?";
			PreparedStatement peersId = con.prepareStatement(idQuery);
			peersId.setString(1, name);
			ResultSet rs = peersId.executeQuery();
			if (rs.next())
				id = rs.getInt("id");
		} finally {
			con.close();
		}
		return id;
	}

	@Override
	public List<SipDevice> getAllDevices() throws Exception {
		Connection con = null;
		List<SipDevice> l = new ArrayList<SipDevice>();
		try {
			con = Dao.getConnection();
			String devicesQuery = "select id, name from sip_devices";
			PreparedStatement devices = con.prepareStatement(devicesQuery);
			ResultSet rs = devices.executeQuery();
			while (rs.next()) {
				l.add(new SipDevice(rs.getString("name"), "", null, rs
						.getInt("id")));
			}
		} finally {
			con.close();
		}
		return l;
	}

	@Override
	public boolean updateTransfertNumber(String number) throws Exception {
		Connection con = null;
		try {
			con = Dao.getConnection();
			String countRedirect = "select count(*) tot from extensions_table where id=3";
			PreparedStatement count = con.prepareStatement(countRedirect);
			ResultSet rs = count.executeQuery();
			int cnt = 0;
			if(rs.next())
				cnt = rs.getInt("tot");
			if(cnt==0){
				String insertRedirect = "insert into extensions_table values (3,'idefisk_test',1002,1,'Dial','"+number+", 20, Ttr')";
				PreparedStatement insert = con.prepareCall(insertRedirect);
				insert.execute();
				
			}else{
				String updateRedirect = "update extensions_table set appdata='"+number+", 20, Ttr' where id=3";
				PreparedStatement update = con.prepareCall(updateRedirect);
				update.execute();
			}
		} finally {
			con.close();
		}
		return true;
	}
}
