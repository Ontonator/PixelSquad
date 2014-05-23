package tommy.pixelsquad.player;

import java.awt.Image;

import tommy.pixelsquad.*;

public abstract class Player extends Entity {

	public boolean selected;

	public Player(Room room, Image[][] spriteArray, double moveSp) {

		super(room, spriteArray);

		w = 16;
		h = 16;

		visualW = 32;
		visualH = 48;

		visualXOffset = (w - visualW) / 2;
		visualYOffset = h - visualH - 6;

		x = -visualXOffset;
		y = -visualYOffset;

		this.moveSp = moveSp;

	}

	public void step() {

		super.step();

		if (selected
				&& (room.game.left || room.game.right || room.game.down || room.game.up)) {
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

			// cycleFrame();

		} else

			frame = 0;

	}

}