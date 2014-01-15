package tommy.pixelsquad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Lib {

	public static boolean checkCollisionBB(double x1, double y1, double x2,
			double y2, double w1, double h1, double w2, double h2) {

		return x1 > x2 - w1 && x1 < x2 + w2 && y1 > y2 - h1 && y1 < y2 + h2;

	}

	public static void drawImageZb(Graphics2D g, double[][] zb, Image image,
			int x, int y, double z, int w, int h) {

		// Raster raster = ((BufferedImage) image).getData();
		BufferedImage bi = (BufferedImage) image;
		// double[] pixelRGB = new double[4];

		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				if (z > zb[i][j]) {
					// raster.getPixel(
					// (int) Math.floor((double) i / w
					// * image.getWidth(null)),
					// (int) Math.floor((double) j / h
					// * image.getHeight(null)), pixelRGB);
					// int pixelRGB = // , pixelRGB);
					g.setPaint(new Color(bi.getRGB(
							(int) Math.floor((double) i / w
									* image.getWidth(null)),
							(int) Math.floor((double) j / h
									* image.getHeight(null)))));
					
					// (int)
					// Math.round(),
					// (int) Math.round(), (int) Math
					// .round()));
					g.fill(new Rectangle2D.Double(x + i, y + j, 1, 1));
				}

	}

}