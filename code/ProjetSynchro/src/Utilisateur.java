/**
 * 
 */

/**
 * @author Melvin
 *
 */
public class Utilisateur {

	private String nom;
	private int sessionId;
	private boolean push;
	
	Utilisateur() {
		nom = "";
		sessionId = 0;
		push = false;
	}

	public boolean getPush() {
		return push;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	@Override
	public String toString() {
		return "Utilisateur [nom=" + nom + ", sessionId=" + sessionId
				+ ", push=" + push + "]";
	}
	
	
	
}
