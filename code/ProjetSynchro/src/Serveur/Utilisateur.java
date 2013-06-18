package Serveur;

public class Utilisateur {
	
	private String nom;
	private String sessionid;
	private String password;
	private Boolean push;
	private static int sessionidIncremental = 100;
	
	public Utilisateur(String _nom, String _password){
		
		nom = _nom;
		push = false;
		password = _password;
		GenererSessionid();
		sessionidIncremental++;
	}
	
	private void GenererSessionid(){
		sessionid = Integer.toString(sessionidIncremental);
	}
	
	public String getNom(){
		return nom;
	}

	public String getSessionid(){
		return sessionid;
	}
	
	public Boolean getPush(){
		return push;
	}
	
	public void setPush(Boolean _valeur){
		push = _valeur;
	}
	
	public boolean verifierPassword(String _retourclient, String _challenge){
		
		if (_retourclient.contentEquals(password + _challenge)){
			return true;
		}else{
			return false;
		}
	}
}
