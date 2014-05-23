package tommy.pixelsquad;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import tommy.pixelsquad.TileType.Tile;
import tommy.pixelsquad.player.Ninja;
import tommy.pixelsquad.player.Player;
import tommy.pixelsquad.player.Wizard;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 900;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int pixelSize = 2;
	public static final int DEFAULT_TILE_WIDTH = 16;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;

	public boolean up, down, left, right;

	private static final int UP_KEY = KeyEvent.VK_W;
	private static final int DOWN_KEY = KeyEvent.VK_S;
	private static final int LEFT_KEY = KeyEvent.VK_A;
	private static final int RIGHT_KEY = KeyEvent.VK_D;

	public Room room;// = new Room(this, WIDTH * 2, HEIGHT * 2);

	public TileTypeManager tileTypeManager = new TileTypeManager();

	public static final int CHANGE_PLAYER_KEY = KeyEvent.VK_SHIFT;
	public short currentPlayer;

	public static final Image[][] sprNinja = new Image[4][3];
	public static final Image[][] sprWizard = new Image[4][3];

	public static Image sprHighlight;

	public static Image sprGrass;
	public static Image sprBush;
	public static Image sprRockSmall;
	public static Image sprVoid;

	public Game() {

		frame = new JFrame();
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setTitle("PixelSquad (Beta)");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try {
			sprNinja[0][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/right.png");
			sprNinja[1][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/back.png");
			sprNinja[2][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/left.png");
			// sprNinja[3] = Lib.getImage(getClass(),
			// "/tommy/pixelsquad/resources/ninja/front.png");
			sprNinja[3][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/front0.png");
			sprNinja[3][1] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/front1.png");
			// sprNinja[3][2] = Lib.getImage(getClass(),
			// "/tommy/pixelsquad/resources/ninja/front2.png");

			sprWizard[0][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/right.png");
			sprWizard[1][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/back.png");
			sprWizard[2][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/left.png");
			sprWizard[3][0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/front.png");

			sprHighlight = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/highlight.png");

			sprGrass = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/grass.png");
			sprBush = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/bush.png");
			sprRockSmall = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/rockSmall.png");
			sprVoid = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/void.png");
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		tileTypeManager.addTileType(new TileType(sprGrass, false, false,
				sprGrass.getWidth(null) * pixelSize, sprGrass.getHeight(null)
						* pixelSize), 0x00ff00);

		tileTypeManager.addTileType(
				new TileType(sprBush, false, true, sprBush.getWidth(null)
						* pixelSize, sprBush.getHeight(null) * pixelSize),
				0x009900);

		tileTypeManager.addTileType(
				new TileType(sprRockSmall, true, true, sprRockSmall
						.getWidth(null) * pixelSize, sprRockSmall
						.getHeight(null) * pixelSize), 0x777777);

		room = new Room(this, "test", tileTypeManager.getTileType(0x00ff00));

		room.player.add(new Ninja(room));
		room.player.add(new Wizard(room));

		room.player.get(0).selected = true;

		addKeyListener(this);

	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		long nextTick = System.nanoTime();

		int fps = 0;
		long lastSecond = System.nanoTime();

		while (running) {
			update();
			sleepToNextTick(nextTick);
			nextTick = System.nanoTime();

			fps++;
			if (System.nanoTime() > lastSecond + 1000000000) {
				lastSecond = System.nanoTime();
				System.out.println(fps);
				fps = 0;
			}
		}

	}

	private void sleepToNextTick(long nextTick) {

		long sleepTime = (31250000 - System.nanoTime() + nextTick) / 1000000;
		try {
			if (sleepTime > 0)
				Thread.sleep(sleepTime);
			else
				System.out.println("lagging: " + sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public synchronized void update() {

		synchronized (room) {

			for (Player i : room.player)
				i.step();

			render();

		}

	}

	public void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		double[][] zb = new double[WIDTH][HEIGHT];
		for (int i = 0; i < zb.length; i++)
			for (int j = 0; j < zb[i].length; j++)
				zb[i][j] = -Double.MAX_VALUE;

		Player selPl = room.player.get(currentPlayer);

		double relativeX = selPl.x + selPl.visualXOffset + selPl.visualW / 2
				- WIDTH / 2;
		double relativeY = selPl.y + selPl.visualYOffset + selPl.visualH / 2
				- HEIGHT / 2;

		if (relativeX < 0)
			relativeX = 0;
		else if (relativeX + WIDTH > room.w)
			relativeX = room.w - WIDTH;

		if (relativeY < 0)
			relativeY = 0;
		else if (relativeY + HEIGHT > room.h)
			relativeY = room.h - HEIGHT;

		for (Tile i : room.tile)
			if (!i.getOuter().extruding)
				i.draw(g, zb, relativeX, relativeY);

		for (Tile i : room.tile)
			if (i.getOuter().extruding)
				i.draw(g, zb, relativeX, relativeY);

		for (Player i : room.player)
			i.draw(g, zb, relativeX, relativeY);

		Lib.drawImageZb(g, zb, sprHighlight,
				(int) Math.round(selPl.x - relativeX),
				(int) Math.round(selPl.y - relativeY), -Double.MAX_VALUE,
				(int) Math.round(selPl.w), (int) Math.round(selPl.h));

		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		Game game = new Game();

		game.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == UP_KEY)
			up = true;
		else if (e.getKeyCode() == DOWN_KEY)
			down = true;
		else if (e.getKeyCode() == LEFT_KEY)
			left = true;
		else if (e.getKeyCode() == RIGHT_KEY)
			right = true;
		else if (e.getKeyCode() == CHANGE_PLAYER_KEY) {
			room.player.get(currentPlayer).selected = false;

			currentPlayer++;
			if (currentPlayer >= room.player.size())
				currentPlayer -= room.player.size();

			room.player.get(currentPlayer).selected = true;
		} else if (e.getKeyCode() == KeyEvent.VK_EQUALS)
			room.player.add(Math.random() < 0.5 ? new Ninja(room) : new Wizard(
					room));

	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == UP_KEY)
			up = false;
		else if (e.getKeyCode() == DOWN_KEY)
			down = false;
		else if (e.getKeyCode() == LEFT_KEY)
			left = false;
		else if (e.getKeyCode() == RIGHT_KEY)
			right = false;

	}

}