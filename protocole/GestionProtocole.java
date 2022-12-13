package protocole;

import banque.BanqueSimple;

/**
 * Classe qui gère le protocole textuel.
 * 
 * @author torguet
 *
 */
public class GestionProtocole {
	private BanqueSimple banque;

	/**
	 * constructeur
	 * 
	 * @param banque : la banque sur laquelle nous allons faire les traitements.
	 */
	public GestionProtocole(BanqueSimple banque) {
		super();
		this.banque = banque;
	}

	/*
	 * 
	 * PROTOCOLE
	 * 
	 * Requêtes
	 * 
	 * CREATION id somme_initiale : permet de demander la création d’un compte
	 * identifié par id sur lequel sera placé la somme_initiale.
	 * 
	 * 
	 * POSITION id : permet d’obtenir la position courante du compte identifié par
	 * id.
	 * 
	 * AJOUT id somme : ajoute une somme sur le compte identifié par id.
	 * 
	 * RETRAIT id somme : retire une somme sur le compte identifié par id.
	 * 
	 * Réponses :
	 * 
	 * OK commande : informe le client que la commande s'est correctement déroulée.
	 * 
	 * 
	 * ERREUR raison : la raison de l'échec de la commande sous forme de chaîne de
	 * caractères.
	 * 
	 * POS solde date_dernière_opération : envoi au client le solde actuel du compte
	 * et la date de la dernière opération.
	 * 
	 */

	/**
	 * Analyse une requête et appelle la bonne méthode sur la banque
	 * 
	 * @param requete : la requête à traiter
	 * @return réponse à la requête
	 */
	public String traiter(String requete) {
		try {
			// On découpe la requête
			String[] tab = requete.split(" ");

			// Le premier paramètre est toujours un id
			String id = tab[1];

			// certaines requêtes transportent aussi une somme en second paramètre
			double somme = 0;

			// le premier mot est le type de requête
			switch (tab[0]) {
			case "CREATION":
				// il faut vérifier que le compte n'existe pas déjà
				if (banque.compteExiste(id)) {
					// on retourne une erreur
					return "ERREUR compte existant";
				} else {
					// on récupère la somme
					somme = Double.parseDouble(tab[2]);
					// on crée le compte
					banque.creerCompte(id, somme);
					// on retourne la réponse
					return "OK CREATION";
				}
			case "AJOUT":
				// il faut vérifier que le compte existe bien
				if (!banque.compteExiste(id)) {
					// on retourne une erreur
					return "ERREUR compte inexistant";
				} else {
					// on récupère la somme
					somme = Double.parseDouble(tab[2]);
					// on ajoute la somme
					banque.ajouter(id, somme);
					// on retourne la réponse
					return "OK AJOUT";
				}
			case "RETRAIT":
				// il faut vérifier que le compte existe bien
				if (!banque.compteExiste(id)) {
					// on retourne une erreur
					return "ERREUR compte inexistant";
				} else {
					// on récupère la somme
					somme = Double.parseDouble(tab[2]);
					// on retire la somme
					banque.retirer(id, somme);
					// on retourne la réponse
					return "OK RETRAIT";
				}
			case "POSITION":
				// il faut vérifier que le compte existe bien
				if (!banque.compteExiste(id)) {
					// on retourne une erreur
					return "ERREUR compte inexistant";
				} else {
					// on récupère le solde du compte
					somme = banque.getSolde(id);
					// on récupère la date de dernière opération
					String date = banque.getDerniereOperation(id).toString();
					// on construit puis retourne la réponse
					return "POS " + somme + " " + date;
				}
			default:
				// le type de requête ne correspond à rien de connu
				return "ERREUR requête inconnue";
			}
		} catch (NullPointerException ex) {
			// si la requête était nulle on renvoie une erreur
			return "ERREUR requête nulle";
		} catch (ArrayIndexOutOfBoundsException ex2) {
			// le tableau tab n'avait pas assez d'éléments
			// ça veut dire qu'il manquait un ou plusieurs paramètres
			// on retourne une erreur
			return "ERREUR pas assez de paramètres";
		} catch (NumberFormatException ex3) {
			// La conversion de la chaine du second paramètre en double
			// a échoué
			// on retourne une erreur
			return "ERREUR paramètre d'un mauvais type";
		}
	}

	/**
	 * méthode principale pour tester la classe
	 * @param args
	 */
	public static void main(String[] args) {
		// On crée une banque et un objet pour gérer le protocole
		// et on les associe
		GestionProtocole gp = new GestionProtocole(new BanqueSimple());
		// Pour tester GestionProtocole
		System.out.println(gp.traiter("CREATION id 1000"));
		System.out.println(gp.traiter("POSITION id"));
		System.out.println(gp.traiter("AJOUT id 1000"));
		System.out.println(gp.traiter("RETRAIT id 1000"));
		System.out.println(gp.traiter("POSITION id"));
		System.out.println(gp.traiter(null));
		System.out.println(gp.traiter("TOTO"));
		System.out.println(gp.traiter("TOTO id"));
		System.out.println(gp.traiter("CREATION"));
		System.out.println(gp.traiter("CREATION id2"));
		System.out.println(gp.traiter("CREATION id2 toto"));
		System.out.println(gp.traiter("CREATION id 100"));
		System.out.println(gp.traiter("AJOUT id3 100"));

	}

}
