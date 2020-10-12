package clients;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientUDP {
	private String nomServeur;
	private int numeroPort;
	// Déclaration d’un socket datagramme
	private DatagramSocket sock;    

	

	/**
	 * constructeur
	 * @param nomServeur : nom du serveur
	 * @param numeroPort : numéro de port du serveur
	 * @throws SocketException 
	 */
	public ClientUDP(String nomServeur, int numeroPort) throws SocketException {
		super();
		this.nomServeur = nomServeur;
		this.numeroPort = numeroPort;
		this.sock = new DatagramSocket();
	}

	/**
	 * communique avec le serveur
	 * @param requete : requete à envoyer
	 * @return réponse reçue
	 */
	private String communication(String requete) {
		try {
	//  Construction de l’@IP
	      InetAddress destAddr = InetAddress.getByName(this.nomServeur);

	     // Choix  du port destination
	      int destPort = this.numeroPort;

	     // Conversion du message en tableau d’octets
	     byte[] dataBytes = requete.getBytes(); 

	    //  Construction du DatagramPacket
	    DatagramPacket dgram =
	            new DatagramPacket(dataBytes, dataBytes.length, destAddr, destPort);

	    // Envoi du datagramme
	    sock.send(dgram);
	    
	 // Construction du tampon et de l’objet qui vont servir à recevoir
	      byte[] buffer = new byte[255];
	      dgram.setData(buffer);
	      
	       // Attends puis reçoit un datagramme   
	       sock.receive(dgram);

	      //  Extraction des données
	      // ( dgram.getLength() contient le nb d'octets effectivement reçus)

	      String reponse = new String(buffer, 0, dgram.getLength());
	      
	      return reponse;
	    
		} catch(Exception ex) {
			return "ERREUR de communication";
		}
		
	}

	/**
	 * Gestion du menu principal
	 */
	private void travail() {
		// Sert à récupérer les entrées clavier de l'utilisateur
		Scanner scan = new Scanner(System.in);
		
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
			String reponse;
			try {
				// on attend un entier de l'utilisateur
				int menu = scan.nextInt();
				// on saute le retour à la ligne
				scan.nextLine();
				
				// en fonction de ce qu'a tapé l'utilisateur
				switch(menu) {
				case 0:
					nonFini = false;
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
					reponse = this.communication("CREATION "+id+" "+somme);
					// on affiche la réponse
					System.out.println(reponse);
					break;
				case 2:
					// position d'un compte
					// il nous faut l'id
					System.out.println("Quel est l'id :");
					id = scan.nextLine();
					// on construit et envoie la requête au serveur
					reponse = this.communication("POSITION "+id);
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
					reponse = this.communication("AJOUT "+id+" "+somme);
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
					reponse = this.communication("RETRAIT "+id+" "+somme);
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

	/**
	 * Méthode principale
	 * @param args : parametres du programme
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException {
		ClientUDP client = new ClientUDP("localhost",13214);
		client.travail();
	}

}
