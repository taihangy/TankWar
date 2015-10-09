import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: TankServer.java
 * @Package 
 * @Description: Tank Sever
 * @author Taihang Ye
 * @date Oct 8, 2015 9:21:57 PM
 * @version V1.0
 */
public class TankServer {
	private static int ID = 100; //Tank Unique ID (or we can use java UUID)
	public static final int TCP_PORT = 8888;
	protected List<Client> clients = new ArrayList<Client>();
	
	public void start() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		while(true) {
			Socket s = null;
			try {
				//If we use multi-thread to handle several client we should be aware
				//of synchronize issue because ID may be the same
				s = ss.accept();
				//Save IP and udpPort into Client list
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int updPort = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				Client c = new Client(IP, updPort);
				clients.add(c);
				//Write stream ID
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
	System.out.println("A Client Connect! Addr: " + s.getInetAddress() + ":" + s.getPort() + "---UDP Port:" + updPort);
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(s != null) {
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}
	
	private class Client {
		private String IP;
		private int udpPort;
		
		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}
}

