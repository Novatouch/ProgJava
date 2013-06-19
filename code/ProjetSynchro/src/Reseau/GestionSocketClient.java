package Reseau;

import java.io.IOException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
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
	private InputStream is;
	private BufferedOutputStream outS;
	//private ObjectOutputStream outStream;
	
	public GestionSocketClient(Socket _socket) throws IOException{
		
		this.socket = _socket;
		this.out = new PrintWriter(_socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (_socket.getInputStream()));
		this.is = socket.getInputStream();
		this.outS = new BufferedOutputStream(socket.getOutputStream());
	}
	
	public GestionSocketClient(String _identifiantServeur, Integer _port ) throws UnknownHostException, IOException{
		
		InetAddress ipServeur; 
		ipServeur = InetAddress.getByName(_identifiantServeur);
		
		
		this.socket = new Socket(ipServeur, _port); ;
		this.out = new PrintWriter(this.socket.getOutputStream());
		this.in = new BufferedReader (new InputStreamReader (this.socket.getInputStream()));
		this.is = socket.getInputStream();
		this.outS = new BufferedOutputStream(socket.getOutputStream());
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

	public void recevoirFichier(String _path){
		
		
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
	    int bufferSize = 0;
	    
	    try {
	        is = socket.getInputStream();

	        bufferSize = socket.getReceiveBufferSize();
	        
	    } catch (IOException ex) {
	        System.out.println("ERROR reseau:recevoirFichier > Can't get socket input stream.");
	    }
	    
	    try {
	        fos = new FileOutputStream(_path);
	        bos = new BufferedOutputStream(fos);

	    } catch (FileNotFoundException ex) {
	        System.out.println("ERROR reseau:recevoirFichier > File not found.");
	    }
	    
	    byte[] bytes = new byte[bufferSize];

	    int count;

	    try {
			while ((count = is.read(bytes)) > 0) {
			    bos.write(bytes, 0, count);
			     
		    bos.flush();
		    bos.close();
		    //is.close();
		    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void envoyerFichier(String _path){
		
		
		File file = new File(_path);
	    // Get the size of the file
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        System.out.println("ERROR reseau:envoyerFichier > Fichier trop gros ");
	    }
	    byte[] bytes = new byte[(int) length];
	    FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    
	

	    int count;

	    try {
			while ((count = bis.read(bytes)) > 0) {
			    outS.write(bytes, 0, count);
			}
			
			outS.flush();
		   // outS.close();
		    fis.close();
		    bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void fermerSocket() throws IOException{
		
		this.socket.close();
	}
}
