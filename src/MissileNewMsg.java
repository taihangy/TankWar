import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Title: MissileNewMsg.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 11, 2015 10:38:30 PM
 * @version V1.0
 */
public class MissileNewMsg extends Msg {
	protected Missile m;
	protected int msgType = Msg.MISSILE_NEW_MSG;
	protected TankClient tc;
	
	public MissileNewMsg(Missile m) {
		this.m = m;
	}
	
	public MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}
	

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(m.tankId);
			dos.writeInt(m.id);
			dos.writeInt(m.posX);
			dos.writeInt(m.posY);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.good);
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
			int tankid = dis.readInt();
			if(tankid == tc.myTank.id) return;
			int id = dis.readInt();
			int posX = dis.readInt();
			int posY = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			Missile m = new Missile(tankid, posX, posY, dir, good, tc);
			m.id = id;
			tc.missiles.add(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

