package Client;

public class MissedParametersExeption extends Exception {


	public MissedParametersExeption(){
		System.out.println("format du fichier de configuration attendu :");
		System.out.println("ipServeur:<ip du serveur>");
		System.out.println("portServeur:<port du serveur>");
		System.out.println("utilisateur:<nom utilisateur>");
		System.out.println("motDePasse:<mot de passe>");
		System.out.println("vérifier le fichier de configuration: config-client.conf");
	  } 
}