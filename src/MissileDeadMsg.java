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
public class MissileDeadMsg extends Msg {
	protected int msgType = MISSILE_DEAD_MSG;
	protected TankClient tc;
	protected int id;
	protected int tankId;
	
	public MissileDeadMsg(int tankId, int id) {
		this.id = id;
		this.tankId = tankId;
	}
	
	public MissileDeadMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tankId);
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
			int tankId = dis.readInt(); 
			int id = dis.readInt();
			for(int i = 0; i < tc.missiles.size(); i++) {
				Missile m = tc.missiles.get(i);
				if(tankId == m.tankId && id == m.id) {
					m.alive = false;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

