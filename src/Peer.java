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
		sucessors = new int[2];
		predecessors = new LinkedList<Integer>();
		predecessors.add(-1);
		predecessors.add(-1);
		
		sucessors[0] = firstSucessor;
		sucessors[1] = secondSucessor;
	
		DatagramSocket udpSocket = new DatagramSocket(Peer.toPort(id));
		
		udpClient = new UdpClient(this, udpSocket);
		udpServer = new UdpServer(this, udpSocket);
		
		tcpClient = new TcpClient(this);
		tcpServer = new TcpServer(this);
	}
	
	public void run() {
		tcpClient.start();
		tcpServer.start();
		udpServer.start();
		udpClient.start();
	}
	
	static int toPort(int id) {
		return 50000+id;
	}
	
	static int hash(int file) {
		return file%256;
	}
	
	protected void updateSucessors(int leavingPeer, int candSucessor1, int candSucessor2) {
		if(leavingPeer == sucessors[0]) {//then Im the first predecessor
			sucessors[0] = candSucessor1;
			sucessors[1] = candSucessor2;
		}
		else if(leavingPeer == sucessors[1]) {//then Im the second predecessor
			sucessors[1] = candSucessor1;
		}
		System.out.println("My first sucessor is now peer "+sucessors[0]+".");
		System.out.println("My second sucessor is now peer "+sucessors[1]+".");		
	}
	
	public void quit() {
		System.exit(0);
	}
	
	public void updatePredecessors(int peerId) {
		predecessors.add(peerId);
		predecessors.remove();
	}
	
	public int getFirstPredecessor() {
		return predecessors.peek();
	}
	
	public int getFirstSucessor() {
		return sucessors[0];
	}
	
	public int getSecondSucessor() {
		return sucessors[1];
	}
	
	public int getId() {
		return id;
	}
}