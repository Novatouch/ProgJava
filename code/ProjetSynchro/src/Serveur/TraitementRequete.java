package Serveur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import Reseau.GestionSocketClient;
import Reseau.MalformedMessage;
import Reseau.Message;
import javax.xml.xpath.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TraitementRequete implements Runnable {
	
	private ConfigurationServer config;
	private GestionSocketClient	socketClient;
	
	public TraitementRequete(ConfigurationServer _config, Socket _socketClient){
		
		config = _config;
		try {
			socketClient = new GestionSocketClient(_socketClient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Integer creationChallenge(){
		
		return 777;
	}
		
	void authentification(Message _reqClient){
		
		// vérification de la requête
		_reqClient.authentificationVerificationRequest();
		
		// récupération du nom de l'utilisateur dans le requête
		String utilisateur = _reqClient.getUser();
		System.out.println("INFORMATION: serveur > Utilisateur demandé : " + utilisateur);
		
		// recherche du client dans la base locale
		String chemin = System.getProperty("user.dir")  + "/" + "serverUtilisateur.xml";
		
		//String expression="/server/user/name[text()="+ utilisateur  +"]";
		String expression="/server/user[name/text()=\""+ utilisateur +"\"]";
		
		XPath xpath = XPathFactory.newInstance().newXPath();

		InputSource inputSource = new InputSource(chemin);
		try {
			NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
			
			if (nodes.getLength() == 1){

				System.out.println("INFORMATION: serveur > 1 utilisateur trouvé dans la base locale");
	
				Node nodeUtilisateur = nodes.item(0);
				
				// String nodeMdp = nodeUtilisateur.getNodeName();
				
				NodeList list = nodeUtilisateur.getChildNodes();
				
				// récupération du nom 
				String name = list.item(1).getTextContent();
				// récupération du password
				String password = list.item(3).getTextContent();
				
				Utilisateur util = new Utilisateur(name, password);
				
				// recherche du client dans la liste
				ListeClientConnecte liste = config.getListe();
				
				// recherche du client dans la liste des clients connectés 
				if(liste.rechercherClient(name) == false){

					System.out.println("INFORMATION: serveur > Aucun client présent dans la liste");
					// création du challenge
					Integer challenge = creationChallenge();
					
					// envoi challenge
					Message envoiChallenge = new Message();
					
					envoiChallenge.authentificationConstructionChallenge(name, Integer.toString(challenge));
					
					socketClient.envoyerMessage(envoiChallenge.toString());
					
					System.out.println("INFORMATION: serveur > Envoi du challenge au client");
					
					// récupération réponse client
					try {
						
						
						Message repCheallenge = new Message(socketClient.recevoirMessage());
						
						
						// vérification de la requête
						
						// récupération de la réponse du client au challenge
						String repChallenge = repCheallenge.getNumber();
						
						
						// vérification de la réponse au challenge
						String schallenge = Integer.toString(challenge);
						
						System.out.println("INFORMATION: serveur > Reception de le réponse au challenge");
						
						if(util.verifierPassword(repChallenge, schallenge) == true){

							System.out.println("INFORMATION: serveur > Challenge recu correct");
							// ajout du client à la liste
							liste.ajouterClient(util);
							System.out.println("INFORMATION: serveur > Ajout du client à la liste");
							
							
							// confirmation de l'authentification et envoi du numéro de session
							Message confirmClient = new Message();
							
							confirmClient.authentificationConstructionReponseFinale(util.getNom(), util.getSessionid());
							
							socketClient.envoyerMessage(confirmClient.toString());
							System.out.println("INFORMATION: serveur > Envoi message de confirmation");
							
							int continuer = 1;
							
							// boucle d'envoi des message keepalive
							// cycle de reception et de réponse aux messages keepalive
							while (continuer  == 1)
							{
								
								try
								{
									try {
										Thread.sleep(1000);
										// attente reception message serveur
										Message keepaliveecho = new Message();
										
										keepaliveecho.authentificationConstructionKeepaliveEcho(util.getNom(), util.getSessionid(),util.getPush());
										
										socketClient.envoyerMessage(keepaliveecho.toString());
										System.out.println("INFORMATION: serveur > Envoi message echo keepalive");
										
										
										Message keepaliveAnswer = new Message(socketClient.recevoirMessage());
										System.out.println("INFORMATION: serveur > Reception de la réponse du client keepalive");
										
										if(keepaliveAnswer.authentificationVerificationKeepaliveAnswer(util.getNom(), util.getSessionid()) == false){
											System.out.println("ERROR: serveur > Reception message keepalive incorect");
											continuer = 0;
											liste.supprimerClient(util.getSessionid());
										}
									} catch (InterruptedException e) {
										// erreur pour arrêter Thread
										
										System.out.println("ERROR: serveur >  Interuption de la connexion avec le client");
									}
									
								} catch (IOException e) {
									
									// supression du client si aucune réponse reçue
									System.out.println("ERROR: serveur >  Interuption de la connexion avec le client, retrait de l'utilisateur de la liste");
									continuer = 0;
									liste.supprimerClient(util.getSessionid());
								}
				
							}
						}
						else
						{
							System.out.println("ERROR: serveur >  Réponse au challenge erronnée" + repCheallenge.toString());
						}
							
					} catch (MalformedMessage | IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("ERROR: serveur > Erreur reception cahllenge client ");
						e1.printStackTrace();
					}
					
					
				}
				else
				{
					System.out.println("INFORMATION: serveur > Utilisateur déjà connecté");
				}
					
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
	
	public void run() {
		
		
		// récupération du message
		try {
			Message reqClient = new Message(socketClient.recevoirMessage());
			
			
		switch(reqClient.getType()) {
	    case "auth":
	    	authentification(reqClient);
	        break;
	    case "carrot":
	        
	        break;
		}	
			
		} catch (MalformedMessage | IOException e) {
			
			System.out.println("Client > attente requête serveur");
		}
		
		// récupération du type de message
		
		// lancement de la bonne fonction selon le type du message
	}
}
