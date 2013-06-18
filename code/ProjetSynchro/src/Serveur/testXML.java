package Serveur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class testXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String chemin = System.getProperty("user.dir")  + "/" + "serverUtilisateur.xml";
		String utilisateur = "philippe";
		//String expression="/server/user/name[text()="+ utilisateur  +"]";
		String expression="/server/user[name/text()=\""+ utilisateur +"\"]";
		
		XPath xpath = XPathFactory.newInstance().newXPath();

		InputSource inputSource = new InputSource(chemin);
		try {
			NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
			
			if (nodes.getLength() == 1){
				
				System.out.println("1 utilisateur trouvé");
	
				Node nodeUtilisateur = nodes.item(0);
				
				// String nodeMdp = nodeUtilisateur.getNodeName();
				
				NodeList list = nodeUtilisateur.getChildNodes();
				
				// récupération du nom 
				String name = list.item(1).getTextContent();
				// récupération du password
				String password = list.item(3).getTextContent();
				
			}
			else
			{
				System.out.println("0 utilisateur trouvé");
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
