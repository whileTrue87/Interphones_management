package ch.suricatesolutions.driveboxmgmttool.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import ch.suricatesolutions.dingdong.applications.Application;
import ch.suricatesolutions.driveboxmgmttool.ToolCore;

/**
 * Handle all the configuration files operations
 * @author Maxime Reymond
 *
 */
public class ConfigFileManager {
	
	private static ConfigFileManager instance;
	
	private ConfigFileManager(){
		
	}
	
	public static ConfigFileManager getInstance(){
		if(instance == null)
			instance = new ConfigFileManager();
		return instance;
	}
	
	/**
	 * Get all the application from the dashboard_config.xml file
	 * @return An array containing all the installed applications
	 * @throws Exception
	 */
	public Application[][] getAllApplications() throws Exception {
		File f = new File(ToolCore.dashboard_config_filename);
		SAXBuilder sxb = new SAXBuilder();
		FileInputStream fis = new FileInputStream(f);
		Document doc = sxb.build(fis);
		Element root = doc.getRootElement();

		int width = root.getAttribute("width").getIntValue();
		int height = root.getAttribute("height").getIntValue();
		Application[][] apps = new Application[height][width];

		XPath xpa = XPath.newInstance("/dashboard/applications/application");
		@SuppressWarnings("unchecked")
		List<Element> applications = xpa.selectNodes(root);
		for (Element app : applications) {
			Class<?> appCls = Class.forName(app.getAttributeValue("className"));
			Constructor<?> construct = appCls.getConstructor();
			Application applic = (Application) construct.newInstance();
			int x = app.getAttribute("xPos").getIntValue();
			int y = app.getAttribute("yPos").getIntValue();
			apps[x][y] = applic;
		}
		fis.close();
		return apps;
	}
	
	public boolean getMuteOption() throws JDOMException, IOException{
		boolean mute = false;
		File f = new File(ToolCore.dashboard_config_filename);
		SAXBuilder sxb = new SAXBuilder();
		FileInputStream fis = new FileInputStream(f);
		Document doc = sxb.build(fis);
		Element root = doc.getRootElement();
		mute = Boolean.parseBoolean(root.getAttributeValue("mute"));
		return mute;
	}

	public boolean updateMuteOption(boolean mute) throws JDOMException, IOException {
		File f = new File(ToolCore.dashboard_config_filename);
		SAXBuilder sxb = new SAXBuilder();
		FileInputStream fis = new FileInputStream(f);
		Document doc = sxb.build(fis);
		Element root = doc.getRootElement();
		Attribute muteAttr = root.getAttribute("mute");
		muteAttr.setValue(String.valueOf(mute));
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(doc, new FileOutputStream(f));
		return true;
	}
}