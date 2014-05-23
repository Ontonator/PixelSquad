package tommy.pixelsquad;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import tommy.pixelsquad.TileType.Tile;
import tommy.pixelsquad.player.Player;

public class Room {

	public Game game;

	public double w, h;

	public ArrayList<Player> player = new ArrayList<Player>();
	public ArrayList<Tile> tile = new ArrayList<Tile>();

	public Room(Game game, double w, double h) {

		this.game = game;

		this.w = w;
		this.h = h;

	}

	public Room(Game game, String levelName, TileType background) {

		this.game = game;

		try {

			BufferedImage bi = (BufferedImage) Lib.getImage(getClass(),
					"/tommy/pixelsquad/rooms/" + levelName + ".png");

			w = bi.getWidth() * Game.DEFAULT_TILE_WIDTH;
			h = bi.getHeight() * Game.DEFAULT_TILE_WIDTH;

			for (int i = 0; i < w; i += background.w)
				for (int j = 0; j < h; j += background.h)
					tile.add(background.new Tile(i, j));

			for (int i = 0; i < bi.getWidth(); i++)
				for (int j = 0; j < bi.getHeight(); j++) {
					int pixelRGB = bi.getRGB(i, j);
					if ((pixelRGB & 0xff000000) == 0xff000000) {
						TileType tileType = game.tileTypeManager
								.getTileType(pixelRGB & 0xffffff);
						if (tileType != null)
							tile.add(tileType.new Tile(i * 16, j * 16));
					}
				}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}