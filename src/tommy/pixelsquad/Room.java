package tommy.pixelsquad;

import java.util.ArrayList;

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

}