import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer extends Thread {

	Peer peer;
	DatagramSocket socket;
	
	public UdpServer(Peer myPeer, DatagramSocket socket) throws IOException {
		this.peer = myPeer;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		byte[] buf;
		DatagramPacket packet;
		
		ByteArrayInputStream bais;
		InputStreamReader isr;
		BufferedReader br;
		String messageReceived;
		
		while(true) {
			try {
				buf = new byte[3000];
				packet = new DatagramPacket(buf, buf.length);
				this.socket.receive(packet);
				
				bais = new ByteArrayInputStream(packet.getData());
				isr = new InputStreamReader(bais);
				br = new BufferedReader(isr);
				messageReceived = br.readLine();
				
				this.peer.takeAppropriateAction(messageReceived, packet.getPort());
			} catch(Exception e){
				System.out.println("shit happened in SERVER");
				e.printStackTrace();
			}
		}
	}
}
