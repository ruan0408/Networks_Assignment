
public class PingRequest extends Message {

	private static final long serialVersionUID = 2994247948769690505L;
	
	public PingRequest(int senderId, int receiverId) {
		super(senderId, receiverId);
	}
	
	@Override
	public void executeAction(Peer peer) {
		System.out.println("A ping request message was received from Peer "+this.getSenderId());
		peer.updatePredecessors(this.getSenderId());
		Message message = new PingResponse(peer.getId(), this.getSenderId());
		peer.udpClient.sendMessage(message);
	}
}
