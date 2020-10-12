package serveurs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import banque.BanqueSimple;
import protocole.GestionProtocole;

public class ServeurMultiProtocole implements Runnable {
	
	// On crée une banque et un objet pour gérer le protocole
	// et on les associe
	GestionProtocole gp = new GestionProtocole(new BanqueSimple());

	private void travailTCP() {


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

	private void travailUDP() {
		// Déclaration d’un socket datagramme
		DatagramSocket sock;

		try {
			sock = new DatagramSocket(13214); // Lie au port UDP 13214

			while (true) {
				// Construction du tampon et de l’objet qui vont servir à recevoir
				byte[] buffer = new byte[255];
				DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);

				// Attends puis reçoit un datagramme
				sock.receive(dgram);

				// Récupération des infos sur l’émetteur pour lui répondre
				InetAddress correspondant = dgram.getAddress();
				int portCorrespondant = dgram.getPort();

				// Extraction des données
				// ( dgram.getLength() contient le nb d'octets effectivement reçus)

				String requete = new String(buffer, 0, dgram.getLength());

				String reponse = gp.traiter(requete);

				// Conversion du message en tableau d’octets
				byte[] dataBytes = reponse.getBytes();

				// Construction du DatagramPacket
				DatagramPacket dgramSend = new DatagramPacket(dataBytes, dataBytes.length, correspondant,
						portCorrespondant);

				// Envoi du datagramme
				sock.send(dgramSend);

			}

		}
		
		

		catch (IOException ioe) {
			System.out.println("Erreur création socket: " + ioe.getMessage());
			return;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.travailUDP();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServeurMultiProtocole sm = new ServeurMultiProtocole();
		Thread thread = new Thread(sm);
		thread.start();
		sm.travailTCP();
	}



}
