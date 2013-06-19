package Serveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BaseServeur {
	
	private InputSource inputSource1;
	private InputSource inputSourceBaseRepertoire;
	private XPath xpath;
	
	public BaseServeur(String _chemin, String _chemin2){
		
		xpath = XPathFactory.newInstance().newXPath();
		
		inputSource1 = new InputSource(_chemin);
		inputSourceBaseRepertoire = new InputSource(_chemin2);
		
	}

	public  Utilisateur rechercherClientBaseServeur(String _utilisateur){
		
		System.out.println("INFORMATION: serveur > Utilisateur demandé : " + _utilisateur);
		 
		String expression="/server/user[name/text()=\""+ _utilisateur +"\"]";
			
		System.out.println(expression);

		Utilisateur util = null;
			try {
				
				NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource1, XPathConstants.NODESET);
				
				if (nodes.getLength() == 1)
				{

					System.out.println("INFORMATION: serveur > 1 utilisateur trouvé dans la base locale");
		
					Node nodeUtilisateur = nodes.item(0);
					
					// String nodeMdp = nodeUtilisateur.getNodeName();
					
					NodeList list = nodeUtilisateur.getChildNodes();
					
					// récupération du nom 
					String name = list.item(1).getTextContent();
					// récupération du password
					String password = list.item(3).getTextContent();
					
					util = new Utilisateur(name, password);
					
					return util;
				}
				else
				{
					System.out.println("INFORMATION: serveur > 0 utilisateur trouvé");
					
					util = null;
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return util;
	}
	 
	
	public String extractionBaseImageBaseUtilisateur(String _utilisateur){
		
		
		String expression="/server/user[@name=\""+ _utilisateur +"\"]";
		String resultat = null;
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		try {
			
			// recupération partie utilisateur
			NodeList nodes = (NodeList) xpath.evaluate(expression, inputSourceBaseRepertoire, XPathConstants.NODESET);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			try {
				docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("server");
				doc.appendChild(rootElement);
				
				
				for (int i = 0; i < nodes.getLength(); i++) {
		            Node node = nodes.item(i);
		            Node copyNode = doc.importNode(node, true);
		            rootElement.appendChild(copyNode);
		        }
				
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					transformer = transFactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringWriter buffer = new StringWriter();
				try {
					transformer.transform(new DOMSource(doc),
					      new StreamResult(buffer));
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				resultat = buffer.toString();
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultat = null;
			}
	 
			// root elements
			
		} catch (XPathExpressionException e) {
			
			e.printStackTrace();
			resultat = null;
		}

		return resultat;
	}
	
    /* Transforme la base texte en base XML
     * @param chemin de la base texte
     * @param nom de l'utilisateur
     * @param nom � donner � la base XML cr��e
     */
    public void baseToXML(String cheminBase, String nomUser, String nomBaseXML, String osChoisi) {
    	
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
		    	// quand la méthode retourne la valeur null.
	    		while ((line = buff1.readLine()) != null) {
	    			//System.out.println(line);
	    			
	    			//mettre le path du Fichier ou du Repertoire trouvé dans le fichier sous forme de string
	    			String pathFound = line.substring(11,line.lastIndexOf(" ")); 
	    			pathFound = pathFound.substring(0,pathFound.indexOf(" "));
	    			
	    			// Trouver le nom dans le path
	    			String nameFound = "";
	    			if(osChoisi.equals("windows")) {
	    				nameFound = pathFound.substring(pathFound.lastIndexOf("\\")+1,pathFound.lastIndexOf(""));
	    			}
	    			else {
	    				nameFound = pathFound.substring(pathFound.lastIndexOf("/")+1,pathFound.lastIndexOf(""));
	    			}
	    			
	    			//mettre la date sous forme de STring
	    			String dateFound = lineToDate(line);
	    			
	    			// savoir si c'est un fichier ou un dossier et le mettre sous forme de String
	    			String typeFound = line.substring(0, 7);
	    			
	    			//conna�tre la taille et la mettre sous forme de String
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
			  		
//			  		// sha1 elements
//			  		Element sha1 = doc.createElement("sha1");
//			  		sha1.appendChild(doc.createTextNode("xxxxxx"));
//			  		data.appendChild(sha1);
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
    
    /*Trouve la date dans une chaine de caract�re enregistr�e dans la base
     * @param ligne de la base � d�couper
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
    
    /*Trouve la taille dans une chaine de caract�re enregistr�e dans la base
     * @param ligne de la base � d�couper
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
    
	/* Ecris dans un fichier en mode append (� la fin du fichier en le modifiant)
	 * @param nom du fichier dans lequel �crire
	 * @param texte � �crire dans ce fichier
	 */
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
}
