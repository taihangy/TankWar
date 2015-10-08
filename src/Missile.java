import java.awt.Color;
import java.awt.Graphics;

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
	public static final int MISSILE_SPEED_X = 10;
	public static final int MISSILE_SPEED_Y = 10;
	protected int posX, posY;
	protected Direction dir; 
	protected boolean alive;
	protected TankClient tc;
	
	public Missile(int posX, int posY, Direction dir) {
		this.posX = posX;
		this.posY = posY;
		this.dir = dir;
		this.alive = true;
	}
	
	public Missile(int posX, int posY, Direction dir, TankClient tc) {
		this(posX, posY, dir);
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(posX, posY, DIAMETER, DIAMETER);
		g.setColor(c);
		move();
	}
	
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
			tc.missiles.remove(this);
		}
	}

	public static void main(String[] args) {
	}

}

