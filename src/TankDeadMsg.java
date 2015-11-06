import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Title: TankDeadMsg.java
 * @Package 
 * @Description: Tank Dead Message
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 12, 2015 11:01:36 AM
 * @version V1.0
 */
public class TankDeadMsg extends Msg {
	protected int msgType = TANK_DEAD_MSG;
	protected TankClient tc;
	protected int id;
	
	public TankDeadMsg(int id) {
		this.id = id;
	}
	
	public TankDeadMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt(); 
			if(id == tc.myTank.id) return;
			for(int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if(id == t.id) {
					t.alive = false;
					break;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

