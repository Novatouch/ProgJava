package Client;
/**
 * 
 */

/**
 * @author Melvin
 *
 */


import java.io.*;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


	public class RecupererBaseEnParcoursArborescence {

	    private String cheminInit = "";
	    private Boolean cheminRecursif = false;
	    public int cptFichier = 0;
	    public int cptDossier = 0;
	    private String nomBase = "";

	    public RecupererBaseEnParcoursArborescence(String chemin, Boolean sousDossier, String nomBase) {
	        super();
	        
	        File file = new File(nomBase);
	        // supprimer le fichier s'il existe
	        if (file.exists()) {
	        
		        try {
					supprimerFichier(nomBase);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	        this.cheminInit = chemin;
	        this.cheminRecursif = sousDossier;
	        this.nomBase = nomBase;
	    }

	    /* effectue le parcours en arborescence sur le chemin du repertoire donné
	     * 
	     */
	    public void list() {
	        this.listDirectory(this.cheminInit);
	    }

	    /* Parcours recursif du repertoire afin d'enregistrer les dossiers et les fichiers ainsi que leurs caractéristiques dans un fichier texte
	     * @param chemin du repertoire parcouru
	     */
	    private void listDirectory(String dir) {
	    	
	        File file = new File(dir);
	        File[] files = file.listFiles();
	        long dateModification;
	        long octets;
		    String base = nomBase;
		    String chaine;
		    
	        if (files != null) {
	        	
	            for (int i = 0; i < files.length; i++) {
	            	
	                if (files[i].isDirectory() == true) { // Pour un Repertoire
	                	
	                	dateModification = files[i].lastModified(); //Date en long
	                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); // convertir long->date
	                	octets = files[i].length();
	                	
	                	//Transformer le chemin absolu en chemin relatif
	                	String s= files[i].getAbsolutePath();
	                	String str[]=s.split("Stockage");
	                	
	                	// Affichage des caractÃ©ristiques du Dossier dans la console
	                	chaine = "Dossier  : " + str[1] + " " + sdf.format(dateModification) + " " + octets + System.getProperty("line.separator");
	                    System.out.println(chaine);
	                    
	                    // Ecriture dans la base
	                    ecrire(base, chaine);
	                    this.cptDossier++;
	                    
	                } else { // Pour un Fichier
	                	
	                	dateModification = files[i].lastModified(); //Date en long
	                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); // convertir long->date
	                	octets = files[i].length();
	                	
	                	//Transformer le chemin absolu en chemin relatif
	                	String s1= files[i].getAbsolutePath();
	                	String str1[]=s1.split("Stockage",3);
	                	System.out.println(str1[1]);
	                	
	                	// Affichage des caractÃ©ristiques du Fichier dans la console
	                	chaine = "Fichier  : " + str1[1] + " " + sdf.format(dateModification) + " " + octets + System.getProperty("line.separator");
	                    System.out.println(chaine);
	                    
	                    // Ecriture dans la base
	                    ecrire(base, chaine);
	                    this.cptFichier++;
	                }
	                if (files[i].isDirectory() == true && this.cheminRecursif == true) {
	                    this.listDirectory(files[i].getAbsolutePath());
	                }
	            }
	        }
	    }
	
	    /* Supprime un fichier
	     * @param nom du fichier à supprimer
	     */
	    public static boolean supprimerFichier(String nomFichier) throws Exception {

	        File file = new File(nomFichier);
	        
	        //VÃ©rifie si le fichier existe
	        if (!file.exists()) {
	            throw new Exception("Fichier non trouvÃ© !");
	        }
	        
	        //Teste les propriÃ©tÃ©s et les droits sur le fichier
	        if (!file.canWrite()) {
	            throw new Exception("Droits insuffisants pour accÃ©der au fichier");
	        }

	        return file.delete();
	    }
		
	    /* Lis un fichier ligne par ligne et l'affiche à l'écran
	     * @param nom du fichier à lire
	     */
		public static void lire(String nomFichier) {
		       
	        String chaine="";
	        String ligne;
	        //Mettre le chemin du fichier en string
	        String fichier = System.getProperty("user.dir") + "/"+ nomFichier;
	       
	        try{
	            InputStream ips = new FileInputStream(fichier); // ouverture du fichier en entrÃ©e
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

		/* Ecris dans un fichier en mode append (à la fin du fichier en le modifiant)
		 * @param nom du fichier dans lequel écrire
		 * @param texte à écrire dans ce fichier
		 */
	    public static void ecrire(String nomFic, String texte) {
	       
	        //Mettre le chemin du fichier en string
	        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;
	   
	        try {

	            FileWriter fw = new FileWriter(adressedufichier, true); // nouvelle ouverture fichier en Ã©criture ; true = append Ã  la fin du fichier
	           
	            BufferedWriter output = new BufferedWriter(fw); // ouverture du buffer d'Ã©criture
	           
	            output.write(texte); // Ã©crit le texte en sortie
	           
	            output.flush(); // envoie le texte dans le fichier
	   
	            output.close(); // fermeture du fichier

	            System.out.println("Ecriture dans le fichier...");
	        }
	        catch(IOException ioe){ // gestion de l'exception
	            System.out.print("Erreur : ");
	            ioe.printStackTrace();
	            }

	    }
	    
	    /* Recherche le nom d'un dossier/fichier dans la base
	     * @param chemin de la base
	     * @param nom du dosssier/fichier recherché
	     */
	    public static boolean rechercherDansBase(String cheminFichier, String nomRecherche) {
	    	 
	    	boolean retour = false;
	    	String chaine;
	    	try{
	    		
		    	BufferedReader buff = new BufferedReader(new FileReader(cheminFichier));
	    	 
		    	try {
		    		String line;
			    	// Lecture du fichier ligne par ligne. Cette boucle se termine
			    	// quand la mÃ©thode retourne la valeur null.
		    		while ((line = buff.readLine()) != null) {
		    			//System.out.println(line);
		    			//mettre le nom du Fichier ou du Repertoire trouvÃ© dans le fichier sous forme de string
		    			chaine = line.substring(11,line.lastIndexOf(" ")); 
		    			chaine = chaine.substring(0,chaine.indexOf(" "));
		    			chaine = chaine.substring(chaine.lastIndexOf("\\")+1,chaine.lastIndexOf(""));
		    			//System.out.println(" chaine : " + chaine);
		    			
		    			if(chaine.equals(nomRecherche)) {
		    				System.out.println("TrouvÃ© !");
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
	    
	    /*Trouve la date dans une chaine de caractère enregistrée dans la base
	     * @param ligne de la base à découper
	     */
	    public static String lineToDate(String line) {
	    	String date = "";
	    	String chaine = "";
	    	String chaine1 = "";
	    	
	    	chaine = line.substring(11,line.lastIndexOf(" "));
	    	//System.out.println("Etape 1 : " + chaine);
	    	
	    	chaine1 = chaine.substring(chaine.indexOf(" "),chaine.lastIndexOf(""));
	    	//System.out.println("Etape 2 : " + chaine1);
	    	
	    	date = chaine1.substring(1,chaine1.lastIndexOf(""));
	    	//System.out.println("Date : " + date);
	    	
	    	return(date);
	    }
	    
	    /*Trouve la taille dans une chaine de caractère enregistrée dans la base
	     * @param ligne de la base à découper
	     */
	    public static String lineToSize(String line) {
	    	String size = "";
	    	String chaine = "";
	    	String chaine1 = "";
	    	String chaine2 = "";
	    	
	    	chaine = line.substring(11,line.lastIndexOf(""));
	    	//System.out.println("Etape 1 : " + chaine);
	    	
	    	chaine1 = chaine.substring(chaine.indexOf(" "),chaine.lastIndexOf(""));
	    	//System.out.println("Etape 2 : " + chaine1);
	    	
	    	chaine2 = chaine1.substring(chaine1.lastIndexOf(" "),chaine1.lastIndexOf(""));
	    	
	    	size = chaine2.substring(1,chaine2.lastIndexOf(""));
	    	//System.out.println("Size : " + size);
	    	
	    	return(size);
	    }
	    
	    /* Transforme la base texte en base XML
	     * @param chemin de la base texte
	     * @param nom de l'utilisateur
	     * @param nom à donner à la base XML créée
	     */
	    public void baseToXML(String cheminBase, String nomUser, String nomBaseXML) {
	    	
	  	try {
	  		 
	  		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	  		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	   
	  		// root elements
	  		Document doc = docBuilder.newDocument();
	  		Element rootElement = doc.createElement("server");
	  		doc.appendChild(rootElement);
	   
	  		// Name elements
	  		Element user = doc.createElement("user");
	  		rootElement.appendChild(user);
	   
	  		// set attribute to Name element
	  		Attr attr = doc.createAttribute("name");
	  		attr.setValue(nomUser);
	  		user.setAttributeNode(attr);
	   
	    	try {

		    	BufferedReader buff1 = new BufferedReader(new FileReader(cheminBase));
	    	 
		    	try {
		    		String line;
			    	// Lecture du fichier ligne par ligne. Cette boucle se termine
			    	// quand la mÃ©thode retourne la valeur null.
		    		while ((line = buff1.readLine()) != null) {
		    			//System.out.println(line);
		    			
		    			//mettre le path du Fichier ou du Repertoire trouvÃ© dans le fichier sous forme de string
		    			String pathFound = line.substring(11,line.lastIndexOf(" ")); 
		    			pathFound = pathFound.substring(0,pathFound.indexOf(" "));
		    			
		    			// Trouver le nom dans le path
		    			String nameFound = pathFound.substring(pathFound.lastIndexOf("\\")+1,pathFound.lastIndexOf(""));
		    			
		    			//mettre la date sous forme de STring
		    			String dateFound = lineToDate(line);
		    			
		    			// savoir si c'est un fichier ou un dossier et le mettre sous forme de String
		    			String typeFound = line.substring(0, 7);
		    			
		    			//connaître la taille et la mettre sous forme de String
		    			String sizeFound = lineToSize(line);
		    			
		    			
		    			// Remplir le fichier XML
		    			
		    			// data element
				  		Element data = doc.createElement("data");
				  		user.appendChild(data);
				  		
				  		// type fichier/dossier element
				  		Element type = doc.createElement("type");
				  		type.appendChild(doc.createTextNode(typeFound));
				  		data.appendChild(type);
				  		
				  		// owner elements
				  		Element owner = doc.createElement("owner");
				  		owner.appendChild(doc.createTextNode(nomUser));
				  		data.appendChild(owner);
				   
				  		// name elements
				  		Element name = doc.createElement("name");
				  		name.appendChild(doc.createTextNode(nameFound));
				  		data.appendChild(name);
				   
				  		// date elements
				  		Element date = doc.createElement("date");
				  		date.appendChild(doc.createTextNode(dateFound));
				  		data.appendChild(date);
				   
				  		// path elements
				  		Element path = doc.createElement("path");
				  		path.appendChild(doc.createTextNode(pathFound));
				  		data.appendChild(path);
				  		
				  		// size elements
				  		Element size = doc.createElement("size");
				  		size.appendChild(doc.createTextNode(sizeFound));
				  		data.appendChild(size);
				  		
//				  		// sha1 elements
//				  		Element sha1 = doc.createElement("sha1");
//				  		sha1.appendChild(doc.createTextNode("xxxxxx"));
//				  		data.appendChild(sha1);
		    		}
		    	} finally {
		    		// dans tous les cas, on ferme nos flux
		    		System.out.println("Fermeture du flux");
		    		buff1.close();
		    	}
		  		
		   
		  		// write the content into xml file
	
		  		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		  		Transformer transformer = transformerFactory.newTransformer();
		  		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		  		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		  		DOMSource source = new DOMSource(doc);
		  		StreamResult result = new StreamResult(new File(nomBaseXML));
		   
		  		// Output to console for testing
		  		// StreamResult result = new StreamResult(System.out);
		   
		  		transformer.transform(source, result);
		   
		  		System.out.println("File saved!");
	  		
	    	} catch (IOException ioe) {
	    	// erreur de fermeture des flux
	    		System.out.println("Erreur --" + ioe.toString());
	    	}
	    	
	  	  } catch (ParserConfigurationException pce) {
	  		  pce.printStackTrace();
	  	  } catch (TransformerException tfe) {
	  		  tfe.printStackTrace();
	  	  }
	  	}

	}