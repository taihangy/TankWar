/**
 * @Title: TankClient.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 7, 2015 7:59:43 PM
 * @version V1.0
 */

import java.awt.*;
import java.awt.event.*;
public class TankClient extends Frame {
	
	public void launchFram() {
		this.setLocation(400, 300);
		this.setSize(800, 600);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		this.setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFram();
	}

}

