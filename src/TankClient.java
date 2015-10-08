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
	public static final int TANK_SPEED_X = 5;
	public static final int TANK_SPEED_Y = 5;
	
	private int x = 50, y = 50;
	private Image backScreenImage = null;
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);
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

		public void keyPressed(KeyEvent e) {
//			System.out.println("ok");
			int key = e.getKeyCode();
			switch(key){
			case KeyEvent.VK_RIGHT: x += TANK_SPEED_X; break;
			case KeyEvent.VK_LEFT: x -= TANK_SPEED_X; break;
			case KeyEvent.VK_UP: y -= TANK_SPEED_Y; break;
			case KeyEvent.VK_DOWN: y += TANK_SPEED_Y; break;
			}
		}
		 
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFram();
	}

}

