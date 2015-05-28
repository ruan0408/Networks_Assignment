
public class FileResponse extends Message {

	private static final long serialVersionUID = -7833468379039593572L;	
	private int file;		//file that was requested
	
	public FileResponse(int senderId, int receiverId, int file) {
		super(senderId, receiverId); //peer that has the file and peer that requested the file
		this.file = file;
	}

	@Override
	public int getFile() {return file;}

	/*
	 * The peer that originally made the file request will display this message upon receival of a FileResponse
	 */
	@Override
	public void executeAction(Peer receivingPeer) {
		System.out.println("Received a response message from peer "+getSenderId()+", which has the file "+file+".");
	}
}
