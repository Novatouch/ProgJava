package Serveur;

import java.io.IOException;

public class testConfigurationServer {
	


		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub

			ConfigurationServer config;
			
			
			try {
				config = new ConfigurationServer();
				
				
				System.out.println(config.getServeurPort().toString());
				System.out.println(config.getRepertoire().toString());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur ouverture fichier, aucun fichier config-client.conf  trouvé dans le répertoire "+ System.getProperty("user.dir"));
			} catch (MissedParametersServerExeption e) {
				
			}

		}
	


}
