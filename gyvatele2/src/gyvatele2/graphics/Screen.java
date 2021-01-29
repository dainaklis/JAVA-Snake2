package gyvatele2.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.Text;

import gyvatele2.entities.Apple;
import gyvatele2.entities.BodyPart;

public class Screen extends JPanel implements Runnable {

	public static final int WIDTH = 500, HEIGHT = 500;

	private Thread thread;
	private boolean running = false;

	private BodyPart b;
	private ArrayList<BodyPart> snake;

	private Apple apple;
	private ArrayList<Apple> apples;

	private Random r;

	private int xCoor = 5, yCoor = 5;
	private int size = 5;
	private int score = 0;
	private boolean right = true, left = false, up = false, down = false;
	private int ticks = 0;
	private Key key;

	
	public Screen() {

		setFocusable(true);
		key = new Key();
		addKeyListener(key);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		r = new Random();

		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();

		start();

	}

	public void tick() {

		if (snake.size() == 0) {

			b = new BodyPart(xCoor, yCoor, 20);
			snake.add(b);

		}

		if (apples.size() == 0) {

			int xCoor = r.nextInt(24);
			int yCoor = r.nextInt(24);

			apple = new Apple(xCoor, yCoor, 20);
			apples.add(apple);
		}

		//Sueda ENEMY, padideja TASKAI
		for (int i = 0; i < apples.size(); i++) {

			if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) 
			{
				size++;
				apples.remove(i);
				i--;
				score++;
			}

		}
		
		// LOSE jei atsitrenkiu i save
		for (int i = 0; i < snake.size(); i++) {

			if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {

				if (i != snake.size() - 1) {
					stop();

				}
			}

		}

		// LOSE jei iseinu is LAUKO, I SIENA
		if (xCoor < 0 || xCoor > 24 || yCoor < 0 || yCoor > 24) {

			stop();

		}

		ticks++;

		if (ticks > 1200000)

		{

			if (right) {
				xCoor++;
			}
			if (left) {
				xCoor--;
			}
			if (up) {
				yCoor--;
			}
			if (down) {
				yCoor++;
			}

			ticks = 0;

			b = new BodyPart(xCoor, yCoor, 20);
			snake.add(b);

			if (snake.size() > size) {
				snake.remove(0);
			}

		}
	}

	// GAMEPAD as
	public void paint(Graphics g) {

		g.clearRect(0, 0, WIDTH, HEIGHT);

//		g.setFont(new Font("arial",Font.BOLD,20));
//		g.drawString("Pradzia", 50, 35);

		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 500, 500);
		
		
		//Raso TASKUS
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString("Taskai: " + score, 8, 17);

		//Padaro TINKLELI
		g.setColor(Color.BLACK);
		for (int i = 0; i < WIDTH / 20; i++) {
			g.drawLine(i * 20, 0, i * 20, HEIGHT);
		}

		for (int i = 0; i < HEIGHT / 20; i++) {
			g.drawLine(0, i * 20, WIDTH, i * 20);
		}

		for (int i = 0; i < snake.size(); i++) {
			snake.get(i).draw(g);

		}

		for (int i = 0; i < apples.size(); i++) {

			apples.get(i).draw(g);
		}

	}

	public void start() {
		
		//Lentele pradzioje, kad pradeti zaisti!!!!
		//1 importuojam IMAGE
		ImageIcon pradzia = new ImageIcon(getClass().getClassLoader().getResource("resources/snake_32.png"));
		JLabel icon = new JLabel(pradzia,JLabel.CENTER);
		icon.setPreferredSize(new Dimension(80,80));
		
		//2 parasom TEXT ir ji nustatom
		JLabel text = new JLabel();
		text.setText("Pasiruosti !!!");
		text.setPreferredSize(new Dimension(0,40)); //DYDIS
		text.setForeground(Color.BLUE);
		text.setHorizontalAlignment(0);
		Font stiliusFont = new Font("arial", Font.BOLD, 30);
		text.setFont(stiliusFont);
		//text.setFont(new Font("arial", Font.BOLD, 10)); //GALIMA ir TAIP
		
		//IKELIAM i LENTELE
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(icon, BorderLayout.NORTH);
		panel.add(text, BorderLayout.CENTER);
		
		//ISSAUKIAM lentele
		JOptionPane.showMessageDialog(null, panel, "", JOptionPane.PLAIN_MESSAGE);

		running = true;
		thread = new Thread(this, "Game Loop");
		thread.start();

	}

	public void stop() {
		
		JLabel text = new JLabel();
		text.setText("Pabaiga !!! Jusu taskai");
		text.setPreferredSize(new Dimension(330,20)); //DYDIS
		text.setForeground(Color.RED);
		text.setHorizontalAlignment(0);
		Font stiliusFont = new Font("arial", Font.BOLD, 15);
		text.setFont(stiliusFont);
		//text.setFont(new Font("arial", Font.BOLD, 10)); //GALIMA ir TAIP
		
		JLabel text2 = new JLabel();
		text2.setText(""+score);
		text2.setPreferredSize(new Dimension(0,20)); //DYDIS
		text2.setForeground(Color.RED);
		text2.setHorizontalAlignment(0);
		text2.setFont(stiliusFont);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(text, BorderLayout.NORTH);
		panel2.add(text2,BorderLayout.CENTER);
		
		JOptionPane.showMessageDialog(this,panel2, "",JOptionPane.PLAIN_MESSAGE);
		
		System.exit(0);

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void run() {

		while (running) {

			tick();
			repaint();

		}

	}

	public class Key implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_RIGHT && !left) {

				up = false;
				down = false;
				right = true;
			}

			if (key == KeyEvent.VK_LEFT && !right) {

				up = false;
				down = false;
				left = true;
			}

			if (key == KeyEvent.VK_UP && !down) {

				right = false;
				left = false;
				up = true;
			}

			if (key == KeyEvent.VK_DOWN && !up) {

				right = false;
				left = false;
				down = true;
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
