
public class PingRequest extends Message {

	private static final long serialVersionUID = 2994247948769690505L;
	private int firstSucessor; //first sucessor of the peer that is sending this message
	
	public PingRequest(int senderId, int receiverId, int firstSucessor) {
		super(senderId, receiverId);
		this.firstSucessor = firstSucessor;
	}
	
	@Override
	public int getFirstSucessor() {
		return firstSucessor;
	}
	
	@Override
	public void executeAction(Peer peer) {
		System.out.println("A ping request message was received from Peer "+super.getSenderId());
		peer.updatePredecessors(getSenderId(), getFirstSucessor());
		Message message = new PingResponse(peer.getId(), super.getSenderId());
		peer.udpClient.sendMessage(message);
	}
}
