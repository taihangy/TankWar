/**
 * @Title: TankClient.java
 * @Package 
 * @Description: Tank Client
 * @author Taihang Ye
 * @date Oct 7, 2015 7:59:43 PM
 * @version V1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TankClient extends Frame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int POSITION_X = 400;
	public static final int POSITION_Y = 300;
	public static final int ENEMY_TANK_NUMBER = 10;
	 
	protected Tank myTank;
	protected ArrayList<Missile> missiles;
	protected ArrayList<Tank> tanks;
	protected ArrayList<Explode> explodes;
	protected Image backScreenImage;
	protected NetClient nc;
	
	public TankClient() {
		missiles = new ArrayList<Missile>();
		myTank = new Tank(50, 50, true, Direction.STOP, this);
		tanks = new ArrayList<Tank>();
		backScreenImage = this.createImage(WIDTH, HEIGHT);
		explodes = new ArrayList<Explode>();
		nc = new NetClient(this);
		launchFrame();
	}
	
	public void paint(Graphics g) {
		g.drawString("Missiles count: " + missiles.size(), WIDTH - 120, 50);
		g.drawString("Explodes count: " + explodes.size(), WIDTH - 120, 70);
		myTank.draw(g);
		for(int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}
		for(int i = 0; i < missiles.size(); i++) {
			Missile missile = missiles.get(i);
			missile.hitTank(myTank);
			missile.draw(g);
		}
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
	
	public void launchFrame() {
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
		//TCP connect to Server
		nc.connect("127.0.0.1", TankServer.TCP_PORT);
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
			myTank.keyPressed(e);
		}
		 
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
	}

}

