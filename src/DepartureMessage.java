
public class DepartureMessage extends Message {

	private static final long serialVersionUID = -1271779855124559179L;
	private int firstSucessor;
	private int secondSucessor;
	
	public DepartureMessage(int senderId, int receiverId, int sucessor1, int sucessor2) {
		super(senderId, receiverId);
		firstSucessor = sucessor1;
		secondSucessor = sucessor2;
	}
	
	@Override
	public int getFirstSucessor() {
		return firstSucessor;
	}
	
	@Override
	public int getSecondSucessor() {
		return secondSucessor;
	}

	/*
	 * When a peer wants to leave, it will send this message to it's predecessors.
	 * The receiving peers will execute this action and update it's sucessors.
	 */
	@Override
	public void executeAction(Peer receivingPeer) {
		System.out.println("Peer "+super.getSenderId()+" will depart from the network.");
		receivingPeer.updateSucessors(super.getSenderId(), getFirstSucessor(), getSecondSucessor());
		
		System.out.println("My first sucessor is now peer "+
				receivingPeer.getFirstSucessor()+".");
		System.out.println("My second sucessor is now peer "+
				receivingPeer.getSecondSucessor()+".");
	}
}
