package tommy.pixelsquad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public abstract class Lib {

	public static boolean checkCollisionBB(double x1, double y1, double x2,
			double y2, double w1, double h1, double w2, double h2) {

		return x1 > x2 - w1 && x1 < x2 + w2 && y1 > y2 - h1 && y1 < y2 + h2;

	}

	public static void drawImageZb(Graphics2D g, double[][] zb, Image image,
			int x, int y, double z, int w, int h) {

		BufferedImage bi = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

		int[] biDataBuffer = ((DataBufferInt) bi.getRaster().getDataBuffer())
				.getData();

		try {
			for (int i = 0; i < w; i++)
				for (int j = 0; j < h; j++) {

					int windowPixelX = x + i;
					int windowPixelY = y + j;

					if (checkCollisionBB(windowPixelX, windowPixelY, 0, 0, 1,
							1, Game.WIDTH, Game.HEIGHT)
							&& z > zb[windowPixelX][windowPixelY]) {

						int pixelX = (int) Math.floor((double) i / w
								* image.getWidth(null));
						int pixelY = (int) Math.floor((double) j / h
								* image.getHeight(null));

						int pixel = biDataBuffer[pixelX + pixelY
								* bi.getWidth()];

						int alpha = pixel & 0xff000000;
						if (alpha != 0) {

							int red = pixel & 0xff0000 >> 16;
							int green = pixel & 0xff00 >> 8;
							int blue = pixel & 0x000000ff;

							g.setPaint(new Color(red, green, blue));
							g.fill(new Rectangle2D.Double(x + i, y + j, 1, 1));

							zb[windowPixelX][windowPixelY] = z;

						}

					}
				}
		} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	public static void smartMove(Entity ent, int dir, double moveSp) {

		boolean vertical = dir == 1 || dir == 3;
		boolean positive = dir == 0 || dir == 3;

		double xOffset = vertical ? 0 : (positive ? moveSp : -moveSp);
		double yOffset = vertical ? (positive ? moveSp : -moveSp) : 0;

		double entEdge = vertical ? ent.y + (positive ? ent.h : 0) : ent.x
				+ (positive ? ent.w : 0);

		double farthestValue = positive ? Double.MAX_VALUE : -Double.MAX_VALUE;

		boolean colliding = false;
		for (Tile i : ent.game.tile)
			if (i.solid)
				if (Lib.checkCollisionBB(ent.x + xOffset, ent.y + yOffset, i.x,
						i.y, ent.w, ent.h, i.w, i.h)) {
					colliding = true;
					break;
				}

		if (colliding) {
			double nextTile = farthestValue;
			for (Tile i : ent.game.tile)
				if (i.x < nextTile
						&& Lib.checkCollisionBB(ent.x + xOffset, ent.y
								+ yOffset, i.x, i.y, ent.w, ent.h, i.w, i.h)
						&& (vertical ? (positive ? i.y >= entEdge
								: i.y + i.h <= entEdge)
								: (positive ? i.x >= entEdge
										: i.x + i.w <= entEdge)))
					nextTile = vertical ? i.y : i.x;
			if (nextTile != farthestValue)
				if (vertical)
					ent.y = nextTile - (positive ? ent.h : 0);
				else
					ent.x = nextTile - (positive ? ent.w : 0);
		} else if (vertical)
			ent.y += (positive ? moveSp : -moveSp);
		else
			ent.x += (positive ? moveSp : -moveSp);

	}
	
}