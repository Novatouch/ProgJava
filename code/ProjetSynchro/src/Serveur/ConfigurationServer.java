package Serveur;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigurationServer {

	private Integer portServeur;
	private String repertoire;
	private ListeClientConnecte liste;
	private Integer nbClientMax;
	private BaseServeur base;
	private String os;

	// constructeur récupérant information dans un fichier de configuration
	public ConfigurationServer() throws IOException, MissedParametersServerExeption  {
			
			//initialisation des variables
			portServeur = 0;
			repertoire = "stockage";
			nbClientMax = 5;
			liste = new ListeClientConnecte();
			String chemin,chemin2;
			os = System.getProperty("os.name");
			
			System.out.println(os);
			
			if(os.contentEquals("Linux")) {
				chemin2 = System.getProperty("user.dir") + "/serverRepertoire.xml";
			} else {
				chemin2 = System.getProperty("user.dir") + "\\serverRepertoire.xml";
			}
			
			if(os.contentEquals("Linux")) {
				chemin = System.getProperty("user.dir") + "/serverUtilisateur.xml";
			} else {
				chemin = System.getProperty("user.dir") + "\\serverUtilisateur.xml";
			}
			
			base = new BaseServeur(chemin,chemin2);
			
			// ouverture du fichier contenant la configuration du client
			String ligne;
			String fichier;
			
			if(os.contentEquals("Linux")) {
				fichier = System.getProperty("user.dir") + "/"+ "config-serveur.conf";
			} else {
				fichier = System.getProperty("user.dir") + "\\"+ "config-serveur.conf";
			}
	       
	        // ouverture du fichier en entrée
	        InputStream ips = new FileInputStream(fichier); 
	        
	        // nouvelle ouverture fichier en lecture
	        InputStreamReader ipsr = new InputStreamReader(ips); 
	        
	        // ouverture du buffer de lecture
	        BufferedReader br = new BufferedReader(ipsr); 
	
	        // on lit ligne par ligne
			while ((ligne=br.readLine())!=null){ 
				
	            String delims = "[:]";
	            String[] tokens = ligne.split(delims);
	  
	    
	            if(tokens[0].contains("repertoire")){
	            	repertoire = tokens[1];
	            	
	            } else if (tokens[0].contains("portServeur")){
	            	
	            	portServeur = Integer.valueOf(tokens[1]);
	            	
				} else if (tokens[0].contains("os")){
	            	
	            	os = tokens[1];
				}
	            
	        }
	        br.close();
	        
	        if( (portServeur == 0) ||(repertoire.contains("not defined"))){
	        	throw new MissedParametersServerExeption();
	        }  
	        
		}
	
		public Integer getServeurPort(){
			return portServeur;
			
		}
		
		public String getRepertoire(){
			return repertoire;
		}
		
		public ListeClientConnecte getListe(){
			return liste;
		}
		
		public Integer getNbClientMax(){
			return nbClientMax;
		}
		
		public BaseServeur getBaseServeur(){
			return base;
		}
		
}
