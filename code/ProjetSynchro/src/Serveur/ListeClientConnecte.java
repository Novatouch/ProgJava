package Serveur;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ListeClientConnecte {
	
	
	private ConcurrentHashMap<Integer,Utilisateur> liste;
	
	public ListeClientConnecte(){
		liste = new ConcurrentHashMap<Integer,Utilisateur>();
	}

	public void ajouterClient(Utilisateur _user){
		liste.put(Integer.parseInt(_user.getSessionid()), _user);
	}
	
	public void supprimerClient(String _sessionid){
		liste.remove(Integer.parseInt(_sessionid));
	}
	
	public Boolean rechercherClient(String _client){

		Collection<Utilisateur> listeUtilisateur = liste.values();
		
		Iterator<Utilisateur> iterator = listeUtilisateur.iterator();
		
		while(iterator.hasNext()){
			
			Utilisateur util = iterator.next();

			if(util.getNom().contentEquals(_client)){
				
				return true;
			}
		}
		
		return false;
	
	}
	
	public Boolean rechercherClientSession(String _sessionid){
		
		return liste.contains(Integer.parseInt(_sessionid));
	}
}
