import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

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
	protected Direction dir = Direction.STOP;
	public static final int TANK_SPEED_X = 5;
	public static final int TANK_SPEED_Y = 5;
	
	public Tank(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(posX, posY, 30, 30);
		g.setColor(c);
		move(dir);
	}
	
	private void move(Direction dir) {
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
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_RIGHT: bR = true; break;
		case KeyEvent.VK_LEFT: bL = true; break;
		case KeyEvent.VK_UP: bU = true; break;
		case KeyEvent.VK_DOWN: bD = true;; break;
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
		}
		locateDir();
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}

