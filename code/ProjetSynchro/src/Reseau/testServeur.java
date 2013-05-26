package Reseau;

import java.io.IOException;

public class testServeur {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		GestionSocketServeur server;
		
		try{
			
		GestionSocketClient client;
		
		
		server = new GestionSocketServeur(6001, 20);
		
		client = new GestionSocketClient(server.accepterClient());
		
		client.envoyerMessage("plop");
		
		client.fermerSocket();
		
		server.arreterServeur();
		}
		catch (IOException e) {
			System.out.println("Erreur démmarage serveur !");
		}
	

	}
}
