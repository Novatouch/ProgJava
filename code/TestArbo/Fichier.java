import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 */

/**
 * @author centos
 *
 */
public class Fichier {

	public static boolean supprimerFichier(String nomFichier) throws Exception {

        File file = new File(nomFichier);
        
        //Vérifie si le fichier existe
        if (!file.exists()) {
            throw new Exception("Fichier non trouvé !");
        }
        
        //Teste les propriétés et les droits sur le fichier
        if (!file.canWrite()) {
            throw new Exception("Droits insuffisants pour accéder au fichier");
        }

        return file.delete();
    }
	
	public static void lire(String nomFichier) {
	       
        String chaine="";
        String ligne;
        //Mettre le chemin du fichier en string
        String fichier = System.getProperty("user.dir") + "/"+ nomFichier;
       
        try{
            InputStream ips = new FileInputStream(fichier); // ouverture du fichier en entrée
            InputStreamReader ipsr = new InputStreamReader(ips); // nouvelle ouverture fichier en lecture
            BufferedReader br = new BufferedReader(ipsr); // ouverture du buffer de lecture

            while ((ligne=br.readLine())!=null){ // on lit ligne par ligne
                System.out.println(ligne);
                chaine+=ligne+"\n";
            }
            br.close();
        }       
        catch (Exception e){
            System.out.println(e.toString());
        }   
    }

    public static void ecrire(String nomFic, String texte) {
       
        //Mettre le chemin du fichier en string
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;
   
        try {

            FileWriter fw = new FileWriter(adressedufichier, true); // nouvelle ouverture fichier en écriture ; true = append à la fin du fichier
           
            BufferedWriter output = new BufferedWriter(fw); // ouverture du buffer d'écriture
           
            output.write(texte); // écrit le texte en sortie
           
            output.flush(); // envoie le texte dans le fichier
   
            output.close(); // fermeture du fichier

            System.out.println("Ecriture dans le fichier...");
        }
        catch(IOException ioe){ // gestion de l'exception
            System.out.print("Erreur : ");
            ioe.printStackTrace();
            }

    }
    
    public static boolean rechercherDansFichier(String cheminFichier, String texte) {
    	 
    	boolean retour = false;
    	String chaine;
    	try{
	    	// Création du flux bufférisé sur un FileReader, immédiatement suivi par un 
	    	// try/finally, ce qui permet de ne fermer le flux QUE s'il le reader
	    	// est correctement instancié (évite les NullPointerException)
	    	BufferedReader buff = new BufferedReader(new FileReader(cheminFichier));
    	 
	    	try {
	    		String line;
		    	// Lecture du fichier ligne par ligne. Cette boucle se termine
		    	// quand la méthode retourne la valeur null.
	    		while ((line = buff.readLine()) != null) {
	    			//System.out.println(line);
	    			//mettre le nom du Fichier ou du Repertoire trouvé dans le fichier sous forme de string
	    			chaine = line.substring(11,line.lastIndexOf(" ")); 
	    			chaine = chaine.substring(0,chaine.indexOf(" "));
	    			//System.out.println(" chaine : " + chaine);
	    			
	    			if(chaine.equals(texte)) {
	    				System.out.println("Trouvé !");
	    				return(true);
	    			}
	    			
	    		}
	    	} finally {
	    		// dans tous les cas, on ferme nos flux
	    		buff.close();
	    	}
    	} catch (IOException ioe) {
    	// erreur de fermeture des flux
    	System.out.println("Erreur --" + ioe.toString());
    	}
    	return(retour);
    }
    
    public static String lineToDate(String line) {
    	String date = "";
    	String chaine = "";
    	String chaine1 = "";
    	chaine = line.substring(11,line.lastIndexOf(" "));
    	//System.out.println("Etape 1 : " + chaine);
    	chaine1 = chaine.substring(chaine.indexOf(" "),chaine.lastIndexOf(""));
    	//System.out.println("Etape 2 : " + chaine1);
    	date = chaine1.substring(1,chaine1.lastIndexOf(""));
    	System.out.println("Date : " + date);
    	return(date);
    }
    
    public static void comparerFichiers(String cheminFichier1, String cheminFichier2) {
    	
    	String chaine;
    	
    	try{
	    	// Création du flux bufférisé sur un FileReader, immédiatement suivi par un 
	    	// try/finally, ce qui permet de ne fermer le flux QUE s'il le reader
	    	// est correctement instancié (évite les NullPointerException)
	    	BufferedReader buff1 = new BufferedReader(new FileReader(cheminFichier1));
	    	BufferedReader buff2;
    	 
	    	try {
	    		String line;
	    		String line2;
		    	// Lecture du fichier ligne par ligne. Cette boucle se termine
		    	// quand la méthode retourne la valeur null.
	    		while ((line = buff1.readLine()) != null) {
	    			//System.out.println(line);
	    			//mettre le nom du Fichier ou du Repertoire trouvé dans le fichier sous forme de string
	    			chaine = line.substring(11,line.lastIndexOf(" ")); 
	    			chaine = chaine.substring(0,chaine.indexOf(" "));
	    			//System.out.println(" chaine : " + chaine);
	    			
	    			if(rechercherDansFichier(cheminFichier2, chaine)) {
	    				buff2 = new BufferedReader(new FileReader(cheminFichier2));
	    				while ((line2 = buff2.readLine()) != null) {
	    					
	    					if(line2.equals(line)) {
	    						//comparer les dates
	    						System.out.println("Comparaison des dates...");
	    						System.out.println(line);
	    						String date1 = lineToDate(line);
	    						String date2 = lineToDate(line2);
	    						
	    						if(date1.equals(date2)) {
	    							System.out.println("Les dates sont les mêmes");
	    						}
	    						else {
	    							//comparer les dates
	    						}
	    					}
	    					
	    				} buff2.close();
	    			}
	    			else {
	    				System.out.println("Fichier : " + chaine + "non présent dans la base 2 !");
	    			}
	    			
	    		}
	    	} finally {
	    		// dans tous les cas, on ferme nos flux
	    		System.out.println("Fermeture du flux");
	    		buff1.close();
	    		
	    	}
    	} catch (IOException ioe) {
    	// erreur de fermeture des flux
    	System.out.println("Erreur --" + ioe.toString());
    	}
    	
    
    }
    
}
