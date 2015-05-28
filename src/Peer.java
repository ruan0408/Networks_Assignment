import java.io.IOException;
import java.net.DatagramSocket;

public class Peer {

	private int id;
	private int firstPredecessor;
	private int secondPredecessor;
	private int firstSucessor;
	private int secondSucessor;
	UdpClient udpClient;
	UdpServer udpServer;
	TcpClient tcpClient;
	TcpServer tcpServer;
	
	Peer(int id, int firstSucessor, int secondSucessor) throws IOException{
		this.id = id;
		this.firstSucessor = firstSucessor;
		this.secondSucessor = secondSucessor;
	
		DatagramSocket udpSocket = new DatagramSocket(Peer.toPort(id));
		udpClient = new UdpClient(this, udpSocket);
		udpServer = new UdpServer(this, udpSocket);
		tcpClient = new TcpClient(this);
		tcpServer = new TcpServer(this);
	}
	static int toPort(int id) {return 50000+id;}
	static int hash(int file) {return file%256;}
	public void quit() {System.exit(0);}
	public int getId() {return id;}
	public int getFirstPredecessor() {return firstPredecessor;}
	public int getSecondPredecessor() {return secondPredecessor;}
	public int getFirstSucessor() {return firstSucessor;}
	public int getSecondSucessor() {return secondSucessor;}
	public void setFirstPredecessor(int id) {firstPredecessor = id;}
	public void setSecondPredecessor(int id) {secondPredecessor = id;}
	public void setFirstSucessor(int id) {firstSucessor = id;}
	public void setSecondSucessor(int id) {secondSucessor = id;}
	
	/*
	 * Starts the servers and clients.
	 */
	public void run() {
		tcpClient.start();
		tcpServer.start();
		udpServer.start();
		udpClient.start();
	}
	
	/*
	 * Method for updating the current sucessors. This method will be called when one sucessor
	 * of mine is leaving. This sucessor will pass me it's id and it's own sucessors, so I can
	 * decide how to update.
	 */
	protected void updateSucessors(int leavingPeer, int candSucessor1, int candSucessor2) {
		if(leavingPeer == getFirstSucessor()) {//then Im the first predecessor
			setFirstSucessor(candSucessor1);
			setSecondSucessor(candSucessor2);
		}
		else if(leavingPeer == getSecondSucessor())//then Im the second predecessor
			setSecondSucessor(candSucessor1);		
	}
	/*
	 * This method will be called when someone sends a PingRequest message to this peer.
	 * The sending peer will pass it's first sucessor, so I can decide if he is my firstPredecessor
	 * or secondPredecessor.
	 */
	protected void updatePredecessors(int peerId, int sucessor1) {
		if(getId() == sucessor1)
			setFirstPredecessor(peerId);
		else
			setSecondPredecessor(peerId);
	}
}