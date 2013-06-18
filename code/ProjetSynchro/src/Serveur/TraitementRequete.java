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
			System.out.println("ERROR: serveur > perte de connexion avec le serveur");
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
		
		// récupération de la base serveur
		BaseServeur base = config.getBaseServeur();
		
		Utilisateur util = base.rechercherClientBaseServeur(utilisateur);
		
		if(util != null)
		{

			// recherche du client dans la liste
			ListeClientConnecte liste = config.getListe();
			
			// recherche du client dans la liste des clients connectés 
			if(liste.rechercherClient(utilisateur) == false)
			{

				System.out.println("INFORMATION: serveur > Aucun client présent dans la liste");
				// création du challenge
				Integer challenge = creationChallenge();
				
				// envoi challenge
				Message envoiChallenge = new Message();
				
				envoiChallenge.authentificationConstructionChallenge(util.getNom(), Integer.toString(challenge));
				
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
									//System.out.println("INFORMATION: serveur > Envoi message echo keepalive");
									
									
									Message keepaliveAnswer = new Message(socketClient.recevoirMessage());
									//System.out.println("INFORMATION: serveur > Reception de la réponse du client keepalive");
									
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
	}
	
	void demandeSynchronisation(Message _reqClient){
		
		
		// vérification de la requête 
		if (_reqClient.synchronisationVerificationRequest() == true)
		{
			
			
			// vérifie que l'utilisateur soit authentifié
			ListeClientConnecte  list = config.getListe();
			
			if(list.rechercherClientSession(_reqClient.getSessionid()) == true)
			{
				System.out.println("INFORMATION: serveur > reception requête de synchronisation de l'utilisateur:" + _reqClient.getUser());
				
				// recupération des informations dans la base serveur
				BaseServeur base = config.getBaseServeur();
				
				// envoi des informations à l'utilisateur
				String extraction = base.extractionBaseImageBaseUtilisateur(_reqClient.getUser());
				
				try {
					Thread.sleep(500);
					
					if (extraction != null){
						
						socketClient.envoyerMessage(extraction);
						System.out.println("INFORMATION: serveur > envoi de l'extraction réalisé");
					}
					else
					{
						System.out.println("ERROR: serveur > erreur lors de l'extraction des données utilisateur de la base");
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
			{
				System.out.println("ERROR serveur:synchro > client non authentifié ");
			}
		}
		else
		{
			System.out.println("ERROR: message synchronisation transmis par le client non conforme" + _reqClient.toString());
		}	
	}
	
	public void run() {
		
		
		// récupération du message
		try {
			Message reqClient = new Message(socketClient.recevoirMessage());
			
			System.out.println("INFORMATION: serveur:traitement requete > message provenant du client connecté recu :" + reqClient.toString() );
			
			switch(reqClient.getType()) {
		    case "auth":
		    	authentification(reqClient);
		        break;
		    case "synchronisation":
		    	System.out.println("INFORMATION: serveur:synchro > demande recue");
		    	demandeSynchronisation(reqClient);
		    	
		        break;
		        
		    default:
		    	System.out.println("ERROR: message transmis par le client non conforme, type inconnu: " + reqClient.toString());
		    	break;
			}	
			
		} catch (MalformedMessage | IOException e) {
			
			System.out.println("ERROR: message transmis par le client non conforme");
		}
	
	}
}
