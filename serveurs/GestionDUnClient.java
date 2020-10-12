package serveurs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import protocole.GestionProtocole;

/**
 * Gère la communication avec un client connecté
 * @author torguet
 *
 */
public class GestionDUnClient implements Runnable {

	/**
	 * Le socket de service connecté avec le client
	 */
	private Socket sockService;
	/**
	 * L'objet qui gère le protocole
	 */
	private GestionProtocole gp;
	
	/**
	 * constructeur
	 * @param sockService : le socket de service connecté avec le client
	 * @param gp : l'objet qui gère le protocole
	 */
	public GestionDUnClient(Socket sockService, GestionProtocole gp) {
		super();
		// on recopie les références dans les attributs
		this.sockService = sockService;
		this.gp = gp;
	}

	/**
	 * Méthode exécutée par le thread
	 * Elle gère la communication avec le client
	 */
	@Override
	public void run() {

		try {
			// Instancie un BufferedReader travaillant sur un InputStreamReader lié à
			// l’input stream de la socket

			BufferedReader reader = new BufferedReader(new InputStreamReader(sockService.getInputStream()));

			// Instancie un PrintStream travaillant sur l’output stream de la socket

			PrintStream pStream = new PrintStream(sockService.getOutputStream());

			while (true) {
				// Lit une ligne de caractères depuix le flux, et donc la reçoit du client
				String requete = reader.readLine();

				// si la requête vaut null, c'est que le client a fermé la connexion
				if (requete == null)
					break; // on sort de la boucle

				// On traite la requête
				String reponse = gp.traiter(requete);

				// écrit une ligne de caractères sur le flux, et donc l’envoie au client
				pStream.println(reponse);
			}
			// on ferme nous aussi la connexion
			sockService.close();
		} catch (Exception ex) {
			// on affiche un message d'erreur standard
			ex.printStackTrace();
		}
	}

}
