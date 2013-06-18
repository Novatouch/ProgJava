package Client;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Reseau.GestionSocketClient;
import Reseau.MalformedMessage;
import Reseau.Message;

/**
 * 
 */

/**
 * @author Melvin
 *
 */
public class Synchronisation implements Runnable {
	

	private ConfigurationClient configClient;
	
	public Synchronisation(ConfigurationClient confClient) {
		
		configClient = confClient;
		
	}
	


	/* Recherche dans le fichier XML un objet
	 * @param cheminFichierXML
	 * @param type de l'objet cherché (fichier/dossier)
	 * @param path de l'objet cherché
	 */
	public boolean rechercherDansBaseXML(Document doc, String typeObjRech, String pathObjRech) {
		
		boolean bool = false;
		
		try {
			 
//			File fXmlFile = new File(cheminBase);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("data");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				// Pour chaque element de la base
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					if(eElement.getElementsByTagName("type").item(0).getTextContent().equals(typeObjRech)) {
						//System.out.println("Type equal");
						if(eElement.getElementsByTagName("path").item(0).getTextContent().equals(pathObjRech)) {
							//System.out.println("Path equal");
							bool = true;
						}
					}
		 
				}
			}
			
			
			
	    } catch (Exception e) {
		e.printStackTrace();
	    }
		
		return(bool);
		
	}
	
	/* Recherche dans le fichier XML la date d'un élément
	 * @param cheminFichierXML
	 * @param type de l'élément
	 * @param path de l'élément
	 */
	public String rechercherDateDansBaseXML(Document doc, String typeObjRech, String pathObjRech) {
		
		String date = "";
		
		try {
			 
//			File fXmlFile = new File(cheminBase);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("data");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				// Pour chaque element de la base
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					if(eElement.getElementsByTagName("type").item(0).getTextContent().equals(typeObjRech)) {
						//System.out.println("Type equal");
						if(eElement.getElementsByTagName("path").item(0).getTextContent().equals(pathObjRech)) {
							//System.out.println("Path equal");
							date = eElement.getElementsByTagName("date").item(0).getTextContent();
						}
					}
		 
				}
			}
			
			
			
	    } catch (Exception e) {
		e.printStackTrace();
	    }
		
		return(date);
		
	}
	
/* Transforme la date en secondes
 * @param date à transformer
 */
	public int transformerDateEnSecondes(String date) {
		int dateCount = 0;
		
		// date de la forme : 06/12/2013 23:36:47
		
		String seconds = date.substring(17,date.lastIndexOf(""));
		//System.out.println(seconds);	
		int s = Integer.parseInt(seconds);
		//System.out.println(s);

		String minutes = date.substring(14,date.lastIndexOf(":"));
		//System.out.println(minutes);
		int m = Integer.parseInt(minutes);
		//System.out.println(m);
		
		String heures = date.substring(11,13);
		//System.out.println(heures);
		int h = Integer.parseInt(heures);
		//System.out.println(h);
		
		String jour = date.substring(0,2);
		//System.out.println(jour);
		int j = Integer.parseInt(jour);
		//System.out.println(j);
		
		String mois = date.substring(3,5);
		//System.out.println(mois);
		int mo = Integer.parseInt(mois);
		//System.out.println(mo);
		
		String an = date.substring(6,date.lastIndexOf(" "));
		//System.out.println(an);
		int a = Integer.parseInt(an);
		//System.out.println(a);
		
		dateCount = s + m*60 + h*3600 + j*86400 + mo*86400*30 + a*86400*30*12;
		
		
		return(dateCount);
	}
	
	/* Compare les dates, retourne 0 si egales, 1 si dateClient > dateServeur, 2 si dateServeur > date Client
	 * @param date côté client
	 * @param date côté serveur
	 */
	public int comparerDates(String dateClient, String dateServeur) {
		int retour = 0;
		
		if(transformerDateEnSecondes(dateClient) == transformerDateEnSecondes(dateServeur)) {
			retour = 0;
		}
		
		if(transformerDateEnSecondes(dateClient) > transformerDateEnSecondes(dateServeur)) {
			retour = 1;
		}
		
		if(transformerDateEnSecondes(dateClient) < transformerDateEnSecondes(dateServeur)) {
			retour = 2;
		}
		
		return(retour);
	}
	
	public void run() {
		
		while(true) {
			
			
			try {
				 System.out.println("Sleep Synchro");
		         Thread.sleep(5*60*10);
		     } catch (Exception e) {
		         System.out.println("Got an exception on sleep!");
		     }
	
		
			boolean estDansBase = false;
			String dateServeur = "";
			int retourDate = 3;
			
			try {
				
				GestionSocketClient gestionSocket = new GestionSocketClient(configClient.getServeurAdresse(), configClient.getServeurPort(),5000);
				Message msg = new Message();
				msg.synchronisationConstructionRequest(configClient.getUtilisateur(), configClient.getSessionid());
				
				while(configClient.getEstConnecte() != true)  {
					System.out.println("En Attente de connection");
				}
				
				gestionSocket.envoyerMessage(msg.toString());
				System.out.println("Emission du message toString");
				
				String StringXMLServeur = gestionSocket.recevoirMessage();
				System.out.println("Reception de la baseXML Serveur sous forme de message");
				
				try {
					DocumentBuilder dBuilderServer = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					
					ByteArrayInputStream stream = new ByteArrayInputStream(StringXMLServeur.getBytes());
					
					try {
						
						System.out.println("Transforme le message contenant la base XML en arbre XML");
						org.w3c.dom.Document docServer = dBuilderServer.parse(stream);
	
						
						// pour comprendre l'interÃªt de la normalisation http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
						docServer.getDocumentElement().normalize();
	
		
						System.out.println("Recuperation base client");
						BaseClient baseClient = new BaseClient(configClient.getRepertoire());
						baseClient.recupererBases("basetxtClient", "baseXMLClient", configClient.getUtilisateur());
						
						
						
							
						try {
							 
							File fXmlFile = new File(configClient.getUserDir() + "\\baseXMLClient");
							DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(fXmlFile);
							
							doc.getDocumentElement().normalize();
						 
							NodeList nList = doc.getElementsByTagName("data");
						 
							for (int temp = 0; temp < nList.getLength(); temp++) {
						 
								Node nNode = nList.item(temp);
						 
								//System.out.println("\nCurrent Element :" + nNode.getNodeName());
								
								// Pour chaque élément de la base
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						 
									Element eElement = (Element) nNode;
									
									// On recherche si cet élément est présent dans la baseXMLServeur
									estDansBase = rechercherDansBaseXML(docServer, eElement.getElementsByTagName("type").item(0).getTextContent(), eElement.getElementsByTagName("path").item(0).getTextContent());
									
									if(estDansBase == true) { // si oui on compare les dates
										dateServeur = rechercherDateDansBaseXML(docServer,  eElement.getElementsByTagName("type").item(0).getTextContent(), eElement.getElementsByTagName("path").item(0).getTextContent());
										retourDate = comparerDates(eElement.getElementsByTagName("date").item(0).getTextContent(),dateServeur);
										
										//si dateClient > dateServeur
										if (retourDate == 1) {
											System.out.println("envoyer l'élément au serveur depuis le client : " + eElement.getElementsByTagName("path").item(0).getTextContent());
										}
										
										//si dateServeur > dateClient
										else if(retourDate == 2) {
											System.out.println("envoyer l'élément au client depuis le serveur : "  + eElement.getElementsByTagName("path").item(0).getTextContent());
										}
										
										else if(retourDate == 3) {
											System.out.println("Erreur : comparaison de dates : "  + eElement.getElementsByTagName("path").item(0).getTextContent());
										}
									}
									
									else { // n'est pas dans la base
										System.out.println("supprimer l'élément côté serveur : "  + eElement.getElementsByTagName("path").item(0).getTextContent());
									}
					
									
								}
						 
							}
					
					    } catch (Exception e) {
						e.printStackTrace();
					    }
						
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						System.out.println("ERREUR: message > requÃªte non conforme xml");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					System.out.println("ERREUR: message > erreur d'initialisation environement xml");
				}
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur : client > connexion serveur Ã©chouÃ© pour la synchronisation");
				
				// attente de 5 seconde avant une nouvelle tentative
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}


