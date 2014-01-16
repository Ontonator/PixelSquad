package tommy.pixelsquad;

import java.awt.Image;

public abstract class Entity {

	public double x = 0, y = 0;
	public double w = 32, h = 32;

	public double visualW = 32, visualH = 48;
	public double visualXOffset = 0, visualYOffset = -16;

	public double hsp, vsp;
	public short dir = 3;

	public double moveSp;

	public Game game;
	public Image[] spriteArray;

	public Entity(Game game, Image[] spriteArray) {

		this.game = game;
		this.spriteArray = spriteArray;

	}

}
