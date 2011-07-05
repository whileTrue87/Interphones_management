package ch.suricatesolutions.driveboxmgmttool.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import ch.suricatesolutions.dingdong.applications.Application;
import ch.suricatesolutions.driveboxmgmttool.ToolCore;

public class ConfigFileManager {
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
		List<Element> applications = xpa.selectNodes(root);
		for (Element app : applications) {
			Class<?> appCls = Class.forName(app.getAttributeValue("className"));
			Constructor<?> construct = appCls.getConstructor();
			Application applic = (Application) construct.newInstance();
			int x = app.getAttribute("xPos").getIntValue();
			int y = app.getAttribute("yPos").getIntValue();
			apps[x][y] = applic;
		}
		return apps;
	}
}