import java.awt.Color;
import java.awt.Graphics;

/**
 * @Title: Explode.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author A18ccms A18ccms_gmail_com
 * @date Oct 8, 2015 3:16:44 PM
 * @version V1.0
 */
public class Explode {
	protected int posX, posY;
	protected boolean alive;
	protected TankClient tc;
	protected static int[] diameter = {0, 5, 10, 15, 20, 25, 15, 5, 0};
	protected int step;
	
	
	public Explode(int posX, int posY, TankClient tc) {
		this.posX = posX;
		this.posY = posY;
		this.alive = true;
		this.step = 0;
		this.tc = tc;
	}


	public void draw(Graphics g) {
		if(!alive) {
			tc.explodes.remove(this);
			return;
		}
		if(step == diameter.length) {
			alive = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(posX, posY, diameter[step], diameter[step]);
		step++;
		g.setColor(c);
	}
}

