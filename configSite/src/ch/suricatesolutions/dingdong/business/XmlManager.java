package ch.suricatesolutions.dingdong.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
	public boolean isAtPosition(byte[] xml, int x, int y) throws JDOMException, IOException {
		if(xml == null)
			return false;
		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		doc = sxb.build(new ByteArrayInputStream(xml));
		Element root = doc.getRootElement();
		XPath xpa = XPath.newInstance("/application/x_position");
		Element eXPos = (Element) xpa.selectSingleNode(root);
		if(eXPos == null)
			return false;
		boolean present = Integer.parseInt(eXPos.getText())==x;
		xpa = XPath.newInstance("/application/y_position");
		Element eYPos = (Element) xpa.selectSingleNode(root);
		if(eYPos == null)
			return false;
		present &= Integer.parseInt(eYPos.getText())==y;
		return present;
	}

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

	public byte[] createXmlDashboardConfigurationFile(Date lastModification, List<byte[]> list) throws JDOMException, IOException {
		Element root = new Element("dashboard");
		Element lastModif = new Element("lastModification");
		Element apps = new Element("Applications");
		root.addContent(lastModif);
		root.addContent(apps);
		
		for(byte[] b : list){
			Element app = new Element("application");
			SAXBuilder sxb = new SAXBuilder();
			Document doc = null;
			doc = sxb.build(new ByteArrayInputStream(b));
			Element appRoot = doc.getRootElement();
			
			app.setAttribute("xPos",appRoot.getChild("x_position").getText());
			app.setAttribute("yPos",appRoot.getChild("y_position").getText());
			app.setAttribute("version",appRoot.getChild("version").getText());
			app.setAttribute("className",appRoot.getChild("id").getText());
			
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
}
