import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Title: TankMoveMsg.java
 * @Package 
 * @Description: Tank move message
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 9, 2015 10:09:51 PM
 * @version V1.0
 */
public class TankMoveMsg extends Msg{
	protected int msgType = TANK_MOVE_MSG;
	protected int id;
	protected int posX;
	protected int posY;
	protected Direction dir;
	protected Direction ptDir;
	protected TankClient tc;
	
	public TankMoveMsg(int id, int posX, int posY, Direction dir, Direction ptDir) {
		this.id = id;
		this.dir = dir;
		this.posX = posX;
		this.posY = posY;
		this.ptDir = ptDir;
	}

	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(posX);
			dos.writeInt(posY);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
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
			int posX = dis.readInt();
			int posY = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			Direction ptDir = Direction.values()[dis.readInt()];
			for(int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if(id == t.id) {
					t.dir = dir;
					t.ptDir = ptDir;
					t.posX = posX;
					t.posY = posY;
					break;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

