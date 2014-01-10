package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Tile {

	double x, y, w, h;

	public Image sprite;

	public boolean solid;

	public Tile(Image sprite, boolean solid, double x, double y, double w,
			double h) {

		this.sprite = sprite;
		this.solid = solid;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}

	public void draw(Graphics2D g, double relativeX, double relativeY) {

		g.drawImage(sprite, (int) Math.round(x - relativeX),
				(int) Math.round(y - relativeY), (int) Math.round(w),
				(int) Math.round(h), null);

	}
}