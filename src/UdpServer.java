import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer extends Thread {

	Peer peer;
	DatagramSocket socket;
	
	public UdpServer(Peer myPeer, DatagramSocket socket) throws IOException {
		this.peer = myPeer;
		this.socket = socket;
	}
	
	/*
	 * Receives UDP messages and takes appropriate action.
	 */
	@Override
	public void run() {
		byte[] incomingData = new byte[1024];
		
		while(true) {
			try {
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);	
				socket.receive(incomingPacket);
				byte[] data = incomingPacket.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				Message message = (Message) is.readObject();
				message.executeAction(peer);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
