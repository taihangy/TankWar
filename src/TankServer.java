import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket; 
import java.net.SocketException;
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
	public static final int UDP_PORT = 6666;
	protected List<Client> clients = new ArrayList<Client>();
	
	public void start() {
		new Thread(new UDPThread()).start();
		//TCP handshake
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
	
	private class UDPThread implements Runnable {
		
		byte[] buf = new byte[1024];
		
		@Override
		public void run() {
			DatagramSocket ds = null;
			try {
				//UDP Socket--DataSocket
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
System.out.println("UDP thread started at port: " + UDP_PORT);
			while(ds != null) {
				//DatagramPacket used to receive data, buf use to store data
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					//Get datagram packet from client
					ds.receive(dp);
System.out.println("a packet received from client");
					//Forward package to other clients
					for(int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP, c.udpPort));
						ds.send(dp);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
}

