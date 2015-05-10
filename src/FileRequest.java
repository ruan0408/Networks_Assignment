
public class FileRequest extends Message {

	private static final long serialVersionUID = 1797075951286448858L;
	private int file;
	
	public FileRequest(int senderId, int receiverId, int file) {
		super(senderId, receiverId);
		this.file = file;
	}
	
	@Override
	public int getFile() {
		return file;
	}
	
	@Override
	public void executeAction(Peer peer) { // Peer that is receiving this message
		int hash = Peer.hash(file);
		if(((peer.getId() < peer.getFirstPredecessor()) &&((hash <= peer.getId()) || (hash > peer.getFirstPredecessor()))) ||
				((hash > peer.getFirstPredecessor()) && (hash <= peer.getId()))) {

			System.out.println("File "+file+" is here.");
			Message fileResponse = new FileResponse(peer.getId(), this.getSenderId(), file);
			peer.tcpClient.sendMessage(fileResponse);
			System.out.println("A response message, destined for peer "+this.getSenderId()+", has been sent.");
		}
		else {
			System.out.println("File "+file+" is not here.");
			System.out.println("File request message has been forwarded to my sucessor.");
			this.setReceiverId(peer.getFirstSucessor());
			peer.tcpClient.sendMessage(this);
		}

	}
}
