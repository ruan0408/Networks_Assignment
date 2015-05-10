import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {

	private Peer peer;
	private ServerSocket serverSocket;
	
	public TcpServer(Peer peer) throws IOException {
		this.peer = peer;
		this.serverSocket = new ServerSocket(Peer.toPort(peer.getId()));
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				Message message = (Message) inStream.readObject();
				message.executeAction(peer);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
