package Reseau;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import javax.lang.model.element.Element;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Attr;
import org.w3c.dom.Text;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Message {
	
	private String sessionid;
	private String number;
	private String name;
	private Integer size;
	private String sha1;
	private Date date;
	private String path;
	private String user;
	private String type;
	private String status;
	private Boolean synchronised;
	
	public Message (){

		sessionid = null;
		number = null;
		name = null;
		size = -1;
		sha1 = null;
		date = null;
		path = null;
		user = null;
		type = null;
		status = null;
		synchronised = false;
	}
	
	public Message(String _chaine) throws MalformedMessage{
		
		sessionid = null;
		number = null;
		name = null;
		size = -1;
		sha1 = null;
		date = null;
		path = null;
		user = null;
		type = null;
		status = null;
		synchronised = false;
		
		
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			
			ByteArrayInputStream stream = new ByteArrayInputStream(_chaine.getBytes());
			
			try {
				 
				org.w3c.dom.Document doc = dBuilder.parse(stream);
				
				// pour comprendre l'interêt de la normalisation http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
				 
				
				// vérifie la racine du document xml
				if (! doc.getDocumentElement().getNodeName().contains("message")){
					throw new MalformedMessage();
				}
				// récupération type
				try {
					type = doc.getElementsByTagName("type").item(0).getTextContent();
					
				} catch (NullPointerException e) {
					
					throw new MalformedMessage();
				}
				
				// récupération user
				try {
					user = doc.getElementsByTagName("user").item(0).getTextContent();
					
				} catch (NullPointerException e) {
					// ne rien faire
				}
				// récupération status
				try {
					status = doc.getElementsByTagName("status").item(0).getTextContent();
					
				} catch (NullPointerException e) {
					// ne rien faire
				}
				
				try {
					number = doc.getElementsByTagName("number").item(0).getTextContent();
					
				} catch (NullPointerException e) {
					// ne rien faire
				}
				
				try {
					sessionid = doc.getElementsByTagName("sessionid").item(0).getTextContent();
					
				} catch (NullPointerException e){
					// ne rien faire
				}
				
				try {
					String message = doc.getElementsByTagName("synchronisation").item(0).getTextContent();
					
					if(message.contentEquals("active")){
						synchronised = true;
					}
					
				} catch (NullPointerException e) {
					// ne rien faire
				}
				
				try {
					String message = doc.getElementsByTagName("number").item(0).getTextContent();
					
					number = message;
					
				} catch (NullPointerException e) {
					// ne rien faire
				}
				
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				System.out.println("ERREUR: message > requête non conforme xml");
				throw new MalformedMessage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println("ERREUR: message > erreur d'initialisation environement xml");
		}
	}


	
	public String  toString(){
		
		// création d'un nouveau document xml
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			
			// création de la racine du document
			org.w3c.dom.Document doc = docBuilder.newDocument();
			org.w3c.dom.Element rootElement = doc.createElement("message");
			doc.appendChild(rootElement);
	 
			// vérifie si la variable est remplie avant d'ajouter une balise au document
			if(user != null){

				org.w3c.dom.Element elementType = doc.createElement("user");
				elementType.appendChild(doc.createTextNode(user));
				rootElement.appendChild(elementType);
			}
						
			// vérifie si la variable est remplie avant d'ajouter une balise au document
			if(type != null){

				org.w3c.dom.Element elementType = doc.createElement("type");
				elementType.appendChild(doc.createTextNode(type));
				rootElement.appendChild(elementType);
			}
			
			// vérifie si la variable est remplie avant d'ajouter une balise au document
			if(status != null){

				org.w3c.dom.Element elementType = doc.createElement("status");
				elementType.appendChild(doc.createTextNode(status));
				rootElement.appendChild(elementType);
			}
			
			// vérifie si la variable est remplie avant d'ajouter une balise au document
			if(sessionid != null){

				org.w3c.dom.Element elementType = doc.createElement("sessionid");
				elementType.appendChild(doc.createTextNode(sessionid));
				rootElement.appendChild(elementType);
			}
			
			// vérifie si la variable est remplie avant d'ajouter une balise au document
			if(synchronised == true){

				org.w3c.dom.Element elementType = doc.createElement("synchronisation");
				// création du texte à rajouter à la balise
				String message = "active";
				elementType.appendChild(doc.createTextNode(message));
				rootElement.appendChild(elementType);
			}

			if(number != null){

				org.w3c.dom.Element elementType = doc.createElement("number");
				// création du texte à rajouter à la balise
				String message = number;
				elementType.appendChild(doc.createTextNode(message));
				rootElement.appendChild(elementType);
			}
			
			// écrit le document xml dans une chaîne de caractère
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.transform(new DOMSource(doc),
			      new StreamResult(buffer));
			
			return buffer.toString();
			

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getType(){
		return type;
	}

	public String getNumber(){
		return number;
	}
	
	public String getSessionid(){
		return sessionid;
	}
	
	public String getUser(){
		return user;
	}
	
	
	public void authentificationConstructionRequest(String _user) {
		
		type = "auth";
		user = _user;
		status = "request";
		
	}
	
	public Boolean authentificationVerificationRequest() {
		
		if (type.contentEquals("auth") && user != null && status.contentEquals("request")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public Boolean authentificationVerificationChallenge(String _user) {
		
		if (type.contentEquals("auth") && user.contentEquals(_user) && status.contentEquals("challenge") && number != null){
			return true;
		}
		return false; 
	}
	
	public void authentificationConstructionChallenge(String _user, String _number){
		
		type = "auth";
		user = _user;
		status = "challenge";
		number = _number;
	}
	

	
	public void authentificationConstructionChallengeAnswer(String _user, String _number) {
		
		type = "auth";
		user = _user;
		status = "challenge_answer";
		number = _number;
	}
	
	public void authentificationConstructionReponseFinale(String _user, String _sessionid){
		user = _user;
		type = "auth";
		status = "accepted";
		sessionid = _sessionid;
	}
	
	public Boolean authentificationVerificationReponseFinale(String _user) {
		
		if (type.contentEquals("auth") && user.contentEquals(_user) && (status.contentEquals("accepted") || status.contentEquals("denied") || status.contentEquals("already authenticated"))){
			
			if(status.contentEquals("accepted"))
			{
				if(sessionid != null)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else 
			{
				return true;
			}
		}
		return false; 
	}

	public void authentificationConstructionKeepaliveEcho(String _user, String _sessionid, Boolean _synchronised){
		user = _user;
		type = "echo";
		status = "request";
		sessionid = _sessionid;
		synchronised  = _synchronised;
	}
	
	
	public Boolean authentificationVerificationKeepaliveEcho(String _user, String _sessionid) {
		
		if (user.contentEquals(_user) && sessionid.contentEquals(_sessionid) && type.contentEquals("echo") && status.contentEquals("request"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean authentificationVerificationKeepaliveSynchro() {
		
		return synchronised;
	}

	
	public void authentificationConstructionKeepaliveAnswer(String _user, String _sessionid) {
		
		type = "echo";
		user = _user;
		status = "answer";
		sessionid = _sessionid;
	}

	public Boolean authentificationVerificationKeepaliveAnswer(String _user, String _sessionid){
		
		if (user.contentEquals(_user) && sessionid.contentEquals(_sessionid) && type.contentEquals("echo") && status.contentEquals("answer"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
