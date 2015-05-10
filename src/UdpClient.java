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
	
	@Override
	public void run() {
		final UdpClient udpClient = this;
		Thread t = new Thread() {
		    public void run() {
				for(int sucessorId : udpClient.peer.sucessors) {
						Message message = new PingRequest(peer.getId(), sucessorId);
						udpClient.sendMessage(message);
				}
		    }
		};
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(t, 0, 5, TimeUnit.SECONDS);
	}
	
	public void sendMessage(Message message) {
		int receiverId = message.getReceiverId();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(message);	
			byte[] data = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Peer.toPort(receiverId));
			socket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("shit in UDP CLIENT int sendMessage");
			e.printStackTrace();
		}	
	}
}