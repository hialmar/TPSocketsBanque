package serveurs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import banque.BanqueSimple;
import protocole.GestionProtocole;

public class ServeurUDP {

	public static void main(String[] args) {
		// Déclaration d’un socket datagramme
		DatagramSocket sock;    
		// On crée une banque et un objet pour gérer le protocole
		// et on les associe
		GestionProtocole gp = new GestionProtocole(new BanqueSimple());
		
		try {
		      sock = new DatagramSocket(13214);     // Lie au port UDP 13214
		      
		      while(true) {
		    	// Construction du tampon et de l’objet qui vont servir à recevoir
		          byte[] buffer = new byte[255];
		          DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);
		          
		           // Attends puis reçoit un datagramme   
		           sock.receive(dgram);

		           // Récupération des infos sur l’émetteur pour lui répondre
		           InetAddress correspondant = dgram.getAddress();
		           int portCorrespondant = dgram.getPort();

		           //  Extraction des données
		           // ( dgram.getLength() contient le nb d'octets effectivement reçus)

		           String requete =new String(buffer, 0, dgram.getLength());
		    	  
		           String reponse = gp.traiter(requete);
		           
		        // Conversion du message en tableau d’octets
		           byte[] dataBytes = reponse.getBytes(); 

		          //  Construction du DatagramPacket
		          DatagramPacket dgramSend =
		                  new DatagramPacket(dataBytes, dataBytes.length, 
		                		  correspondant, portCorrespondant);

		          // Envoi du datagramme
		          sock.send(dgramSend);
		           
		      }
		      
		      
		}

		catch(IOException ioe) {
		      System.out.println("Erreur création socket: " + ioe.getMessage());
		      return;
		}
	}

}
