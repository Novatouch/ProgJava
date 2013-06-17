package Reseau;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <b>Cette classe regroupe les objets nécessaires à la création d'une connexion du coté client.</b>
 * <p>
 * Liste des différents attribus :
 * <ul>
 * <li> socketClient </li>
 * <li> out </li>
 * <li> in </li>
 * </ul>
 * </p>
 * <p>
 * 
 * 
 * </p>
 * 
 * @see GestionSocketClient
 * 
 * @author Gautier Philippe
 * @version 1.0
 */

public class GestionSocketClient {

	/**
     * Constructeur GestionSocketClient.
     * <p>
     * Lors de la création d'un objet GestionSocketClient la variable socketClient est initialisée et la connexion 
     * au serveur est effectuée. Les deux variables gérant les flux sont aussi initialisées
     * </p>
     * 
     * @param param1
     *            Presentation paramètre.
     * @param parma2
     *            Le même.
     * 
	 * liste des attributs modifiés par le constructeur
	 * @throws IOException 
	 * @see GestionSocketServeur#socket
	 * 
	 */
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	
	public GestionSocketClient(Socket _socket) throws IOException{
		
		this.socket = _socket;
		this.out = new PrintWriter(_socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (_socket.getInputStream()));
	}
	
	public GestionSocketClient(String _identifiantServeur, Integer _port ) throws UnknownHostException, IOException{
		
		InetAddress ipServeur; 
		ipServeur = InetAddress.getByName(_identifiantServeur);
		
		
		this.socket = new Socket(ipServeur, _port); ;
		this.out = new PrintWriter(this.socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (this.socket.getInputStream()));
		
		//socket.setSoTimeout(5000);
	}
	
	public GestionSocketClient(String _identifiantServeur, Integer _port, Integer _timeout) throws UnknownHostException, IOException{
		
		InetAddress ipServeur; 
		ipServeur = InetAddress.getByName(_identifiantServeur);
		
		
		this.socket = new Socket(ipServeur, _port); ;
		this.out = new PrintWriter(this.socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (this.socket.getInputStream()));
		
		socket.setSoTimeout(_timeout);
	}
	
	public void envoyerMessage(String _chaine){
		
		 out.println(_chaine);
	     out.flush();
	}
	
	public String recevoirMessage() throws IOException{
		
		return in.readLine();
	}
	
	public String recevoirMessageTimeout() throws IOException{
		
		return in.readLine();
	}
	public void fermerSocket() throws IOException{
		
		this.socket.close();
	}
}
