package tommy.pixelsquad.player;

import java.awt.Image;

import tommy.pixelsquad.*;

public abstract class Player extends Entity {

	public boolean selected;

	public Player(Game game, Image[] spriteArray, double moveSp) {

		super(game, spriteArray);

		w = 24;
		h = 24;

		visualW = 32;
		visualH = 48;

		visualXOffset = (w - visualW) / 2;
		visualYOffset = (h - visualH) / 2 - 16;

		x = -visualXOffset;
		y = -visualYOffset;

		this.moveSp = moveSp;

	}

	public void step() {

		if (selected) {
			if (game.left) {
				Lib.smartMove(this, 2, moveSp);
				if (!game.right)
					dir = 2;
			}

			if (game.right) {
				Lib.smartMove(this, 0, moveSp);
				if (!game.left)
					dir = 0;
			}

			if (game.down) {
				Lib.smartMove(this, 3, moveSp);
				if (!game.up)
					dir = 3;
			}

			if (game.up) {
				Lib.smartMove(this, 1, moveSp);
				if (!game.down)
					dir = 1;
			}
		}

	}

}