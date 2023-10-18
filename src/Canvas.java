import objectdata.Point;
import rasterdata.Presentable;
import rasterdata.Raster;
import rasterdata.RasterBI;
import rasterop.CirclerTrivial;
import rasterop.LinerTrivial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * trida pro kresleni na platno: zobrazeni pixelu
 * 
 * @author PGRF FIM UHK
 * @version 2020
 */

//DU: zacit s krizkem uprostred, aby se s nim dalo hybat na vsechny strany a abby za tim krizkem byla videt trasa, naprs o 3
	//ukladani souradnic do arraylistu
public class Canvas {

	private JFrame frame;
	private JPanel panel;
	private LinerTrivial ln;
	private CirclerTrivial cr;
	//private BufferedImage img;
	private RasterBI img; //TODO prepsat s vlastni tridou, kdyz uzivatel klikne oznaci se bod a tazenim mysi ? + dalsi bod = usecka
	private Presentable prs;
	private Point p1;
	private Point p2;
	private boolean mouseDragged;
	private int background;
	private int x, y;

	public Canvas(int width, int height) {
		frame = new JFrame();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  //aby jsme nastavili barvu pixelu
		img = new RasterBI(width, height);
		background = 0x2f2f2f;
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				img.present(g);
			}
		};

		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		panel.requestFocusInWindow();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {  //pri stisknuti tlacitka se bude posunovat jako had
					y--;
					img.clear(background);
					img.setColor(x, y, 0xffff00);
					img.setColor(x, y, 0xffff00);
					img.setColor(x + 1, y, 0xffff00);
					img.setColor(x - 1, y, 0xffff00);
					img.setColor(x, y - 1, 0xffff00);
					img.setColor(x, y + 1, 0xffff00);
					img.present(panel.getGraphics());
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					y++;
					img.clear(background);
					img.setColor(x, y, 0xffff00);
					img.setColor(x + 1, y, 0xffff00);
					img.setColor(x - 1, y, 0xffff00);
					img.setColor(x, y - 1, 0xffff00);
					img.setColor(x, y + 1, 0xffff00);
					img.present(panel.getGraphics());
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					x--;
					img.clear(background);
					img.setColor(x, y, 0xffff00);
					img.setColor(x + 1, y, 0xffff00);
					img.setColor(x - 1, y, 0xffff00);
					img.setColor(x, y - 1, 0xffff00);
					img.setColor(x, y + 1, 0xffff00);
					img.present(panel.getGraphics());
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					x++;
					img.clear(background);
					img.setColor(x, y, 0xffff00);
					img.setColor(x + 1, y, 0xffff00);
					img.setColor(x - 1, y, 0xffff00);
					img.setColor(x, y - 1, 0xffff00);
					img.setColor(x, y + 1, 0xffff00);
					img.present(panel.getGraphics());
				}
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					img.setColor(e.getX(), e.getY(), 0xffff00);
				if (e.getButton() == MouseEvent.BUTTON2)
					img.setColor(e.getX(), e.getY(), 0xff00ff);
				if (e.getButton() == MouseEvent.BUTTON3)
					img.setColor(e.getX(), e.getY(), 0xffffff);
				img.present(panel.getGraphics());
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				img.clear(background);
				p1 = new Point(e.getX(), e.getY());
				mouseDragged = false;
				img.present(panel.getGraphics());
			}
		});
		/*panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				img.clear(background);
				p2 = new Point(e.getX(), e.getY());
				mouseDragged = true;
				ln = new LinerTrivial();
				ln.drawLine(img, p1, p2,0xffff00);
				img.present(panel.getGraphics());
			}
		});*/
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				img.clear(background);
				p2 = new Point(e.getX(), e.getY());
				mouseDragged = true;
				ln = new LinerTrivial();
				cr = new CirclerTrivial();
				ln.drawLine(img, p1, p2,0xffff00);
				cr.drawCircle(img, p1, 0xffff00);
				img.present(panel.getGraphics());
			}
		});
	}

	/*public void clear() {
		Graphics gr = img.getGraphics();
		gr.setColor(new Color(0x2f2f2f));
		gr.fillRect(0, 0, img.getWidth(), img.getHeight());
	}*/

	/*public void present(Graphics graphics) {
		graphics.drawImage(img, 0, 0, null);
	}*/

	public void draw() {
		img.clear(background);
		/*img.setColor(img.getWidth() / 2 , img.getHeight() / 2, 0xffff00);
		img.setColor((img.getWidth() / 2) + 1 , img.getHeight() / 2, 0xffff00);
		img.setColor((img.getWidth() / 2) - 1 , img.getHeight() / 2, 0xffff00);
		img.setColor(img.getWidth() / 2 , (img.getHeight() / 2) + 1, 0xffff00);
		img.setColor(img.getWidth() / 2 , (img.getHeight() / 2) - 1, 0xffff00);*/

	}

	public void start() {
		x = img.getWidth() / 2;
		y = img.getHeight() / 2;
		draw();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
	}

}