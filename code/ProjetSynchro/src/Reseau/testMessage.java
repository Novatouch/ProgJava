package Reseau;

public class testMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String chaine = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><message><user>identidiant utilisateur</user><type>auth</type><status>request</status><info><number>54455</number></info></message>";
	
		try {
			Message req = new Message(chaine);
			
			System.out.println("Affichage requête :" + req.toString());
			
			Message reqAuth;
			
			reqAuth = new Message();
			
			System.out.println(req.getNumber());
			
			reqAuth.authentificationConstructionRequest("toto");
			System.out.println(reqAuth.toString());
			
		} catch (MalformedMessage e) {
			// TODO Auto-generated catch block
			System.out.println("Requête malformée");
		}
	}

}
