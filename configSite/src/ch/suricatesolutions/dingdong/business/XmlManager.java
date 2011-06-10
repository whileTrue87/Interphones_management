package ch.suricatesolutions.dingdong.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

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
public class XmlManager {

	public boolean isAtPosition(byte[] bs, int x, int y) throws JDOMException, IOException {
		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		doc = sxb.build(new ByteArrayInputStream(bs));
		Element root = doc.getRootElement();
		XPath xpa = XPath.newInstance("/application/x_position");
		Element eXPos = (Element) xpa.selectSingleNode(root);
		boolean present = Integer.parseInt(eXPos.getText())==x;
		xpa = XPath.newInstance("/application/y_position");
		Element eYPos = (Element) xpa.selectSingleNode(root);
		present &= Integer.parseInt(eYPos.getText())==y;
		return present;
	}

	public byte[] updatePosition(byte[] old) {
		System.out.println("Update position");
		return null;
	}

	public byte[] UpdateConfigurationFile(byte[] configurationFile,
			int xPos, int yPos) throws JDOMException, IOException {
		SAXBuilder sxb = new SAXBuilder();
		Document doc = null;
		// List<InstitutionXml> institXmlList = null;

		doc = sxb.build(new ByteArrayInputStream(configurationFile));
		Element root = doc.getRootElement();
		XPath xpa = XPath.newInstance("/application/x_position");
		Element eXPos = (Element) xpa.selectSingleNode(root);
//		System.out.println("old XPos="+eXPos.getText());
		eXPos.setText(String.valueOf(xPos));
		xpa = XPath.newInstance("/application/y_position");
		Element eYPos = (Element) xpa.selectSingleNode(root);
//		System.out.println("old YPos="+eYPos.getText());
		eYPos.setText(String.valueOf(yPos));

		XMLOutputter xmlOut = new XMLOutputter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		xmlOut.output(doc, out);
		out.close();
		return out.toByteArray();
	}
}
