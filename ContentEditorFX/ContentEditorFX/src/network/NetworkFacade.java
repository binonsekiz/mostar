package network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkFacade {
	
	public static NetworkFacade instance = null;

	private CommunicationThreadClientSide serverConnection;
	private ArrayList<CommunicationThreadClientSide> peerConnections;
	private NetworkPackage pack;
	
	public NetworkFacade() {
		if(instance != null){
			throw new RuntimeException("Multiple Network Facades initialized");
		}
		instance = this;
		peerConnections = new ArrayList<CommunicationThreadClientSide>();
		pack = new NetworkPackage();
	}
	
	public CommunicationThreadClientSide ensureServerConnection() {
		if(serverConnection == null || !serverConnection.isAlive())
			openServerConnection();
		return serverConnection;
	}
	
	private void openServerConnection() {
		try {
			Socket client = new Socket("localhost", 1018);
			serverConnection = new CommunicationThreadClientSide(client);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if(e instanceof ConnectException) {
				System.out.println("Connection refused");
			}
		}
	}
	
	public void establishInitialConnection(NetworkListener listener) {
		ensureServerConnection();
		if(isConnectedToServer()){
			listener.notifyWithNetworkEvent(NetworkEventType.EstablishConnection);
		}
	}
	
	public void tryLogin(String username, String password, NetworkListener listener) {
		ensureServerConnection();
		System.out.println("Login try");
		pack.clear();
		pack.setType(NetworkPackageType.LoginRequest);
		pack.setData("Username " + username + ", password: " + password);
		serverConnection.send(pack);
	}
	
	public boolean isConnectedToServer() {
		if(serverConnection == null) return false;
		return serverConnection.isAlive();
	}
	
	public void checkMessage() {
		
	}
	
}
