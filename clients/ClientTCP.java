package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * Le client TCP
 */
public class ClientTCP {

	/**
	 * Méthode principale
	 * @param args : arguments du programme inutilisés ici
	 * @throws UnknownHostException : lancé si on s'est trompé de nom de machine
	 * @throws IOException : lancé s'il y a eu un problème de communication
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// Sert à récupérer les entrées clavier de l'utilisateur
		Scanner scan = new Scanner(System.in);
		
		// Instanciation du socket en précisant le nom de machine et le port
		Socket sock = new Socket("localhost", 13214); 
		// Instancie un BufferedReader travaillant sur un InputStreamReader lié à
		// l’input stream de la socket

		BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		// Instancie un PrintStream travaillant sur l’output stream de la socket

		PrintStream pStream = new PrintStream(sock.getOutputStream());

		// Tant que l'utilisateur ne choisit pas quitter
		boolean nonFini = true;
		while(nonFini) {
			// affiche le menu
			System.out.println("1- créer un compte");
			System.out.println("2- position du compte");
			System.out.println("3- ajouter au compte");
			System.out.println("4- retirer du compte");
			System.out.println("0- quitter");

			// paramètres des requêtes
			String id;
			double somme;
			try {
				// on attend un entier de l'utilisateur
				int menu = scan.nextInt();
				// on saute le retour à la ligne
				scan.nextLine();
				
				// en fonction de ce qu'a tapé l'utilisateur
				switch(menu) {
				case 0:
					nonFini = false;
					sock.close();
					break;
				case 1:
					// création d'un compte
					// il nous faut l'id
					System.out.println("Quel est l'id :");
					id = scan.nextLine();
					// et la somme
					System.out.println("Quel est la somme initiale :");
					somme = scan.nextDouble();
					// on construit et envoie la requête au serveur
					pStream.println("CREATION "+id+" "+somme);
					// on attend la réponse et on la récupère
					String reponse = reader.readLine();
					// on affiche la réponse
					System.out.println(reponse);
					break;
				case 2:
					// position d'un compte
					// il nous faut l'id
					System.out.println("Quel est l'id :");
					id = scan.nextLine();
					// on construit et envoie la requête au serveur
					pStream.println("POSITION "+id);
					// on attend la réponse et on la récupère
					reponse = reader.readLine();
					// on affiche la réponse
					System.out.println(reponse);
					break;
				case 3:
					// ajout d'une somme
					// il nous faut l'id
					System.out.println("Quel est l'id :");
					id = scan.nextLine();
					// et la somme
					System.out.println("Quel est la somme :");
					somme = scan.nextDouble();
					// on construit et envoie la requête au serveur
					pStream.println("AJOUT "+id+" "+somme);
					// on attend la réponse et on la récupère
					reponse = reader.readLine();
					// on affiche la réponse
					System.out.println(reponse);
					break;
				case 4:
					// retrait d'une somme
					// il nous faut l'id
					System.out.println("Quel est l'id :");
					id = scan.nextLine();
					// et la somme
					System.out.println("Quel est la somme :");
					somme = scan.nextDouble();
					// on construit et envoie la requête au serveur
					pStream.println("RETRAIT "+id+" "+somme);
					// on attend la réponse et on la récupère
					reponse = reader.readLine();
					// on affiche la réponse
					System.out.println(reponse);
					break;
				}
			} catch(InputMismatchException ex) {
				// L'utilisateur a tapé quelque chose d'incorrect
				System.err.println("Erreur de type.");
				// on saute ce qu'il a tapé avant de revenir au menu
				scan.nextLine();
			}
		}
		// on ferme l'objet qui permet de récupérer les saisies utilisateur
		scan.close();
	}

}
