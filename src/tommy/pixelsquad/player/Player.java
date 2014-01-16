package tommy.pixelsquad.player;

import java.awt.Graphics2D;
import java.awt.Image;

import tommy.pixelsquad.*;

public abstract class Player extends Entity {

<<<<<<< HEAD
	public boolean selected;
=======
	public double x = 0;
	public double y = 0;
	public double w = 32;
	public double h = 48;
	public double hsp, vsp;
	public short dir = 3;

	public double moveSp;

	public boolean selected;

	public Game game;
	public Image[] spriteArray;
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6

	public Player(Game game, Image[] spriteArray, double moveSp) {

		super(game, spriteArray);

		this.moveSp = moveSp;

	}

	public void step() {

		if (selected) {
			if (game.left) {
<<<<<<< HEAD
				Lib.smartMove(this, 2, moveSp);
=======
				x -= moveSp;
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6
				if (!game.right)
					dir = 2;
			}

			if (game.right) {
<<<<<<< HEAD
				Lib.smartMove(this, 0, moveSp);
=======
				x += moveSp;
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6
				if (!game.left)
					dir = 0;
			}

			if (game.down) {
<<<<<<< HEAD
				Lib.smartMove(this, 3, moveSp);
=======
				y += moveSp;
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6
				if (!game.up)
					dir = 3;
			}

			if (game.up) {
<<<<<<< HEAD
				Lib.smartMove(this, 1, moveSp);
=======
				y -= moveSp;
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6
				if (!game.down)
					dir = 1;
			}
		}

	}

	public void draw(Graphics2D g, double[][] zb, double relativeX,
			double relativeY) {

<<<<<<< HEAD
		Lib.drawImageZb(g, zb, spriteArray[dir],
				(int) Math.round(x + visualXOffset),
				(int) Math.round(y + visualYOffset), y + visualYOffset
						+ visualH, (int) Math.round(visualW),
				(int) Math.round(visualH));
=======
		g.drawImage(spriteArray[dir], (int) Math.round(x - relativeX),
				(int) Math.round(y - relativeY), (int) Math.round(w),
				(int) Math.round(h), null);
>>>>>>> 8f0bca70569113ae82b2454981aa93246c6391c6

	}

}