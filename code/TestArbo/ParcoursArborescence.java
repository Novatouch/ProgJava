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
	import java.text.SimpleDateFormat;


	public class ParcoursArborescence {

	    private String cheminInit = "";
	    private Boolean cheminRecursif = false;
	    public int cptFichier = 0;
	    public int cptDossier = 0;


	    public ParcoursArborescence(String chemin, Boolean sousDossier) {
	        super();
	        this.cheminInit = chemin;
	        this.cheminRecursif = sousDossier;
	    }

	    public void list() {
	        this.listDirectory(this.cheminInit);
	    }

	    private void listDirectory(String dir) {
	        File file = new File(dir);
	        File[] files = file.listFiles();
	        long dateModification;
	        long octets;
		    String base = "baseDonneesX";
		    String chaine;
	        if (files != null) {
	            for (int i = 0; i < files.length; i++) {
	                if (files[i].isDirectory() == true) { // Pour un Repertoire
	                	
	                	dateModification = files[i].lastModified(); //Date en long
	                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); // convertir long->date
	                	octets = files[i].length();
	                	
	                	// Affichage des caractéristiques du Dossier dans la console
	                	chaine = "Dossier  : " + files[i].getAbsolutePath() + " " + sdf.format(dateModification) + " " + octets + System.getProperty("line.separator");
	                    System.out.println(chaine);
	                    // Ecriture dans la base
	                    Fichier.ecrire(base, chaine);
	                    this.cptDossier++;
	                    
	                } else { // Pour un Fichier
	                	
	                	dateModification = files[i].lastModified(); //Date en long
	                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); // convertir long->date
	                	octets = files[i].length();
	                	
	                	// Affichage des caractéristiques du Fichier dans la console
	                	chaine = "Fichier  : " + files[i].getName() + " " + sdf.format(dateModification) + " " + octets + System.getProperty("line.separator");
	                    System.out.println(chaine);
	                    // Ecriture dans la base
	                    Fichier.ecrire(base, chaine);
	                    this.cptFichier++;
	                }
	                if (files[i].isDirectory() == true && this.cheminRecursif == true) {
	                    this.listDirectory(files[i].getAbsolutePath());
	                }
	            }
	        }
	    }
	

	}
