package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Entity {

	public double x, y;
	public double w, h;

	public double visualW, visualH;
	public double visualXOffset, visualYOffset;

	public short dir = 3;
	public short frame;
	public int frameCooldown;

	public double moveSp;

	public Room room;
	public Image[][] spriteArray;

	public Entity(Room room, Image[][] spriteArray) {

		this.room = room;
		this.spriteArray = spriteArray;

	}

	public void step() {

		if (frameCooldown > 0)
			frameCooldown--;

	}

	public void draw(Graphics2D g, double[][] zb, double relativeX,
			double relativeY) {

		if (Lib.checkCollisionBB(x + visualXOffset, y + visualYOffset,
				relativeX, relativeY, visualW, visualH, Game.WIDTH, Game.HEIGHT))
			Lib.drawImageZb(g, zb, spriteArray[dir][frame == 0 ? 0
					: (frame == 1 ? 1 : (frame == 2 ? 0 : 2))], (int) Math
					.round(x + visualXOffset - relativeX), (int) Math.round(y
					+ visualYOffset - relativeY), y + visualYOffset + visualH,
					(int) Math.round(visualW), (int) Math.round(visualH));

	}

	protected void cycleFrame() {

		if (frameCooldown <= 0) {

			frame++;

			if (frame > 3)
				frame = 0;

			frameCooldown = 10;

		}

	}

}