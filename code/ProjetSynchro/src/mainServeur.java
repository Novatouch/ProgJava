import java.io.IOException;
import java.net.Socket;

import Reseau.GestionSocketClient;
import Reseau.GestionSocketServeur;
import Serveur.ConfigurationServer;
import Serveur.MissedParametersServerExeption;
import Serveur.TraitementRequete;

public class mainServeur {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Integer continuer = 1;
		ConfigurationServer config;
		GestionSocketServeur server;
		

		try {
			config = new ConfigurationServer();
			
			
			// lancement du thread
			server = new GestionSocketServeur(config.getServeurPort(), config.getNbClientMax());
			System.out.println("INFORMATION: serveur > lancement du serveur sur le port : "+ config.getServeurPort());
		
			
			
			while(continuer == 1){
				Socket socketclient = server.accepterClient();
				
				// création du thread allant traiter la requête du client
				System.out.println("INFORMATION: serveur > connexion d'un client");
				
				Thread t = new Thread(new TraitementRequete(config, socketclient));
				t.start();
			}
			
		} catch (IOException e){
			e.printStackTrace();
		} catch (MissedParametersServerExeption e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		}

}

