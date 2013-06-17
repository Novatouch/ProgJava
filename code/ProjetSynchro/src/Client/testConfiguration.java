package Client;

import java.io.IOException;

public class testConfiguration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ConfigurationClient config;
		
		
		try {
			config = new ConfigurationClient();
			
			System.out.println(config.getServeurAdresse());
			System.out.println(config.getUtilisateur());
			System.out.println(config.getServeurPort().toString());
			System.out.println(config.getMotDePasse().toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur ouverture fichier, aucun fichier config-client.conf  trouvé dans le répertoire "+ System.getProperty("user.dir"));
		} catch (MissedParametersExeption e) {
			
		}

	}
}
