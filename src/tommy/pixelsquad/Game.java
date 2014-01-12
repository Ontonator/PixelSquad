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

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tommy.pixelsquad.player.*;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int width = 300;
	public static final int height = width / 16 * 9;
	public static final int scale = 3;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private long ticks = 0;

	public boolean up, down, left, right;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	private static final int UP_KEY = KeyEvent.VK_UP;// W;
	private static final int DOWN_KEY = KeyEvent.VK_DOWN;// S;
	private static final int LEFT_KEY = KeyEvent.VK_LEFT;// A;
	private static final int RIGHT_KEY = KeyEvent.VK_RIGHT;// D;
=======
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5
	private static final int upKey = KeyEvent.VK_W;// W;
	private static final int downKey = KeyEvent.VK_S;// S;
	private static final int leftKey = KeyEvent.VK_A;// A;
	private static final int rightKey = KeyEvent.VK_D;// D;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5

	public final Player[] player = new Player[4];
	public static final int CHANGE_PLAYER_KEY = KeyEvent.VK_SPACE;
	public short currentPlayer;
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5
=======
>>>>>>> e3b7079d5726e3977e1764555edcaa5c073097e5

	public static Image[] sprNinja = new Image[4];
	public static Image[] sprWizard = new Image[4];

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
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

		player[0] = new Ninja(this);
		player[1] = new Wizard(this);
		player[2] = new Ninja(this);
		player[3] = new Wizard(this);

		player[0].selected = true;

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
				ticks++;
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
		g.setColor(Color.MAGENTA.darker());
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Player i : player)
			i.draw(g, 0, 0);

		g.setPaint(Color.GREEN.brighter());
		Player selPl = player[currentPlayer];
		g.draw(new Rectangle2D.Double(selPl.x, selPl.y, selPl.w - 1,
				selPl.h - 1));

		g.setColor(Color.BLACK);
		g.drawString(Long.toString(ticks), 0, g.getFontMetrics().getHeight());
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
			player[currentPlayer].selected = false;

			currentPlayer++;
			if (currentPlayer > 3)
				currentPlayer -= 4;

			player[currentPlayer].selected = true;
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
