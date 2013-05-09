import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReadWriteManager {

    /**
     * @param args
     */
    public static void lire(String nomFichier) {
       
        String chaine="";
        String ligne;
        //Mettre le chemin du fichier en string
        String fichier = System.getProperty("user.dir") + "/"+ nomFichier;
       
        try{
            InputStream ips = new FileInputStream(fichier); // ouverture du fichier en entrée
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

    public static void ecrire(String nomFic, String texte) {
       
        //Mettre le chemin du fichier en string
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;
   
        try {

            FileWriter fw = new FileWriter(adressedufichier, true); // nouvelle ouverture fichier en écriture ; true = append à la fin du fichier
           
            BufferedWriter output = new BufferedWriter(fw); // ouverture du buffer d'écriture
           
            output.write(texte); // écrit le texte en sortie
           
            output.flush(); // envoie le texte dans le fichier
   
            output.close(); // fermeture du fichier

            System.out.println("fichier créé");
        }
        catch(IOException ioe){ // gestion de l'exception
            System.out.print("Erreur : ");
            ioe.printStackTrace();
            }

    }
   
    public static void main(String[] args) {
        // TODO Auto-generated method stub
       
        String nomFichier = "Moi";
        String texteFichier = "J'écris ça dans le fichier !";
       
        ecrire(nomFichier, texteFichier);
        lire(nomFichier);

    }
   


}
