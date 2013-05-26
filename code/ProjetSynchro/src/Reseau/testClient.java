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
			
			message = client.recevoirMessage();
			
			System.out.println(message);
			
			client.fermerSocket();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("INFORMATION: client > erreur de connexion au serveur !");
		}
	}

}
