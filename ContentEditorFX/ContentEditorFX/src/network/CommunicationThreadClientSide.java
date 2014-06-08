package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import javafx.application.Platform;
import javafx.concurrent.Task;
import settings.GlobalAppSettings;

public class CommunicationThreadClientSide{

	@SuppressWarnings("unused")
	private int port;
	@SuppressWarnings("unused")
	private int welcomePort;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private ArrayList<NetworkPackage> receiveQueue;
	private Task<Void> receiveThread;
	
	private ExecutorService pool;
	
	private boolean isAlive = false;
	
	public CommunicationThreadClientSide(Socket socket){
		this.socket = socket;
		this.port = GlobalAppSettings.defaultServerPort;
		receiveQueue = new ArrayList<NetworkPackage>();
		pool = Executors.newCachedThreadPool();
		isAlive = true;
		
		System.out.println("initialized connection on client side");
		this.setup();
	}
	
	public void disconnect() {
		isAlive = false;
	}
	
	public void send(NetworkPackage pack){
		try {
			oos.writeObject(pack);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkPackage receive() {
		NetworkPackage pack = receiveQueue.get(receiveQueue.size()-1);
		receiveQueue.remove(receiveQueue.size()-1);
		return pack;
	}
	
	private void setup(){
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.flush();
			
			receiveThread = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					NetworkPackage pack = null;
					while(isAlive) {
						try {
							checkConnection();
							System.out.println("receive client");
							pack = (NetworkPackage) ois.readObject();
							receiveQueue.add(pack);
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			};
			
			pool.submit(receiveThread);
		} catch (Exception e) {
			System.out.println("Caught exception");
			e.printStackTrace();
		}
	}

	protected void checkConnection() {
		if(socket.isClosed() || socket.isConnected() == false || socket.isInputShutdown() || socket.isOutputShutdown()) {
			isAlive = false;
		}
	}

	public boolean isAlive() {
		checkConnection();
		return isAlive;
	}
	
}
