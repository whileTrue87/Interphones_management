package ch.suricatesolutions.driveboxmgmttool.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.suricatesolutions.dingdong.updates.SipDevice;

public class Dao implements IDao {
	
	private static IDao instance;
	
	private Dao(){
		
	}
	
	public static IDao getInstance(){
		if(instance== null){
			instance = new Dao();
		}
		return instance;
	}

	@Override
	public void addSipDevice(SipDevice device) throws Exception {
		if (device != null) {
			Connection con = Dao.getConnection();
			System.err.println(con.toString());
			String addQuery = "insert into sip_devices(name, secret, context, type, host, callerid) values (?,?,?,?,?,?)";
			PreparedStatement addDevice = con.prepareStatement(addQuery);
			addDevice.setString(1, device.getName());
			addDevice.setString(2, device.getPassword());
			addDevice.setString(3, "idefisk_test");
			addDevice.setString(4, "friend");
			addDevice.setString(5, "dynamic");
			addDevice.setString(6, device.getName());
			addDevice.execute();
			con.close();
		}
	}

	@Override
	public void removeSipDevice(int id) throws Exception {
		Connection con = Dao.getConnection();
		String addQuery = "delete from sip_devices where id=?";
		PreparedStatement addDevice = con.prepareStatement(addQuery);
		addDevice.setInt(1, id);
		addDevice.execute();
		con.close();
	}

	private static Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		// String url = "jdbc:odbc:asterisk";
		String url = "jdbc:mysql://localhost:3306/asterisk";
		// ((sun.jdbc.odbc.JdbcOdbcDriver)
		// DriverManager.getDriver(url)).setWriter(new
		// java.io.PrintWriter(System.err));
		Connection con = DriverManager.getConnection(url, "root", "root");
		return con;
	}

	@Override
	public String[] getAllPeersName() throws Exception {
		Connection con = Dao.getConnection();
		String allPeersQuery = "select name from sip_devices";
		PreparedStatement allPeers = con.prepareStatement(allPeersQuery);
		ResultSet rs = allPeers.executeQuery();
		List<String> l = new ArrayList<String>();
		while (rs.next()) {
			l.add(rs.getString("name"));
			// System.out.println(rs.getString("name"));
		}
		con.close();
		return l.toArray(new String[0]);
	}

	@Override
	public int getIdFromName(String name) throws Exception{
		Connection con = Dao.getConnection();
		String idQuery = "select id from sip_devices where name=?";
		PreparedStatement peersId = con.prepareStatement(idQuery);
		peersId.setString(1, name);
		ResultSet rs = peersId.executeQuery();
		int id = -1;
		if (rs.next()) 
			id = rs.getInt("id");
		
		con.close();
		return id;
	}

	@Override
	public List<SipDevice> getAllDevices() throws Exception{
		Connection con = Dao.getConnection();
		String devicesQuery = "select id, name from sip_devices";
		PreparedStatement devices = con.prepareStatement(devicesQuery);
		ResultSet rs = devices.executeQuery();
		List<SipDevice> l = new ArrayList<SipDevice>();
		while (rs.next()) {
			l.add(new SipDevice(rs.getString("name"), "", null, rs.getInt("id")));
			// System.out.println(rs.getString("name"));
		}
		con.close();
		return l;
	}
}
