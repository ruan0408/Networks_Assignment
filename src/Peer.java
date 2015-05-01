import java.io.IOException;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.Queue;

public class Peer {

	int id;
	int[] sucessors;
	Queue<Integer> predecessors;
	
	UdpClient udpClient;
	UdpServer udpServer;
	TcpClient tcpClient;
	TcpServer tcpServer;
	
	Peer(int id, int firstSucessor, int secondSucessor) throws IOException{
		this.id = id;
		this.sucessors = new int[2];
		this.predecessors = new LinkedList<Integer>();
		this.predecessors.add(-1);
		this.predecessors.add(-1);
		
		this.sucessors[0] = firstSucessor;
		this.sucessors[1] = secondSucessor;
	
		DatagramSocket udpSocket = new DatagramSocket(50000+id);
		
		this.udpClient = new UdpClient(this, udpSocket);
		this.udpServer = new UdpServer(this, udpSocket);
		
		this.tcpClient = new TcpClient(this);
		this.tcpServer = new TcpServer(this);
	}
	
	public void run() {
		this.tcpClient.start();
		this.tcpServer.start();
		this.udpServer.start();
		this.udpClient.start();
	}
	
	public void takeAppropriateAction(String receivedMessage, int senderPort) throws IOException {
		
		if(receivedMessage.trim().equals("PING REQUEST")) {
			System.out.println("A ping request message was received from Peer "+(senderPort-50000));
			this.updatePredecessors(senderPort-50000);
			this.udpClient.sendMessage("PING RESPONSE", senderPort);
		}
		else if(receivedMessage.trim().equals("PING RESPONSE")) {
			System.out.println("A ping response message was received from Peer "+(senderPort-50000));
		}
		else if(receivedMessage.trim().matches("request \\d+ \\d+")) {//fileNumber and requesterPort
			String[] tokens = receivedMessage.split("\\s+");
			int fileNumber = Integer.parseInt(tokens[1]);
			int fileHash = fileNumber%256;
			if(this.getFirstPredecessor() < fileHash && 
				(fileHash <= this.id || this.id < this.getFirstPredecessor())) {
				int peer = Integer.parseInt(tokens[2]);
				System.out.println("File "+fileNumber+" is here.");
				//send message to requesting peer 
				this.tcpClient.sendMessage("QUERY RESPONSE "+fileNumber+" "+this.id, peer+50000);
				System.out.println("A response message, destined for peer "+peer+", has been sent.");
			}
			else {
				//forward request...
				this.tcpClient.sendMessage(receivedMessage, this.getFirstSucessor()+50000);
			}
		}
		else if(receivedMessage.trim().matches("QUERY RESPONSE \\d+ \\d+")) { //file and peer holding the file
			String[] tokens = receivedMessage.split("\\s+");
			int fileNumber = Integer.parseInt(tokens[2]);
			int peer = Integer.parseInt(tokens[3]);
			System.out.println("Received a response message from peer "+peer+", which has the file "+fileNumber+".");
		}
	}
	
	public void updatePredecessors(int peerId) {
		this.predecessors.add(peerId);
		this.predecessors.remove();
	}
	
	public int getFirstPredecessor() {
		return this.predecessors.peek();
	}
	public int getFirstSucessor() {
		return this.sucessors[0];
	}
	
	public int getId() {
		return this.id;
	}
}
