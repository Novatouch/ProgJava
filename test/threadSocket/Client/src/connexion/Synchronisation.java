package connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Synchronisation implements Runnable {
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public Synchronisation(){
		
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
		System.out.println("INFORMATION: thread Synchronisation > demarrage de la surveillance");
		try {
			
			Integer continuer = 1;
			Integer	i = 0;
			
			while(continuer == 1){
				
				System.out.println("INFORMATION: thread Synchronisation > changement détecté demande du fichier auprès du serveur");
				
				System.out.println("INFORMATION: thread Synchronisation > connexion au serveur");
				creationConnexion();
				initialisationFlux();
				
				System.out.println("INFORMATION: thread Synchronisation > connexion réussie");
				
				System.out.println("INFORMATION: thread Synchronisation > envoi requête get");
				envoyerMessage("Get");
				
				System.out.println("INFORMATION: thread Synchronisation > reception reponse serveur");
				String reponse = recevoirMessage();
				
				System.out.println("INFORMATION: thread Synchronisation > reponse serveur : " + reponse);
				
				if(i > 10){
					
					continuer = 0;
					System.out.println("INFORMATION: thread Synchronisation > Fin de la surveillance");
				}
				
				i++;
				Thread.sleep(5 * 1000);
			}
			
			
			fermerSocket();
			
			
		}catch (UnknownHostException e){
			e.printStackTrace();
		}catch ( IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
