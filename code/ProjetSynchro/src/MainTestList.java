/**
 * 
 */

/**
 * @author Melvin
 *
 */
public class MainTestList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ListeClientConnecte lList = new ListeClientConnecte();
		Utilisateur lUser = new Utilisateur();
		Utilisateur lUser1 = new Utilisateur();
		//Utilisateur lUser2 = new Utilisateur();
		boolean lTest = false;
		
		lUser.setNom("Toto");
		lUser.setSessionId(1);
		lUser.setPush(false);
		
		lList.ajouterClient(lUser);
		
		lUser1.setNom("TotoBis");
		lUser1.setSessionId(2);
		lUser1.setPush(false);
		
		lList.ajouterClient(lUser1);
		
		System.out.println(lList.getNombreClients());
		lList.afficherListe();
		
		lTest = lList.rechercherClient(lUser);
		
		System.out.println(lTest);
		
		lList.supprimerClient(lUser);
		
		System.out.println(lList.getNombreClients());
		lList.afficherListe();
	}

}
