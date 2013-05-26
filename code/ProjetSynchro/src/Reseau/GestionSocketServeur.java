package Reseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <b>Cette classe regroupe les objets nécessaires à la création d'une connexion serveur.</b>
 * <p>
 * Liste des différents attribus :
 * <ul>
 * <li> socket</li>
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

public class GestionSocketServeur {
	
	/**
     * Cette varaible stockeras le socket créer par le serveur
     */
	
	private ServerSocket socketserver;
	private Socket socketClient;

	/**
     * Constructeur GestionSocketServeur.
     * <p>
     * Lors de la création d'un objet GestionSocketServeur l'attribut socket est initialisé.
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
	public GestionSocketServeur(Integer _port, Integer _nbClientMax) throws IOException{
		
		//initialisation socket
		socketserver = new ServerSocket(_port, _nbClientMax);
		System.out.println("INFORMATION: serveur > lancement du serveur sur le port : "+ socketserver.getLocalPort());
		
	}
	
	/**
     * Permet d'accepter la connexion d'un client.
     * 
     * @return un objet socketclient.
	 * @throws IOException 
     * 
     */
	public Socket accepterClient() throws IOException {
		
		socketClient = new Socket();
		socketClient = socketserver.accept();
		
		return socketClient;
	}


	/**
     * Permet de fermer la connexion du serveur.
	 * @throws IOException 
     * 
     */
	public void arreterServeur() throws IOException {
		System.out.println("INFORMATION: serveur > arrêt du serveur");
		socketserver.close();
	}
	
}
