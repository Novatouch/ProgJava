package connexion;

public class Utilisateur {
	
	private long sessionid;
	private String nom;
	
	public Utilisateur(String _nom){
		this.nom = _nom;
		this.sessionid = Math.round(Math.random() * 100000);
		
	}
	
	public String getNom(){
		return this.nom;
	}

	public long getSessionid(){
		return this.sessionid;
	}
}
