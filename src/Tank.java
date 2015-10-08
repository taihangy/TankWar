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
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_RIGHT: posX += TANK_SPEED_X; break;
		case KeyEvent.VK_LEFT: posX -= TANK_SPEED_X; break;
		case KeyEvent.VK_UP: posY -= TANK_SPEED_Y; break;
		case KeyEvent.VK_DOWN: posY += TANK_SPEED_Y; break;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

