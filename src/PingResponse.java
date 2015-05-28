
public class PingResponse extends Message {

	private static final long serialVersionUID = 5487675052690073120L;
	
	public PingResponse(int senderId, int receiverId) {
		super(senderId, receiverId);
	}
	
	/*
	 * The receiving peer will print this message upon receival of a PingResponse.
	 */
	@Override
	public void executeAction(Peer receivingPeer) {
		System.out.println("A ping response message was received from Peer "+this.getSenderId());
	}

}
