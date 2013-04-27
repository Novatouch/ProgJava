/**
 * 
 */

/**
 * @author centos
 *
 */
import java.io.*;
import java.util.*;
import java.io.File;


public class Main {

	/**
	 * @param args
	 */

	
	
	public static void main(String[] args) {
		
		String cheminArbo = "/home/centos/Documents";
		ParcoursArborescence parcoursArborescence = new ParcoursArborescence(cheminArbo, true);
        Long start = System.currentTimeMillis();
        // Mettre à jour la base
        /*
        try {
        	Fichier.supprimerFichier("/home/centos/workspaceJava/Example/baseDonneesX");
        } catch (Exception ex) {
            System.out.println("Erreur: "+ex.getMessage());
        }
        parcoursArborescence.list();
        System.out.println("");
        System.out.println("......................................");
        System.out.println("Résulats de " + cheminArbo + " trouvés en " + (System.currentTimeMillis() - start) + " ms");
        System.out.println(parcoursArborescence.cptDossier + " dossiers");
        System.out.println(parcoursArborescence.cptFichier + " fichiers");
        
        System.out.println("Voici la base de données :");
        Fichier.lire("baseDonnees");
        
        Fichier.rechercherDansFichier("/home/centos/workspaceJava/Example/baseDonnees", "DDC.zargo~");
        */
        Fichier.comparerFichiers("/home/centos/workspaceJava/Example/baseDonnees", "/home/centos/workspaceJava/Example/baseDonneesX");
	}

}
