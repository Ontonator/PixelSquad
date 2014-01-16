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

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tommy.pixelsquad.player.Ninja;
import tommy.pixelsquad.player.Player;
import tommy.pixelsquad.player.Wizard;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 900;
	public static final int HEIGHT = WIDTH / 16 * 9;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;

	public boolean up, down, left, right;

	private static final int UP_KEY = KeyEvent.VK_W;
	private static final int DOWN_KEY = KeyEvent.VK_S;
	private static final int LEFT_KEY = KeyEvent.VK_A;
	private static final int RIGHT_KEY = KeyEvent.VK_D;

	public final ArrayList<Player> player = new ArrayList<Player>();
	public static final int CHANGE_PLAYER_KEY = KeyEvent.VK_SPACE;
	public short currentPlayer;

	public final ArrayList<Tile> tile = new ArrayList<Tile>();

	public static Image[] sprNinja = new Image[4];
	public static Image[] sprWizard = new Image[4];

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
			sprNinja[0] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/ninja/right.png"));
			sprNinja[1] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/ninja/back.png"));
			sprNinja[2] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/ninja/left.png"));
			sprNinja[3] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/ninja/front.png"));

			sprWizard[0] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/wizard/right.png"));
			sprWizard[1] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/wizard/back.png"));
			sprWizard[2] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/wizard/left.png"));
			sprWizard[3] = ImageIO.read(getClass().getResource(
					"/tommy/pixelsquad/resources/wizard/front.png"));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		player.add(new Ninja(this));
		player.add(new Wizard(this));
		player.add(new Ninja(this));
		player.add(new Wizard(this));

		for (int i = 0; i < 5; i++) {
			boolean solid = Math.random() < 0.5;
			tile.add(new Tile(solid ? sprNinja[0] : sprWizard[0], solid, Math
					.random() * (WIDTH - 10), Math.random() * (HEIGHT - 10),
					20, 20));
		}

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

	public void update() {

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

		g.setColor(Color.MAGENTA.darker());
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Player i : player)
			i.draw(g, zb, 0, 0);

		for (Tile i : tile)
			i.draw(g, zb, 0, 0);

		g.setPaint(Color.GREEN.brighter());
		Player selPl = player.get(currentPlayer);
		g.draw(new Rectangle2D.Double(selPl.x + selPl.visualXOffset, selPl.y
				+ selPl.visualYOffset, selPl.visualW - 1, selPl.visualH - 1));

		g.setColor(Color.WHITE);
		g.drawString(Double.toString(player.get(currentPlayer).x), 0, g
				.getFontMetrics().getHeight());
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
		}

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
