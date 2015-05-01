import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {

	private Peer peer;
	private ServerSocket serverSocket;
	
	public TcpServer(Peer peer) throws IOException {
		this.peer = peer;
		this.serverSocket = new ServerSocket(50000+this.peer.getId());
	}
	
	@Override
	public void run() {
		
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.peer.takeAppropriateAction(inputStream.readLine(), 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
