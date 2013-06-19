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
			
		GestionSocketClient socketClient;
		
		
		server = new GestionSocketServeur(6001, 20);
		
		socketClient = new GestionSocketClient(server.accepterClient());
		System.out.println("Attente fichier du client !");
		
		socketClient.recevoirFichier(System.getProperty("user.dir") + "/plop.txt");
		System.out.println("Reception fichier client !");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socketClient.fermerSocket();
		
		server.arreterServeur();
		}
		catch (IOException e) {
			System.out.println("Erreur démmarage serveur !");
		}
	

	}
}
