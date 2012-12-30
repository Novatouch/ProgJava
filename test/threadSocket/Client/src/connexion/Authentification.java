package connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Authentification implements Runnable {
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public Authentification(){
		
		this.socket = null;
		this.out = null;
		this.in = null;
	}
	
	private void creationConnexion() throws UnknownHostException, IOException{
		this.socket = new Socket(InetAddress.getLocalHost(),5000);	
	}
	
	private void initialisationFlux() throws IOException{
		
		this.out = new PrintWriter(this.socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (this.socket.getInputStream()));
	}

	private void envoyerMessage(String _chaine){
		
		 out.println(_chaine);
	     out.flush();
	}
	
	private String recevoirMessage() throws IOException{
		
		return in.readLine();
	}
	
	private void fermerSocket() throws IOException{
		
		this.socket.close();
	}

	public void run() {
		
		// recupération de la requete du client
		//BufferedReader in;
		
		try {
			
			creationConnexion();
			
			System.out.println("INFORMATION: thread authentification > démarrage");
			
			initialisationFlux();
			
			// envoyer message pour demander l'authentification
			envoyerMessage("Auth");
			System.out.println("INFORMATION: thread authentification > envoi requête au serveur");
			// attente de la réponse
			String reponse = recevoirMessage();
			
			if(reponse.equals("Ok")){
				System.out.println("INFORMATION: thread authentification > authentification réussie");
				
				Integer continuer = 1;
				
				while(continuer ==1){
					
					// attente de message echo puis réponse ping
					reponse = recevoirMessage();
					
					if(reponse.equals("echo")){
						
						System.out.println("INFORMATION: thread authentification > reception requete echo en provenance du serveur");
						envoyerMessage("ping");
					}
					else
					{
						System.out.println("ERROR: thread authentification > reception requete malformée en provenance du serveur");
						continuer = 0;
					}
				}
				
			}else{
				System.out.println("ERROR: thread authentification > echec authentification");
			}
			
			fermerSocket();
			
		}catch (UnknownHostException e){
			e.printStackTrace();
		}catch ( IOException e) {
			e.printStackTrace();
		}
	}
}
