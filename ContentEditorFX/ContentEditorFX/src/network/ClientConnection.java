package network;

import java.net.Socket;

public class ClientConnection {

	private Socket clientSocket;
	
	public ClientConnection(){
		
	}
	
	public void connect(){
		clientSocket = new Socket();
	}
	
}
