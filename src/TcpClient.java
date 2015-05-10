import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient extends Thread {

	private Peer peer;
	
	public TcpClient(Peer peer) throws IOException {
		this.peer = peer;
	}
	
	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		String userRequest;
		String[] aux;
		int file;
		
		while(scan.hasNextLine()) {
			userRequest = scan.nextLine().trim();
			if(userRequest.matches("request \\d+")){
				aux = userRequest.split("\\s+");
				file = Integer.parseInt(aux[1]);
				
				Message request = new FileRequest(peer.getId(), peer.getFirstSucessor(), file);
				sendMessage(request);
				System.out.println("File request message for "+file+" has been sent to my sucessor");
			}
			else if(userRequest.equals("quit")) {
				for(int predecessor : peer.predecessors) {
					Message departure = new DepartureMessage(peer.getId(), predecessor, peer.getFirstSucessor(), peer.getSecondSucessor());
					sendMessage(departure);
				}
				peer.quit();
			}
		}
		scan.close();
	}
	
	public void sendMessage(Message message) {
		try {
			int peerPort = Peer.toPort(message.getReceiverId());
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), peerPort));
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(message);
			socket.close();
		} catch (Exception e) {
			System.out.println("shit at TCP CLIENT SEND MESSSAGE");
			e.printStackTrace();
		}
	}
}
