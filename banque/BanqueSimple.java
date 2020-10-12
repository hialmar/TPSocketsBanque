package banque;
import java.util.Date;
import java.util.HashMap;

/*
 * Classe qui gère la banque de façon simple (en mémoire)
 */
public class BanqueSimple {
	/**
	 * La map des comptes référencés par leur id
	 */
    private HashMap<String, CompteEnBanque> comptes;

    /**
     * construceur
     */
    public BanqueSimple() {
    	// on crée la map
        comptes = new HashMap<String, CompteEnBanque>();
    }

    /**
     * crée un compte dont on précise l'id et la somme initiale
     * @param id : id du futur compte
     * @param somme : somme initiale
     */
    public synchronized void creerCompte(String id, double somme) {
    	// on crée le compte et on l'insére dans la mab
        comptes.put(id, new CompteEnBanque(somme));
    }

    /**
     * ajoute une somme a un compte existant
     * @param id : id du compte
     * @param somme : somme à ajouter
     */
    public synchronized void ajouter(String id, double somme) {
    	// on cherche le compte
        CompteEnBanque cpt = comptes.get(id);
        // on ajoute la somme au compte trouvé
        cpt.ajouter(somme);
    }

    /**
     * retire une somme a un compte existant
     * @param id : id du compte
     * @param somme : somme à retirer
     */
    public synchronized void retirer(String id, double somme) {
        CompteEnBanque cpt = comptes.get(id);
        cpt.retirer(somme);
    }

    /**
     * récupère le solde d'un compte
     * @param id : id du compte
     * @return le solde du compte
     */
    public synchronized double getSolde(String id) {
        CompteEnBanque cpt = comptes.get(id);
        return cpt.getSolde();
    }

    /**
     * récupère la date de dernière opération d'un compte
     * @param id : id du compte
     * @return la date de dernière opération
     */
    public synchronized Date getDerniereOperation(String id) {
        CompteEnBanque cpt = comptes.get(id);
        return cpt.getDerniereOperation();
    }

    /**
     * Est-ce que le compte existe ?
     * @param id : id du compte
     * @return true s'il existe
     */
    public synchronized boolean compteExiste(String id) {
        return comptes.containsKey(id);
    }

    /**
     * Main pour tester la classe
     * @param args : arguments du programme inutilisés ici
     */
    public static void main(String[] args) {
        BanqueSimple s = new BanqueSimple();
        s.creerCompte("ABC1234", 1000); s.ajouter("ABC1234", 100);
        s.retirer("ABC1234", 30);
        double solde = s.getSolde("ABC1234");
        Date date = s.getDerniereOperation("ABC1234");
        System.out.println("ABC1234 -> " + solde + " " + date);
    }
}
