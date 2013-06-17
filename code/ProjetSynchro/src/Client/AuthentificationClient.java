package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Reseau.GestionSocketClient;
import Reseau.MalformedMessage;
import Reseau.Message;
import Serveur.ConfigurationServer;

public class AuthentificationClient implements Runnable {
	
	private ConfigurationClient config;
	private GestionSocketClient socket;
	private Integer continuer;
	private Integer continuer2;
	
	public AuthentificationClient(ConfigurationClient _config){
		
		config = _config;
		socket = null;
		continuer = 1;
		
	}
	
	String resoudreChallenge(String number){
		
		return config.getMotDePasse() + number;
	}
	
	
	public void run() {
	
	
		
		while (continuer == 1){
		
			continuer2 = 1;
			
			// connexion au serveur
			try {
				socket = new GestionSocketClient(config.getServeurAdresse(), config.getServeurPort(), 5000);
				
				System.out.println("INFORMATION : client > connexion serveur réussie");
				
				// création message authentification
				Message reqAuth;
				
				reqAuth = new Message();
				
				reqAuth.authentificationConstructionRequest(config.getUtilisateur());
				
				// envoi message requête authentification
				socket.envoyerMessage(reqAuth.toString());
				System.out.println("INFORMATION : client > envoi demande auth");
				
				try 
				{
					
					System.out.println("INFORMATION : client > Attente reception challenge server");
					// ecoute réponse serveur
					Message repChallenge = new Message(socket.recevoirMessage());
					
					
					// vérification de la reponse
					if (repChallenge.authentificationVerificationChallenge(config.getUtilisateur()) == true)
					{
						System.out.println("INFORMATION : client > challenge reçu");
						
						// résolution du challenge et envoi de la réponse
						Message reqAuthChallengeAnswer = new Message();
						reqAuthChallengeAnswer.authentificationConstructionChallengeAnswer(config.getUtilisateur(), resoudreChallenge(repChallenge.getNumber()));
					
						socket.envoyerMessage(reqAuthChallengeAnswer.toString());
						System.out.println("INFORMATION : client > envoi réponse challenge");
						
						Message repFinal = new Message(socket.recevoirMessage());
						System.out.println("INFORMATION : client > reception validation authentification");
						
						
						if(repFinal.authentificationVerificationReponseFinale(config.getUtilisateur()) == true)
						{
							// récupération de l'identifiant de session et modification de la configuration du client
							config.setSessionid(repFinal.getSessionid());
							config.setEstConnecte(true);
							
							System.out.println("INFORMATION : client > connexion au serveur :" + config.getServeurAdresse() + ":" + config.getServeurPort());
							
							// cycle de reception et de réponse aux messages keepalive
							while (continuer2 == 1)
							{
								
								try
								{
									// attente reception message serveur
									Message keepaliveecho = new Message(socket.recevoirMessage());
									
									if( keepaliveecho.authentificationVerificationKeepaliveEcho(config.getUtilisateur(), config.getSessionid()) == true)
									{
										System.out.println("INFORMATION : client > message keepalive echo recu");
										
										// envoie de la réponse
										Message echoAnswer = new Message();
										
										echoAnswer.authentificationConstructionKeepaliveAnswer(config.getUtilisateur(), config.getSessionid());
										
										socket.envoyerMessage(echoAnswer.toString());
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									else
									{
										System.out.println("ERROR : client > message malformé - keepalive echo :" + keepaliveecho.toString());
										continuer2 = 0;
										continuer = 0;
										config.setEstConnecte(false);
									}
								} catch (IOException e) {
									
									System.out.println("ERROR : client > perte connexion avec le serveur");
									continuer2 = 0;
									continuer = 0;
									config.setEstConnecte(false);
								}
				
							}
						}
						else
						{
							System.out.println("ERROR : client > message réponse finale non conforme :"+ repFinal.toString());
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{
						System.out.println("ERROR : client > message challenge non conforme :" + repChallenge.toString());
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (MalformedMessage e) {
					
					System.out.println("INFORMATION : client > attente requête serveur");
				} catch (IOException e) {
					System.out.println("INFORMATION : client > erreur lors des échanges avec le serveur");
				}
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("INFORMATION : client > connexion serveur échoué");
				
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
