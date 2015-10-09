import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
	private static int UDP_PORT_START = 2223;
	protected int udpPort;
	
	public NetClient() {
		udpPort = UDP_PORT_START++;
	}
	
	public void connect(String IP, int port) {
		Socket s = null;
		try {
			s = new Socket(IP, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			s.close();
System.out.println("Connected to server");
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}

