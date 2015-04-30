import java.net.DatagramSocket;

public class Peer {

	int id;
	int[] sucessors;
	DatagramSocket socket;
	
	ClientSide client;
	ServerSide server;
	
	Peer(int id, int firstSucessor, int secondSucessor){
		this.id = id;
		this.sucessors = new int[2];
		this.sucessors[0] = firstSucessor;
		this.sucessors[1] = secondSucessor;
		try{
			//DatagramSocket socket1 = new DatagramSocket(50000+id);
			client = new ClientSide(id, firstSucessor, secondSucessor);
			server = new ServerSide(id);
			server.setClientSide(client);
		} catch(Exception e) {
			System.out.println("Could not open socket");
		}
		
	}
	
	public int getFirstSucessor(){
		return this.sucessors[0];
	}
	
	public int getSecondSucessor(){
		return this.sucessors[1];
	}
	
	public void run() {
		this.server.start();
		this.client.start();
	}
}
