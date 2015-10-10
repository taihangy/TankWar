import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * @Title: NetClient.java
 * @Package 
 * @Description: Net Client for Tank Client
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 8, 2015 9:29:32 PM
 * @version V1.0
 */
public class NetClient {
	private static int UDP_PORT_START = 2224;
	protected int udpPort;
	protected TankClient tc;
	protected DatagramSocket ds;
	
	public NetClient(TankClient tc) {
		udpPort = UDP_PORT_START++;
		this.tc = tc;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(String IP, int port) {
		Socket s = null;
		try {
			s = new Socket(IP, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			//Get ID from server and save it into myTank.id
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
System.out.println("Connected to server! and server give me a ID: " + id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		//udp package
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		
		new Thread(new UDPReceiveThread()).start();
	}
	
	public void send(TankNewMsg msg) {
		 msg.send(ds, "127.0.0.1", TankServer.UDP_PORT);
	}
	
	private class UDPReceiveThread implements Runnable {
		byte[] buf = new byte[1024];

		public void run() {
			while(ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
System.out.println("a packet received from server");
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			//内部类访问封装类的对象
			TankNewMsg msg = new TankNewMsg(NetClient.this.tc);
			msg.parse(dis);
		}
	}
}

