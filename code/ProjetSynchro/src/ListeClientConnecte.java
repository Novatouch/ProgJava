
import java.util.ArrayList; 

/**
 * 
 */

/**
 * @author Melvin
 *
 */
public class ListeClientConnecte {

	ArrayList<Utilisateur> listeClients;
	int nombreClients;
	
	ListeClientConnecte() {
		
		listeClients = new ArrayList<Utilisateur>();
		nombreClients = 0;
		
	}

	void ajouterClient(Utilisateur pClient) {
		listeClients.add(pClient);
		nombreClients++;
	}
	
	void supprimerClient(Utilisateur pClient) {
		listeClients.remove(pClient);
		nombreClients--;
	}
	
	boolean rechercherClient(Utilisateur pClient) {
		boolean lRet = false;
		lRet = listeClients.contains(pClient);
		return(lRet);
	}
	
	/* Affiche la liste des clients connectés
	 * 
	 */
	void afficherListe() {
		
		for(int i = 0 ; i < listeClients.size() ; i++) {
			System.out.println(listeClients.get(i));	
		}
	}

	public int getNombreClients() {
		return nombreClients;
	}
	
	

}