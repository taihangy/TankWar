import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * @Title: Tank.java
 * @Package 
 * @Description: Tank Object
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 7, 2015 9:23:12 PM
 * @version V1.0
 */
public class Tank {
	protected int posX, posY;
	protected boolean bL, bR, bU, bD;
	protected TankClient tc;
	protected Direction dir;
	protected Direction ptDir = Direction.D;
	protected boolean good;
	protected boolean alive;
	protected Direction[] dirs = Direction.values();
	protected int step = r.nextInt(12) + 3;;
	public static final int TANK_SPEED_X = 10;
	public static final int TANK_SPEED_Y = 10;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	private static Random r = new Random();
	
	public Tank(int posX, int posY, boolean good) {
		assert posX >= 0 && posY >= 0;
		this.posX = posX;
		this.posY = posY;
		this.good = good;
		this.alive = true;
	}
	
	public Tank(int posX, int posY, boolean good, Direction dir, TankClient tc) {
		this(posX, posY, good);
		assert tc != null;
		this.tc = tc;
		this.dir = dir;
	}
	
	public void draw(Graphics g) {
		if(!alive) {
			if(!good) tc.enemies.remove(this);
			return;
		}
		drawTankBody(g);
		drawPT(g);
		move();
	}

	public Rectangle getRect() {
		return new Rectangle(posX, posY, TANK_WIDTH, TANK_HEIGHT);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_RIGHT: bR = true; break;
		case KeyEvent.VK_LEFT: bL = true; break;
		case KeyEvent.VK_UP: bU = true; break;
		case KeyEvent.VK_DOWN: bD = true; break;
		}
		locateDir();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_RIGHT: bR = false; break;
		case KeyEvent.VK_LEFT: bL = false; break;
		case KeyEvent.VK_UP: bU = false; break;
		case KeyEvent.VK_DOWN: bD = false;; break;
		case KeyEvent.VK_CONTROL: fire(); break;
		}
		locateDir();
	}
	
	/** ======== 
	 *  =helper=
	 *  ========
	 * */
	
	private void drawPT(Graphics g) {
		switch(ptDir) {
		case L: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX, posY + TANK_HEIGHT / 2);
			break;
		case LU: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX, posY);
			break;
		case U: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX + TANK_WIDTH / 2, posY);
			break;
		case RU: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX + TANK_WIDTH, posY);
			break;
		case R: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX + TANK_WIDTH, posY + TANK_HEIGHT / 2);
			break;
		case RD: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX + TANK_WIDTH, posY + TANK_HEIGHT);
			break;
		case D: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX + TANK_WIDTH / 2, posY + TANK_HEIGHT);
			break;
		case LD: 
			g.drawLine(posX + TANK_WIDTH / 2, posY + TANK_HEIGHT / 2, 
					   posX, posY + TANK_HEIGHT);
			break;
		
		}
	}

	private void drawTankBody(Graphics g) {
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(posX, posY, TANK_WIDTH, TANK_HEIGHT);
		g.setColor(c);
	}
	
	private void locateDir() {
		if(bL && !bR && !bU && !bD) dir = Direction.L;
		else if(bL && !bR && bU && !bD) dir = Direction.LU;
		else if(!bL && !bR && bU && !bD) dir = Direction.U;
		else if(!bL && bR && bU && !bD) dir = Direction.RU;
		else if(!bL && bR && !bU && !bD) dir = Direction.R;
		else if(!bL && bR && !bU && bD) dir = Direction.RD;
		else if(!bL && !bR && !bU && bD) dir = Direction.D;
		else if(bL && !bR && !bU && bD) dir = Direction.LD;
		else dir = Direction.STOP;
	}
	
	private Missile fire() {
		if(!alive) return null;
		Missile m = new Missile(posX + TANK_WIDTH / 2 - Missile.DIAMETER / 2, 
								posY + TANK_HEIGHT / 2 - Missile.DIAMETER / 2, ptDir, good, tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 
	 * @Title: move
	 * @Description: Tank moving
	 * @param @param dir move with this direction 
	 * @return void    
	 * @throws
	 */
	private void move() {
		if(!good) {
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40) > 35) fire();
		}
		switch(dir) {
		case L: 
			posX -= TANK_SPEED_X; 
			break;
		case LU: 
			posX -= TANK_SPEED_X; 
			posY -= TANK_SPEED_Y;
			break;
		case U: 
			posY -= TANK_SPEED_Y; 
			break;
		case RU: 
			posX += TANK_SPEED_X;
			posY -= TANK_SPEED_Y;
			break;
		case R: 
			posX += TANK_SPEED_X; 
			break;
		case RD: 
			posX += TANK_SPEED_X; 
			posY += TANK_SPEED_Y;
			break;
		case D: 
			posY += TANK_SPEED_Y; 
			break;
		case LD: 
			posX -= TANK_SPEED_X; 
			posY += TANK_SPEED_Y;
			break;
		case STOP: break;
		}
		if(dir != Direction.STOP) {
			ptDir = dir;
		}
		if(posX < 0) posX = 0;
		if(posY < 21) posY = 21;
		if(posX > TankClient.WIDTH - TANK_WIDTH) posX = TankClient.WIDTH - TANK_WIDTH;
		if(posY > TankClient.HEIGHT - TANK_HEIGHT) posY = TankClient.HEIGHT - TANK_HEIGHT;
	}
}

