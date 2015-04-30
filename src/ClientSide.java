import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientSide extends Thread {

	DatagramSocket socket;
	int id;
	int[] sucessors;
	int[] predecessors;
	
	public ClientSide(int id, int firstSucessor, int secondSucessor) throws SocketException {
		this.id = id;
		this.socket = new DatagramSocket();
		//this.socket = socket;
		this.sucessors = new int[2];
		this.sucessors[0] = firstSucessor;
		this.sucessors[1] = secondSucessor;
		this.predecessors = new int[2];
	}
	
	public void updatePredecessors(int predecessor) {
		int aux;
		if(predecessor > this.predecessors[0]) {
			aux = this.predecessors[0];
			this.predecessors[0] = predecessor;
			this.predecessors[1] = aux;
		}
		else if(predecessor != this.predecessors[0]) this.predecessors[1] = predecessor;
			
		//System.out.println(this.predecessors[1]+" "+this.predecessors[0]);
		//System.out.println("updating predecessor "+predecessor);
		//this.predecessors[0] = firstPredecessor;
		//this.predecessors[1] = secondPredecessor;
	}
	
	public void lookUp(int fileNumber) {
		
	}
	
	public void run() {
		
		Scanner scan =  new Scanner(System.in);
		DatagramPacket request, response;
		String[] userRequest;
		String message = "A ping request message was received from Peer "+ this.id;
		request = new DatagramPacket(message.getBytes(), message.getBytes().length);
		
		try {
			request.setAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e1) {
			System.out.println("Couldn't set IP address to localhost");
		}
		byte[] buf = new byte[1024];
		response = new DatagramPacket(buf, buf.length);
	
		long startTime = System.currentTimeMillis();
		while(true) {
			try {
				if(System.in.available() != 0) {
					userRequest =	scan.nextLine().split("\\s+");
					if(userRequest[0].equals("request")) {
						this.lookUp(Integer.parseInt(userRequest[1]));
					}
					else if(userRequest[0].equals("quit")) {
						
					}
					else
						System.out.println("----- This is not a valid option!");
				}
			} catch (IOException e1) {
				System.out.println("shit happened on the INPUT STREAM");
			}
			if(System.currentTimeMillis() - startTime > 5000)	{
				for(int sucessorId : this.sucessors) {
			  	  	try {
			  	  		request.setPort(50000+sucessorId);
						this.socket.send(request);
						
						socket.receive(response);
						this.printPacketData(response);
						
					} catch (Exception e) {
						System.out.println("shit happened in CLIENT");
					}
				}
				startTime = System.currentTimeMillis();	
			}
		}
	}
	
	private void printPacketData(DatagramPacket packet) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		System.out.println(br.readLine());
	}
}
