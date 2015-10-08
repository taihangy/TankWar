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
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int POSITION_X = 400;
	public static final int POSITION_Y = 300;
	
	
	Tank myTank = new Tank(50, 50, this);
	Missile missile = null;
	private Image backScreenImage = null;
	
	public void paint(Graphics g) {
		myTank.draw(g);
		if(missile != null) missile.draw(g);
	}
	
	//Double buffer and background color
	//Repaint would call update override which would call draw
	public void update(Graphics g) {
		if(backScreenImage == null) {
			backScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics gBackScreen = backScreenImage.getGraphics();
		//Get color and draw image background color
		Color c = gBackScreen.getColor();
		gBackScreen.setColor(Color.GREEN);
		gBackScreen.fillRect(0, 0, WIDTH, HEIGHT);
		gBackScreen.setColor(c);
		paint(gBackScreen);
		g.drawImage(backScreenImage, 0, 0, null);
	}
	
	public void launchFram() {
		this.setLocation(POSITION_X, POSITION_Y);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		// multi-thread start
		new Thread(new PaintThread()).start();
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
//			System.out.println("ok");
			myTank.keyPressed(e);
		}
		 
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFram();
	}

}

