import objectdata.Point;
import rasterdata.FilledLineRasterizer;
import rasterdata.Presentable;
import rasterdata.RasterBI;
import rasterop.LinerTrivial;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
	private RasterBI img;
	private Point p1;
	private Point p2;
	private int background;
	private List<Point> points = new ArrayList();
	private FilledLineRasterizer rasterizer;
	private int x, y;

	public Canvas(int width, int height) {
		frame = new JFrame();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
		ln = new LinerTrivial();

		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (panel.getWidth()<1 || panel.getHeight()<1)
					return;
				if (panel.getWidth()<=img.getWidth() && panel.getHeight()<=img.getHeight()) //no resize if new one is smaller
					return;
				RasterBI newRaster = new RasterBI(panel.getWidth(), panel.getHeight());

				newRaster.draw(img);
				img = newRaster;
				rasterizer = new FilledLineRasterizer(img);
				panel.repaint();
			}
		});

		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_C) {
					img.clear(background);
					img.present(panel.getGraphics());
				}
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//p1 = new Point(e.getX(), e.getY());
				if (e.getButton() == 1) {
					if (points.size() == 0) {
						points.add(new Point(e.getX(),e.getY()));
					} else {
						points.clear();
						points.add(new Point(e.getX(),e.getY()));
					}
				}
                /*int size = 2;
				int color  = 0xFFFFFF;
				if (e.getButton() == MouseEvent.BUTTON1){
					color = 0xff0000;
				}
				if (e.getButton() == MouseEvent.BUTTON2){
					color = 0xff00;
				}

				for(int i=-size; i<=size; i++) {
					for (int j = -size; j <= size; j++) {
						img.setPixel(e.getX() + i, e.getY() + j, color);
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					rasterizer.drawLine(x, y, e.getX(), e.getY());
					x = e.getX();
					y = e.getY();
				}
				panel.repaint();*/
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1){
					points.add(new Point(e.getX(),e.getY()));
				}
				panel.repaint();
                /*p2 = new Point(e.getX(), e.getY());
				ln.drawLine(img, p1, p2,0xffff00);

				int size = 2;
				int color  = 0xFFFFFF;
				if (e.getButton() == MouseEvent.BUTTON1)
					color = 0xff0000;
				if (e.getButton() == MouseEvent.BUTTON2)
					color = 0xff00;
				if (e.getButton() == MouseEvent.BUTTON3)
					color =0xff;
				for(int i=-size; i<=size; i++)
					for(int j=-size; j<=size; j++)img.setColor(e.getX()+i, e.getY()+j, color);

				img.present(panel.getGraphics());*/
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				img.clear(background);
				if (points.size() > 0){
					int i = points.size();
					//ln.drawLine(img, points.get(i - 1).getX(), points.get(i - 1).getY(), e.getX(), e.getY(), 0xffff00);
					//ln.drawLine(img, points.get(0).getX(), points.get(0).getY(), e.getX(), e.getY(), 0xffff00);
					ln.drawLine(img, points.get(0).getX(), points.get(0).getY(), e.getX(), e.getY(), 0xffff00);
				}

				panel.repaint();
			}
		});
	}

	public void draw() {
		img.clear(background);
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