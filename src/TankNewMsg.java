import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Title: TankNewMsg.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 9, 2015 7:44:52 PM
 * @version V1.0
 */
public class TankNewMsg {
	protected Tank tank;
	protected TankClient tc;
	
	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}
	
	public TankNewMsg(TankClient tc){
		this.tc = tc;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(tank.id);
			dos.writeInt(tank.posX);
			dos.writeInt(tank.posY);
			dos.writeInt(tank.dir.ordinal());
			dos.writeBoolean(tank.good);
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
			boolean good = dis.readBoolean();
//System.out.println("id: " + id + "-x: " + posX + "-y: " + posY + "-dir: " + dir + "-good: " + good);
			Tank t = new Tank(posX, posY, good, dir, tc);
			t.id = id;
			tc.tanks.add(t);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

