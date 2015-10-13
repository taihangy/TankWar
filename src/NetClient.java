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
	protected int udpPort;
	protected TankClient tc;
	protected DatagramSocket ds;
	
	public NetClient(TankClient tc) {
		this.tc = tc;
	}
	
	public void connect(String IP, int port) {
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		Socket s = null;
		try {
			s = new Socket(IP, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			//Get ID from server and save it into myTank.id
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			if(id % 2 == 0) tc.myTank.good = false;
			else tc.myTank.good = true; 
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
		//After TCP handshake, send tank new udp package
		Msg msg = new TankNewMsg(tc.myTank);
		send(msg);
		
		new Thread(new UDPReceiveThread()).start();
	}
	
	public void send(Msg msg) {
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
			try {
				int msgType = dis.readInt();
				Msg msg = null;
				switch(msgType) {
				case Msg.TANK_NEW_MSG:
					//内部类访问封装类的对象可以用这种方法
					msg = new TankNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.TANK_MOVE_MSG:
					msg = new TankMoveMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.MISSILE_NEW_MSG:
					msg = new MissileNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.TANK_DEAD_MSG:
					msg = new TankDeadMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.MISSILE_DEAD_MSG:
					msg = new MissileDeadMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

