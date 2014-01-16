package tommy.pixelsquad;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import tommy.pixelsquad.player.Ninja;
import tommy.pixelsquad.player.Player;
import tommy.pixelsquad.player.Wizard;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 900;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int pixelSize = 2;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;

	public boolean up, down, left, right;

	private static final int UP_KEY = KeyEvent.VK_W;
	private static final int DOWN_KEY = KeyEvent.VK_S;
	private static final int LEFT_KEY = KeyEvent.VK_A;
	private static final int RIGHT_KEY = KeyEvent.VK_D;

	public final ArrayList<Player> player = new ArrayList<Player>();
	public static final int CHANGE_PLAYER_KEY = KeyEvent.VK_SHIFT;
	public short currentPlayer;

	public final ArrayList<Tile> tile = new ArrayList<Tile>();

	public static final Image[] sprNinja = new Image[4];
	public static final Image[] sprWizard = new Image[4];

	public static Image sprGrass;
	public static Image sprGrassLong;
	public static Image sprRockSmall;

	public Game() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);

		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("PixelSquad (Beta)");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try {
			sprNinja[0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/right.png");
			sprNinja[1] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/back.png");
			sprNinja[2] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/left.png");
			sprNinja[3] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/ninja/front.png");

			sprWizard[0] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/right.png");
			sprWizard[1] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/back.png");
			sprWizard[2] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/left.png");
			sprWizard[3] = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/wizard/front.png");

			sprGrass = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/grass.png");
			sprGrassLong = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/bush.png");
			sprRockSmall = Lib.getImage(getClass(),
					"/tommy/pixelsquad/resources/tiles/rockSmall.png");
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		player.add(new Ninja(this));
		player.add(new Wizard(this));
		player.add(new Ninja(this));
		player.add(new Wizard(this));

		double tileWidth = sprGrass.getWidth(null) * pixelSize;
		double tileHeight = sprGrass.getHeight(null) * pixelSize;
		for (int i = 0; i < WIDTH; i += tileWidth)
			for (int j = 0; j < HEIGHT; j += tileHeight)
				tile.add(new Tile(sprGrass, false, false, i, j, tileWidth,
						tileHeight));

		tileWidth = sprGrassLong.getWidth(null) * pixelSize;
		tileHeight = sprGrassLong.getHeight(null) * pixelSize;
		for (int i = 0; i < 20; i++)
			tile.add(new Tile(sprGrassLong, false, true, Math.random()
					* (WIDTH - tileWidth),
					Math.random() * (WIDTH - tileHeight), tileWidth, tileHeight));

		tileWidth = sprRockSmall.getWidth(null) * pixelSize;
		tileHeight = sprRockSmall.getHeight(null) * pixelSize;
		for (int i = 0; i < 30; i++)
			tile.add(new Tile(sprRockSmall, true, true, Math.random()
					* (WIDTH - tileWidth), Math.random()
					* (HEIGHT - tileHeight), tileWidth, tileHeight));

		player.get(0).selected = true;

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

		long lastTick = System.nanoTime() - 20000000;

		while (running)
			if (lastTick < System.nanoTime() - 20000000) {
				lastTick += 20000000;
				update();
			}

	}

	public synchronized void update() {

		for (Player i : player)
			i.step();

		render();

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

		for (Player i : player)
			i.draw(g, zb, 0, 0);

		for (Tile i : tile)
			i.draw(g, zb, 0, 0);

		Player selPl = player.get(currentPlayer);
		g.setPaint(Color.GREEN.brighter());
		g.draw(new Rectangle2D.Double(
				Math.round(selPl.x + selPl.visualXOffset), Math.round(selPl.y
						+ selPl.visualYOffset), selPl.visualW - 1,
				selPl.visualH - 1));

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
			player.get(currentPlayer).selected = false;

			currentPlayer++;
			if (currentPlayer >= player.size())
				currentPlayer -= player.size();

			player.get(currentPlayer).selected = true;
		} else if (e.getKeyCode() == KeyEvent.VK_EQUALS)
			player.add(Math.random() < 0.5 ? new Ninja(this) : new Wizard(this));

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
