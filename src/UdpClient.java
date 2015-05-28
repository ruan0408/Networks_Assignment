import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.*;

public class UdpClient extends Thread {

	DatagramSocket socket;
	Peer peer;
	
	public UdpClient(Peer myPeer, DatagramSocket socket) throws SocketException {
		this.peer = myPeer;
		this.socket = socket;
	}
	
	/*
	 * Sends PingRequest messages within a 5 second interval.
	 */
	@Override
	public void run() {
		Thread t = new Thread() {
		    public void run() {
				Message message = new PingRequest(peer.getId(), 
						peer.getFirstSucessor(), peer.getFirstSucessor());
				sendMessage(message);
				message = new PingRequest(peer.getId(), 
						peer.getSecondSucessor(), peer.getFirstSucessor());
				sendMessage(message);
		    }
		};
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(t, 0, 5, TimeUnit.SECONDS);
	}
	
	/*
	 * Sends a message through a UDP connection.
	 */
	public void sendMessage(Message message) {
		int receiverId = message.getReceiverId();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(message);	
			byte[] data = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, 
					InetAddress.getLocalHost(), Peer.toPort(receiverId));
			
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}