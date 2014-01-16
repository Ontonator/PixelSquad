package tommy.pixelsquad.player;

import java.awt.Graphics2D;
import java.awt.Image;

import tommy.pixelsquad.*;

public abstract class Player extends Entity {

	public boolean selected;

	public Player(Game game, Image[] spriteArray, double moveSp) {

		super(game, spriteArray);

		this.moveSp = moveSp;

	}

	public void step() {

		if (selected) {
			if (game.left) {
				Lib.smartMove(this, 2, moveSp);
				x -= moveSp;
				if (!game.right)
					dir = 2;
			}

			if (game.right) {
				Lib.smartMove(this, 0, moveSp);
				x += moveSp;
				if (!game.left)
					dir = 0;
			}

			if (game.down) {
				Lib.smartMove(this, 3, moveSp);
				y += moveSp;
				if (!game.up)
					dir = 3;
			}

			if (game.up) {
				Lib.smartMove(this, 1, moveSp);
				y -= moveSp;
				if (!game.down)
					dir = 1;
			}
		}

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