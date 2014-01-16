package tommy.pixelsquad.player;

import java.awt.Image;

import tommy.pixelsquad.*;

public abstract class Player extends Entity {

	public boolean selected;

	public Player(Room room, Image[] spriteArray, double moveSp) {

		super(room, spriteArray);

		w = 24;
		h = 24;

		visualW = 32;
		visualH = 48;

		visualXOffset = (w - visualW) / 2;
		visualYOffset = h - visualH;

		x = -visualXOffset;
		y = -visualYOffset;

		this.moveSp = moveSp;

	}

	public void step() {

		if (selected) {
			if (room.game.left) {
				Lib.smartMove(this, 2, moveSp);
				if (!room.game.right)
					dir = 2;
			}

			if (room.game.right) {
				Lib.smartMove(this, 0, moveSp);
				if (!room.game.left)
					dir = 0;
			}

			// TODO Fix collision bug
			if (room.game.down) {
				Lib.smartMove(this, 3, moveSp);
				if (!room.game.up)
					dir = 3;
			}

			if (room.game.up) {
				Lib.smartMove(this, 1, moveSp);
				if (!room.game.down)
					dir = 1;
			}
		}

	}

}