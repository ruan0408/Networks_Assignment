import java.io.IOException;
import java.net.DatagramSocket;

public class Peer {

	int id;
	int[] sucessors;
	int[] predecessors;
	DatagramSocket udpSocket;
	
	UdpClient udpClient;
	UdpServer udpServer;
	
	Peer(int id, int firstSucessor, int secondSucessor) throws IOException{
		this.id = id;
		this.sucessors = new int[2];
		this.sucessors[0] = firstSucessor;
		this.sucessors[1] = secondSucessor;
	
		DatagramSocket socket = new DatagramSocket(50000+id);
		this.udpClient = new UdpClient(this, socket);
		this.udpServer = new UdpServer(this, socket);
	}
	
	public void run() {
		this.udpServer.start();
		this.udpClient.start();
	}
	
	public void takeAppropriateAction(String receivedMessage, int senderPort) throws IOException {
		if(receivedMessage.trim().equals("PING REQUEST")) {
			System.out.println("A ping request message was received from Peer "+(senderPort-50000));
			this.udpClient.sendMessage("PING RESPONSE", senderPort);
		}
		else if(receivedMessage.trim().equals("PING RESPONSE")) {
			System.out.println("A ping response message was received from Peer "+(senderPort-50000));
		}
	}
}
