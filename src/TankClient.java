/**
 * @Title: TankClient.java
 * @Package 
 * @Description: Tank Client
 * @author Taihang Ye
 * @date Oct 7, 2015 7:59:43 PM
 * @version V1.0
 */

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

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
	protected ConnDialog dialog = new ConnDialog();
	protected static Random r = new Random();
	
	public TankClient() {
		missiles = new ArrayList<Missile>();
		myTank = new Tank(r.nextInt(WIDTH), r.nextInt(HEIGHT), true, Direction.STOP, this);
		tanks = new ArrayList<Tank>();
		backScreenImage = this.createImage(WIDTH, HEIGHT);
		explodes = new ArrayList<Explode>();
		nc = new NetClient(this);
		launchFrame();
	}
	
	public void paint(Graphics g) {
		g.drawString("Press c in keyboard to connect to server", WIDTH - 120, 50);
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
			if(missile.hitTank(myTank)) {
				TankDeadMsg msg = new TankDeadMsg(myTank.id);
				nc.send(msg);
				MissileDeadMsg mdmmsg = new MissileDeadMsg(missile.tankId, missile.id);
				nc.send(mdmmsg);
			}
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
		// paint-thread start
		Thread paintThread = new Thread(new PaintThread(), "Paint thread");
		paintThread.start();
		//TCP connect to Server
		//nc.connect("127.0.0.1", TankServer.TCP_PORT);
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
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_C) {
				dialog.setVisible(true);
			} else {
				myTank.keyPressed(e);
			}
		}
		 
	}
	
	
	private class ConnDialog extends Dialog {
		Button b = new Button("confirm");
		TextField ip = new TextField("127.0.0.1", 12);
		TextField tcpPort = new TextField("" + TankServer.TCP_PORT, 4);
		TextField udpPort = new TextField("2223", 4);
		
		public ConnDialog() {
			super(TankClient.this, true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP"));
			this.add(ip);
			this.add(new Label("Port:"));
			this.add(tcpPort);
			this.add(new Label("My UDP Port:"));
			this.add(udpPort);
			this.add(b);
			this.setLocation(500, 500);
			this.pack();
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String IP = ip.getText();
					int tcp = Integer.parseInt(tcpPort.getText().trim());
					int udp = Integer.parseInt(udpPort.getText().trim());
					nc.udpPort = udp;
					nc.connect(IP, tcp);
					setVisible(false);
				}
			});
		}
		
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
	}

}

