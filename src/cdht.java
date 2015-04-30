
public class cdht {

	public static void main(String[] args) throws Exception {
		int peerId, sucessor1, sucessor2;
		//Scanner scan = new Scanner(System.in);
		
		peerId = Integer.parseInt(args[0]);
		sucessor1 = Integer.parseInt(args[1]);
		sucessor2 = Integer.parseInt(args[2]);
		
		Peer peer = new Peer(peerId, sucessor1, sucessor2);
		peer.run();
	}

}
