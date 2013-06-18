package Serveur;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BaseServeur {
	
	private InputSource inputSource1;
	private XPath xpath;
	
	public BaseServeur(String _chemin){
		
		xpath = XPathFactory.newInstance().newXPath();
		
		inputSource1 = new InputSource(_chemin);
		
	}

	public  Utilisateur rechercherClientBaseServeur(String _utilisateur){
		
		System.out.println("INFORMATION: serveur > Utilisateur demandé : " + _utilisateur);
		 
		String expression="/server/user[name/text()=\""+ _utilisateur +"\"]";
			
		System.out.println(expression);

		Utilisateur util = null;
			try {
				
				NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource1, XPathConstants.NODESET);
				
				if (nodes.getLength() == 1)
				{

					System.out.println("INFORMATION: serveur > 1 utilisateur trouvé dans la base locale");
		
					Node nodeUtilisateur = nodes.item(0);
					
					// String nodeMdp = nodeUtilisateur.getNodeName();
					
					NodeList list = nodeUtilisateur.getChildNodes();
					
					// récupération du nom 
					String name = list.item(1).getTextContent();
					// récupération du password
					String password = list.item(3).getTextContent();
					
					util = new Utilisateur(name, password);
					
					return util;
				}
				else
				{
					System.out.println("INFORMATION: serveur > 0 utilisateur trouvé");
					
					util = null;
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return util;
	}
	 
	
	public String extractionBaseImageBaseUtilisateur(String _utilisateur){
		
		
		String expression="/server/user[@name=\""+ _utilisateur +"\"]";
		String resultat = null;
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		try {
			
			// recupération partie utilisateur
			NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource1, XPathConstants.NODESET);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			try {
				docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("server");
				doc.appendChild(rootElement);
				
				
				for (int i = 0; i < nodes.getLength(); i++) {
		            Node node = nodes.item(i);
		            Node copyNode = doc.importNode(node, true);
		            rootElement.appendChild(copyNode);
		        }
				
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					transformer = transFactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringWriter buffer = new StringWriter();
				try {
					transformer.transform(new DOMSource(doc),
					      new StreamResult(buffer));
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				resultat = buffer.toString();
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultat = null;
			}
	 
			// root elements
			
		} catch (XPathExpressionException e) {
			
			e.printStackTrace();
			resultat = null;
		}

		return resultat;
	}
}
