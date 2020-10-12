package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import banque.BanqueSimple;
import protocole.GestionProtocole;

/**
 * Le serveur TCP
 * @author torguet
 *
 */
public class ServeurTCP {

	/**
	 * Méthode principale
	 * @param args arguments du programme qu'on n'utilise pas ici
	 */
	public static void main(String[] args) {
		// On crée une banque et un objet pour gérer le protocole
		// et on les associe
		GestionProtocole gp = new GestionProtocole(new BanqueSimple());

		// Déclaration du ServerSocket
		ServerSocket sockEcoute;

		// Instanciation du ServerSocket en utilisant le constructeur
		// le plus simple (on précise le port)
		try {
			// Étape 1
			sockEcoute = new ServerSocket(13214);

			// Boucle permettant de gérer tous les clients
			while (true) {
				try {
					// on attend une nouvelle connexion
					Socket sockService = sockEcoute.accept();

					// on crée un thread pour gérer la communication avec le client
					GestionDUnClient gduc = new GestionDUnClient(sockService, gp);
					Thread thread = new Thread(gduc);
					thread.start();
					
				} catch (IOException ioe) {
					System.out.println("Erreur de accept : " + ioe.getMessage());
					break;
				}

			}
		} catch (IOException ioe) {
			System.out.println("Erreur de création du server socket: " + ioe.getMessage());
			return;
		}
	}

}
