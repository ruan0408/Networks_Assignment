import java.io.IOException;
import java.io.PrintWriter;
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
		int fileNumber;
		while(scan.hasNextLine()) {
			userRequest = scan.nextLine();
			if(userRequest.trim().matches("request \\d+")){
				aux = userRequest.split("\\s+");
				fileNumber = Integer.parseInt(aux[1].trim());
				userRequest = String.join(" ", aux).trim();
				
				this.sendMessage(userRequest+" "+this.peer.getId(), this.peer.getFirstSucessor()+50000);
				System.out.println("File request message for "+fileNumber+" has been sent to my sucessor");
			}
			else if(userRequest.trim().equals("quit")) {
				for(int predecessor : this.peer.predecessors) {
					this.sendMessage("QUIT "+this.peer.getId()+" "+this.peer.getFirstSucessor()+" "
							+this.peer.getSecondSucessor(), 50000+predecessor);
				}
				this.peer.quit();
			}
			
		}
		scan.close();
	}
	
	public void sendMessage(String message, int peerPort) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), peerPort));
			PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
			outputStream.println(message);
			socket.close();
		} catch (Exception e) {
			System.out.println("shit at TCP CLIENT SEND MESSSAGE");
			e.printStackTrace();
		}
	}
}
