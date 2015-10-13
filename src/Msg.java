import java.io.DataInputStream;
import java.net.DatagramSocket;

/**
 * @Title: Msg.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 9, 2015 10:16:23 PM
 * @version V1.0
 */
public abstract class Msg {
	public static final int TANK_NEW_MSG = 1;
	public static final int TANK_MOVE_MSG = 2;
	public static final int TANK_DEAD_MSG = 4;
	public static final int MISSILE_NEW_MSG = 3;
	
	public abstract void send(DatagramSocket ds, String IP, int udpPort);
	public abstract void parse(DataInputStream dis);
}

