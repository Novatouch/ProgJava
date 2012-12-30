package connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TraiterRequete implements Runnable {
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public TraiterRequete(Socket _socket){
		
		this.socket = _socket;
	}
	
	private void initialisationFlux() throws IOException{
		
		this.out = new PrintWriter(this.socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
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
			
			System.out.println("INFORMATION: thread > thread de traitement créer");
			initialisationFlux();
			
			
			
			String requete = recevoirMessage();
			System.out.println("INFORMATION: thread > requête du client recu : " + requete);
			
			// analyse de la requête recu
			if(requete.equals("Get")){
				
				System.out.println("INFORMATION: thread > reponse à la requete Get");
				envoyerMessage("Ok");
			} else if(requete.equals("Auth")){
				
				System.out.println("INFORMATION: thread > client authentifié");
				envoyerMessage("Ok");
				
				Integer continuer = 1;
				
				while (continuer == 1){
					
					envoyerMessage("echo");
					System.out.println("INFORMATION: thread > envoi echo");
					String reponse = recevoirMessage();
					System.out.println("INFORMATION: thread > message recu" + reponse);
					
					if (reponse.equals("ping") != true){
						continuer = 0;
						System.out.println("INFORMATION: thread > reponse érronnée recu");
					}
					
					System.out.println("INFORMATION: thread > repone recu");
					
					Thread.sleep(5 * 1000);
				}
			}else{
				envoyerMessage("malformed request");
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

