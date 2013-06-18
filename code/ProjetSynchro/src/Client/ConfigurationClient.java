package Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;


/**
 * <b>Cette classe regroupe les objets nécessaires à la configuration du client.</b>
 * <p>
 * Liste des différents attribus :
 * <ul>
 * <li> </li>
 * </ul>
 * </p>
 * <p>
 * 
 * 
 * </p>
 * 
 * @see 
 * 
 * @author Gautier Philippe
 * @version 1.0
 */

public class ConfigurationClient {
	
	private String utilisateur;
	private String motDePasse;
	private String sessionid;
	private String repertoire;
	private Boolean estConnecte;
	private String serveurAdresse;
	private Integer portServeur;
	private Boolean faireSynchronisation;
	private Date dateDerniereSynchronisation;
	
	public ConfigurationClient() throws IOException, MissedParametersExeption  {
		
		//initialisation des variables
		utilisateur = "not defined";
		sessionid = null;
		portServeur = 0;
		repertoire = System.getProperty("user.dir");
		serveurAdresse = "not defined";
		motDePasse = "not defined";
		faireSynchronisation = true;
		estConnecte = false;
		dateDerniereSynchronisation = new Date();
	
		// ouverture du fichier contenant la configuration du client
		String ligne;
        String fichier = System.getProperty("user.dir") + "/"+ "config-client.conf";
       
        
        InputStream ips = new FileInputStream(fichier); // ouverture du fichier en entrée
        InputStreamReader ipsr = new InputStreamReader(ips); // nouvelle ouverture fichier en lecture
        BufferedReader br = new BufferedReader(ipsr); // ouverture du buffer de lecture

        
		while ((ligne=br.readLine())!=null){ // on lit ligne par ligne
			
            String delims = "[:]";
            String[] tokens = ligne.split(delims);
  
    
            if(tokens[0].contains("ipServeur")){
            	serveurAdresse = tokens[1];
            	
            } else if (tokens[0].contains("utilisateur")){
            	
            	utilisateur = tokens[1];
            
			} else if (tokens[0].contains("portServeur")){
            	
				portServeur = Integer.valueOf(tokens[1]);
				
            } else if (tokens[0].contains("motDePasse")){
            	
            	motDePasse = tokens[1];
            
        	}
            
        }
        br.close();
        
        if( (portServeur == 0) || (utilisateur.contains("not defined")) || (motDePasse.contains("not defined")) ||(serveurAdresse.contains("not defined"))){
        	throw new MissedParametersExeption();
        }  
        
	}
	
	public String getUtilisateur(){
		return utilisateur;
		
	}
	public String getServeurAdresse(){
		return serveurAdresse;
		
	}
	public Integer getServeurPort(){
		return portServeur;
		
	}
	
	public String getRepertoire(){
		return repertoire;
	}
	
	public String getMotDePasse(){
		return motDePasse;
	}
	
	public String getSessionid(){
		return sessionid;
	}
	
	public synchronized Boolean getEstConnecte(){
		return estConnecte;
	}
	
	public synchronized Boolean getFaireSynchronisation(){
		return faireSynchronisation;
	}
	
	public synchronized void  setFaireSynchronisation(Boolean _action){
		faireSynchronisation = _action;
		
	}
	public synchronized void setEstConnecte(Boolean _valeur){
		estConnecte = _valeur;
	}
	
	public synchronized void setSessionid(String string){
		sessionid = string;
	}
}
	
