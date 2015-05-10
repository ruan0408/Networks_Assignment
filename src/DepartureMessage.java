
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

	@Override
	public void executeAction(Peer receivingPeer) {
		System.out.println("Peer "+this.getSenderId()+" will depart from the network.");
		receivingPeer.updateSucessors(this.getSenderId(), this.getFirstSucessor(), this.getSecondSucessor());
	}

}
