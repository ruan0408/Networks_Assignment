import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class ServerSide extends Thread {

	DatagramSocket socket;
	ServerSocket tcpSocket;
	ClientSide clientSide;
	
	public ServerSide(int id) throws IOException {
		this.socket = new DatagramSocket(50000+id);
		this.tcpSocket = new ServerSocket(50000+id);
		//this.socket = socket;
	}
	
	public void setClientSide(ClientSide client) {
		this.clientSide = client;
	}
	
	public void run() {
		String message = "A ping response message was received from Peer "+(this.socket.getLocalPort()-50000);
		byte[] respMessage = message.getBytes();
		byte[] buf = new byte[3000];
		int senderPort;
		DatagramPacket packet;
		
		while(true) {
			try {
				packet = new DatagramPacket(buf, buf.length);
				this.socket.receive(packet);
				this.printPacketData(packet);
	    			    		
				senderPort = packet.getPort();
				
				packet = new DatagramPacket(respMessage, respMessage.length, InetAddress.getLocalHost(), senderPort);
				socket.send(packet);
			} catch(Exception e){
				System.out.println("shit happened in SERVER");
				e.printStackTrace();
			}
		}
	}
	
	private void printPacketData(DatagramPacket packet) throws IOException {
		String[] aux;
		String s;
		ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		s = br.readLine();
		System.out.println(s);
		aux = s.split("\\s+");
		this.clientSide.updatePredecessors(Integer.parseInt(aux[aux.length-1].trim()));
	}
}
