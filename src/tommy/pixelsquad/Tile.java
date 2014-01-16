package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;

public class Tile {

	public double x, y, w, h;

	public Image sprite;

	public boolean solid;
	public boolean extruding;

	public Tile(Image sprite, boolean solid, boolean extruding, double x, double y, double w,
			double h) {

		this.sprite = sprite;
		
		this.solid = solid;
		this.extruding = extruding;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}

	public void draw(Graphics2D g, double[][] zb, double relativeX,
			double relativeY) {

		Lib.drawImageZb(g, zb, sprite, (int) Math.round(x),
				(int) Math.round(y), extruding ? y + h : -Double.MAX_VALUE, (int) Math.round(w),
				(int) Math.round(h));

	}

}