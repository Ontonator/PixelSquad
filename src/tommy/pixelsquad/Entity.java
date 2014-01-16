package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Entity {

	public double x, y;
	public double w, h;

	public double visualW, visualH;
	public double visualXOffset, visualYOffset;

	public short dir = 3;

	public double moveSp;

	public Game game;
	public Image[] spriteArray;

	public Entity(Game game, Image[] spriteArray) {

		this.game = game;
		this.spriteArray = spriteArray;

	}

	public void draw(Graphics2D g, double[][] zb, double relativeX,
			double relativeY) {

		Lib.drawImageZb(g, zb, spriteArray[dir],
				(int) Math.round(x + visualXOffset),
				(int) Math.round(y + visualYOffset), y + visualYOffset
						+ visualH, (int) Math.round(visualW),
				(int) Math.round(visualH));

	}

}