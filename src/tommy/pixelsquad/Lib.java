package tommy.pixelsquad;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import tommy.pixelsquad.TileType.Tile;

public abstract class Lib {

	public static boolean checkCollisionBB(double x1, double y1, double x2,
			double y2, double w1, double h1, double w2, double h2) {

		return x1 > x2 - w1 && x1 < x2 + w2 && y1 > y2 - h1 && y1 < y2 + h2;

	}

	public static void drawImageZb(Graphics2D g, double[][] zb, Image image,
			int x, int y, double z, int w, int h) {

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(image, 0, 0, w, h, null);
		g2.dispose();

		int[] biDataBuffer = ((DataBufferInt) bi.getRaster().getDataBuffer())
				.getData();

		try {
			for (int i = 0; i < w; i++)
				for (int j = 0; j < h; j++) {

					int windowPixelX = x + i;
					int windowPixelY = y + j;
					int pixelNum = i + j * w;

					if (checkCollisionBB(windowPixelX, windowPixelY, 0, 0, 1,
							1, Game.WIDTH, Game.HEIGHT)
							&& (z > zb[windowPixelX][windowPixelY] || (z == -Double.MAX_VALUE && zb[windowPixelX][windowPixelY] == -Double.MAX_VALUE))
							&& (biDataBuffer[pixelNum] & 0xff000000) != 0)

						zb[windowPixelX][windowPixelY] = z;
					else
						biDataBuffer[pixelNum] = 0;

				}
			g.drawImage(bi, x, y, null);
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

		boolean colliding = false;
		for (Tile i : ent.room.tile)
			// TODO fix errors; something to do with "this"
			if (i.getOuter().solid
					&& Lib.checkCollisionBB(ent.x + xOffset, ent.y + yOffset,
							i.x, i.y, ent.w, ent.h, i.getOuter().w,
							i.getOuter().h)) {
				colliding = true;
				break;
			}

		if (colliding) {
			double farthestValue = positive ? Double.MAX_VALUE
					: -Double.MAX_VALUE;

			double nextTile = farthestValue;
			for (Tile i : ent.room.tile)
				if ((positive ? (vertical ? i.y : i.x) < nextTile
						: (vertical ? i.y + i.getOuter().h : i.x
								+ i.getOuter().w) > nextTile)
						&& Lib.checkCollisionBB(ent.x + xOffset, ent.y
								+ yOffset, i.x, i.y, ent.w, ent.h,
								i.getOuter().w, i.getOuter().h)
						&& (vertical ? (positive ? i.y >= entEdge : i.y
								+ i.getOuter().h <= entEdge)
								: (positive ? i.x >= entEdge : i.x
										+ i.getOuter().w <= entEdge)))
					nextTile = vertical ? i.y + (positive ? 0 : i.getOuter().h)
							: i.x + (positive ? 0 : i.getOuter().w);
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

	public static Image getImage(@SuppressWarnings("rawtypes") Class objClass,
			String path) throws IOException, IllegalArgumentException {

		return ImageIO.read(objClass.getResource(path));

	}

}