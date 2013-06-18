package Serveur;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BaseServeur {
	
	private InputSource inputSource;
	
	public BaseServeur(String _chemin){
		
		InputSource inputSource = new InputSource(_chemin);
	}

	 public  Utilisateur rechercherClientBaseServeur(String _utilisateur){
		
		System.out.println("INFORMATION: serveur > Utilisateur demandé : " + _utilisateur);
		 
		String expression="/server/user[name/text()=\""+ _utilisateur +"\"]";
			
		XPath xpath = XPathFactory.newInstance().newXPath();

		Utilisateur util = null;
			try {
				
				NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
				
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
	 
	/*
	public String extractionBaseImageBaseUtilisateur(String _utilisateur){
		
	}*/
}
