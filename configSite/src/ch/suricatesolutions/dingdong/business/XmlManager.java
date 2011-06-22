package ch.suricatesolutions.dingdong.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import ch.suricatesolutions.dingdong.model.TDriveboxHasApplication;

@EJB
@Stateless
/**
 * Manage all the xml operations
 * @author Maxime Reymond
 */
public class XmlManager {

	/**
	 * Checks if the given (x:y) position is in the givent xml file
	 * @param xml The xml file to check in
	 * @param x The x coordinate of the position
	 * @param y The y coordinate of the position
	 * @return True if (x:y) is in the xml file
	 * @throws JDOMException
	 * @throws IOException
	 */
//	public boolean isAtPosition(byte[] xml, int x, int y) throws JDOMException, IOException {
//		if(xml == null)
//			return false;
//		SAXBuilder sxb = new SAXBuilder();
//		Document doc = null;
//		doc = sxb.build(new ByteArrayInputStream(xml));
//		Element root = doc.getRootElement();
//		XPath xpa = XPath.newInstance("/application/x_position");
//		Element eXPos = (Element) xpa.selectSingleNode(root);
//		if(eXPos == null)
//			return false;
//		boolean present = Integer.parseInt(eXPos.getText())==x;
//		xpa = XPath.newInstance("/application/y_position");
//		Element eYPos = (Element) xpa.selectSingleNode(root);
//		if(eYPos == null)
//			return false;
//		present &= Integer.parseInt(eYPos.getText())==y;
//		return present;
//	}

	/**
	 * Update the given configuration file with the (x:y) coordinates
	 * @param configurationFile The configuration file to update
	 * @param xPos The x coordinate
	 * @param yPos The y coordinate
	 * @return The updated configuration file
	 * @throws JDOMException
	 * @throws IOException
	 */
	public byte[] updateConfigurationFile(byte[] configurationFile,
			int xPos, int yPos) throws JDOMException, IOException {
		if(configurationFile== null)
			return null;
		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		doc = sxb.build(new ByteArrayInputStream(configurationFile));
		Element root = doc.getRootElement();
		XPath xpa = XPath.newInstance("/application/x_position");
		Element eXPos = (Element) xpa.selectSingleNode(root);
		if(eXPos != null)
			eXPos.setText(String.valueOf(xPos));
		xpa = XPath.newInstance("/application/y_position");
		Element eYPos = (Element) xpa.selectSingleNode(root);
		if(eYPos != null)
			eYPos.setText(String.valueOf(yPos));
		XMLOutputter xmlOut = new XMLOutputter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		xmlOut.output(doc, out);
		out.close();
		return out.toByteArray();
	}

	/**
	 * Creates the dashboard configuration file from a list of installed applications configuration files
	 * @param lastModification Last time the dashboard was edited
	 * @param list The List containing all the applications configuration files
	 * @return The generated xml configuration file
	 * @throws JDOMException
	 * @throws IOException
	 */
	public byte[] createXmlDashboardConfigurationFile(Date lastModification, List<TDriveboxHasApplication> list) throws JDOMException, IOException {
		if(lastModification==null || list==null)
			return null;
		Element root = new Element("dashboard");
		Element lastModif = new Element("lastModification");
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfHour = new SimpleDateFormat("HH:mm:ss");
		lastModif.setText(dfDate.format(lastModification)+"T"+dfHour.format(lastModification));
		Element apps = new Element("applications");
		root.addContent(lastModif);
		root.addContent(apps);
		
		for(TDriveboxHasApplication b : list){
			Element app = new Element("application");
//			SAXBuilder sxb = new SAXBuilder();
//			Document doc = null;
//			doc = sxb.build(new ByteArrayInputStream(b));
//			Element appRoot = doc.getRootElement();
			
			app.setAttribute("xPos",String.valueOf(b.getxPosition()));
			app.setAttribute("yPos",String.valueOf(b.getyPosition()));
			app.setAttribute("version",b.getTApplication().getVersion());
			System.out.println(b.getTApplication().getVersion());
			app.setAttribute("className",b.getTApplication().getId());
			app.setAttribute("lastModification", dfDate.format(b.getLastModification())+"T"+dfHour.format(b.getLastModification()));
			apps.addContent(app);
		}
		
		Document doc = new Document();
		doc.setRootElement(root);

		XMLOutputter xmlOut = new XMLOutputter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		xmlOut.output(doc, out);
		out.close();
		return out.toByteArray();
	}

	public byte[] createXmlAppConfiguration(TDriveboxHasApplication dha) throws JDOMException, IOException {
		byte[] config = dha.getConfigurationXml();
		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		doc = sxb.build(new ByteArrayInputStream(config));
		Element root = doc.getRootElement();
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfHour = new SimpleDateFormat("HH:mm:ss");
		root.getChild("id").setText(dha.getTApplication().getId());
		root.getChild("name").setText(dha.getTApplication().getName());
		root.getChild("version").setText(dha.getTApplication().getVersion());
		root.getChild("x_position").setText(String.valueOf(dha.getxPosition()));
		root.getChild("y_position").setText(String.valueOf(dha.getyPosition()));
		root.getChild("lastModification").setText( dfDate.format(dha.getLastModification())+"T"+dfHour.format(dha.getLastModification()));
		return config;
	}
}
