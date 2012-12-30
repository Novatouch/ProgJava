package connexion;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Serveur {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Integer continuer = 1;
		
		try {



			// lancement du thread
			ServerSocket socketserver = new ServerSocket(5000, 5);
			System.out.println("INFORMATION: serveur > lancement du serveur sur le port : "+ socketserver.getLocalPort());
			
			while(continuer == 1){
				Socket socketclient = socketserver.accept();
				
				// création du thread allant traiter la requête du client
				System.out.println("INFORMATION: serveur > connexion d'un client");
				
				Thread t = new Thread(new TraiterRequete(socketclient));
				t.start();
			
			}
			socketserver.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
