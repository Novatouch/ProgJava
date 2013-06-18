package Client;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class BaseClient {
	
	String cheminRepertoire;
	
	BaseClient(String cheminRep) {
		cheminRepertoire = cheminRep;

	}
	
	/* Recup�re dans un fichier base toutes les informations des �l�ments du repertoire utilisateur
	 * @param nom de la base texte
	 * @param nom de la base XML
	 * @param utilisateur de la base
	 */
	public void recupererBases(String nomBase, String nomBaseXML, String userConnected) {
		RecupererBaseEnParcoursArborescence parcoursArborescence = new RecupererBaseEnParcoursArborescence(cheminRepertoire, true, nomBase);
        // Mettre à jour la base

        parcoursArborescence.list();
        System.out.println("");
        System.out.println("......................................");
        System.out.println("Résulats de " + cheminRepertoire);
        System.out.println(parcoursArborescence.cptDossier + " dossiers");
        System.out.println(parcoursArborescence.cptFichier + " fichiers");
        parcoursArborescence.baseToXML(nomBase, userConnected, nomBaseXML);
	}

	public String getCheminRepertoire() {
		return cheminRepertoire;
	}
	
}
	
	


