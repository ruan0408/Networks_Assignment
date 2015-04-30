import java.io.IOException;
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
	
	public void run() {
		final UdpClient udpClient = this;
		Thread t = new Thread() {
		    public void run() {
				for(int sucessorId : udpClient.peer.sucessors)
					try {
						udpClient.sendMessage("PING REQUEST", 50000+sucessorId);
					} catch (IOException e) {
						System.out.println("shit happened on CLIENT");
						e.printStackTrace();
					}	
		    }
		};
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(t, 0, 5, TimeUnit.SECONDS);
	}
	
	public void sendMessage(String message, int peerPort) throws IOException {
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length);
		packet.setAddress(InetAddress.getLocalHost());
		packet.setPort(peerPort);
		this.socket.send(packet);
	}
}