package Reseau;

import java.io.IOException;

public class testClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GestionSocketClient client;
		String message;
		
		try {
			client = new GestionSocketClient("127.0.0.1", 6001);
			System.out.println("Connexion au serveur !");
			client.envoyerFichier(System.getProperty("user.dir")+"/plopDebut.txt");
			
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			client.fermerSocket();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("INFORMATION: client > erreur de connexion au serveur !");
		}
	}

}
