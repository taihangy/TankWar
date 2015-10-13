import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 * @Title: Missile.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 7, 2015 9:56:16 PM
 * @version V1.0
 */
public class Missile {
	public static final int DIAMETER = 10;
	public static final int MISSILE_SPEED_X = 15;
	public static final int MISSILE_SPEED_Y = 15;
	protected int posX, posY;
	protected Direction dir; 
	protected boolean alive;
	protected boolean good;
	protected TankClient tc;
	protected int tankId;
	protected static int ID = 1;
	protected int id;
	
	
	public Missile(int tankId, int posX, int posY, Direction dir) {
		this.tankId = tankId;
		this.posX = posX;
		this.posY = posY;
		this.dir = dir;
		this.alive = true;
		this.id = ID++;
	}
	
	public Missile(int tankid, int posX, int posY, Direction dir, boolean good, TankClient tc) {
		this(tankid, posX, posY, dir);
		this.tc = tc;
		this.good = good;
	}
	
	public void draw(Graphics g) {
		if(!alive) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(posX, posY, DIAMETER, DIAMETER);
		g.setColor(c);
		move();
	}
	
	public boolean hitTank(Tank t) {
		if(t.good != this.good && t.alive && getRect().intersects(t.getRect())){
			t.alive = false;
			alive = false;
			Explode e = new Explode(posX, posY, true, 0, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this.hitTank(t)) return true;
		}
		return false;
	}
	
	public Rectangle getRect() {
		return new Rectangle(posX, posY, DIAMETER, DIAMETER);
	}
	
	/** ========
	 *  =helper=
	 *  ========
	 */
	private void move() {
		switch(dir) {
		case L: 
			posX -= MISSILE_SPEED_X; 
			break;
		case LU: 
			posX -= MISSILE_SPEED_X; 
			posY -= MISSILE_SPEED_Y;
			break;
		case U: 
			posY -= MISSILE_SPEED_Y; 
			break;
		case RU: 
			posX += MISSILE_SPEED_X;
			posY -= MISSILE_SPEED_Y;
			break;
		case R: 
			posX += MISSILE_SPEED_X; 
			break;
		case RD: 
			posX += MISSILE_SPEED_X; 
			posY += MISSILE_SPEED_Y;
			break;
		case D: 
			posY += MISSILE_SPEED_Y; 
			break;
		case LD: 
			posX -= MISSILE_SPEED_X; 
			posY += MISSILE_SPEED_Y;
			break;
		default:
			break;
		}
		
		if(posX < 0 || posY < 0 || 
		   posX > TankClient.WIDTH - DIAMETER / 2 || posY > TankClient.HEIGHT - DIAMETER / 2) {
			alive = false;
		}
	}
}

