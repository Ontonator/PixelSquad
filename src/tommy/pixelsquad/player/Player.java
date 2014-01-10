package tommy.pixelsquad.player;

import java.awt.Graphics2D;
import java.awt.Image;

import tommy.pixelsquad.Game;

public abstract class Player {

	public double x = 0;
	public double y = 0;
	public double hsp, vsp;
	public short dir = 3;

	public double moveSp;

	public Game game;
	public Image[] spriteArray;

	public Player(Game game, Image[] spriteArray, double moveSp) {

		this.game = game;
		this.spriteArray = spriteArray;
		this.moveSp = moveSp;

	}

	public void step() {

		if (game.left) {
			x -= moveSp;
			if (!game.right)
				dir = 2;
		}

		if (game.right) {
			x += moveSp;
			if (!game.left)
				dir = 0;
		}

		if (game.down) {
			y += moveSp;
			if (!game.up)
				dir = 3;
		}

		if (game.up) {
			y -= moveSp;
			if (!game.down)
				dir = 1;
		}

	}

	public void draw(Graphics2D g, double relativeX, double relativeY) {

		g.drawImage(spriteArray[dir], (int) Math.round(x - relativeX),
				(int) Math.round(y - relativeY),
				spriteArray[dir].getWidth(null) * 2,
				spriteArray[dir].getHeight(null) * 2, null);

	}

}