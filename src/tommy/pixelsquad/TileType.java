package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;

public class TileType {

	public double w, h;

	public Image sprite;

	public boolean solid;
	public boolean extruding;

	public TileType(Image sprite, boolean solid, boolean extruding, double w,
			double h) {

		this.sprite = sprite;

		this.solid = solid;
		this.extruding = extruding;

		this.w = w;
		this.h = h;

	}

	public class Tile {

		public double x, y;

		public Tile(double x, double y) {

			this.x = x;
			this.y = y;

		}

		public void draw(Graphics2D g, double[][] zb, double relativeX,
				double relativeY) {

			if (Lib.checkCollisionBB(x, y, relativeX, relativeY, w, h,
					Game.WIDTH, Game.HEIGHT))
				if (extruding)
					Lib.drawImageZb(g, zb, sprite,
							(int) Math.round(x - relativeX),
							(int) Math.round(y - relativeY), y + h,
							(int) Math.round(w), (int) Math.round(h));
				else
					g.drawImage(sprite, (int) Math.round(x - relativeX),
							(int) Math.round(y - relativeY),
							(int) Math.round(w), (int) Math.round(h), null);

		}

		public TileType getOuter() {

			return TileType.this;

		}

	}

}
