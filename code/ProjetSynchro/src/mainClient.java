import java.io.IOException;

import Client.AuthentificationClient;
import Client.ConfigurationClient;
import Client.MissedParametersExeption;
import Client.Synchronisation;


public class mainClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		ConfigurationClient config;
		
		
		try {
			// lecture de la configuration dans un fichier
			config = new ConfigurationClient();
			
			// lancement du thread d'authentification
			Thread t = new Thread(new AuthentificationClient(config));
			t.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//lancement du thread de synchronisation
			Thread tSync = new Thread(new Synchronisation(config));
			tSync.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("INFORMATION : client > Erreur ouverture fichier, aucun fichier config-client.conf  trouvé dans le répertoire "+ System.getProperty("user.dir"));
		} catch (MissedParametersExeption e) {
			
		}
	}
}
