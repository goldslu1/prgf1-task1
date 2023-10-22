import objectdata.Point;
import objectdata.Polygon;
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
	Polygon p;
	private int background;
	private boolean rasterizerStart;
	private boolean holdingShift;
	private boolean pressedP;
	private boolean line;
	String modeP;
	private List<Point> points = new ArrayList(); //Array list for LinerTrivial class
	private List<Point> pointsR = new ArrayList(); //Array list for Filled line rasterizer class and its methods
	private int mode;
	private FilledLineRasterizer rasterizer;
	private int x, y;

	public Canvas(int width, int height) {
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		rasterizerStart = false;
		img = new RasterBI(width, height);
		rasterizer = new FilledLineRasterizer(img);
		pointsR.add(new Point(0,0));
		background = 0x2f2f2f;
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				img.present(g);
			}
		};
		pressedP = false;
		line = false;
		panel.setPreferredSize(new Dimension(width, height));
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		panel.requestFocusInWindow();
		ln = new LinerTrivial(img);
		holdingShift = false;
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				RasterBI newImg = new RasterBI(panel.getWidth(), panel.getHeight());
				img = newImg;
				ln = new LinerTrivial(img);
				rasterizer = new FilledLineRasterizer(img);
				draw();
			}
		});

		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_C) {
					points.clear();
					pointsR.clear();
					pointsR.add(new Point(0,0));
					ln = new LinerTrivial(img);
					rasterizer = new FilledLineRasterizer(img);
					rasterizerStart = false;
					img.clear(background);
					pressedP = false;
					img.present(panel.getGraphics());
					panel.repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_SHIFT){
					holdingShift = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_P){
					draw();
					if (!pressedP){
						if (p == null) {
							p = new Polygon(points, 0xff0000);
						} else {
							p.setEdges(points);
						}
						if (points.size() > 1 && p != null){
							p.drawPolygon(img);

						}
						panel.repaint();
						pressedP = true;
					} else {
						panel.repaint();
						pressedP = false;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT){
					holdingShift = false;
				}
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					if (points.size() == 0) {
						points.add(new Point(e.getX(),e.getY()));
					}
					line = true;
				} else if (e.getButton() == 2){
					mode = 2;
					if (x != 0 && y != 0 && rasterizerStart == false) {
						x = 0;
						y = 0;
						rasterizerStart = true;
					}
					rasterizer.rasterize(x, y, e.getX(), e.getY(), Color.WHITE);
					pointsR.add(new Point(e.getX(), e.getY()));
					x = e.getX();
					y = e.getY();
				} else if (e.getButton() == 3){
					mode = 3;
				} else {
					mode = 0;
				}
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1){
					points.add(new Point(e.getX(),e.getY()));
					line = false;
				}
                else if (e.getButton() == 2){
					mode = 2;
				}
				else if (e.getButton() == 3){
					mode = 3;
				} else {
					mode = 0;
				}

				draw();
				if (pressedP){
					redrawPolygon();
				}
				panel.repaint();

			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				draw();
				if (line) {
					redrawLine();
					if (points.size() > 0) {
						int i = points.size();
						ln.drawLine(points.get(i - 1).getX(), points.get(i - 1).getY(), e.getX(), e.getY(), 0xffff00);
						ln.drawLine(points.get(0).getX(), points.get(0).getY(), e.getX(), e.getY(), 0xffff00);

					}
				} else if (mode == 3 && points.size() > 0) {
					int i = points.size();
					ln.drawDashLine(points.get(i - 1).getX(), points.get(i - 1).getY(), e.getX(), e.getY(), 0xffff00);
					ln.drawDashLine(points.get(0).getX(), points.get(0).getY(), e.getX(), e.getY(), 0xffff00);
				}
				panel.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
                draw();
				if (pressedP){
					redrawPolygon();
				}
				panel.repaint();
			}
		});
	}

	public void redrawLine() {
		if (points.size() > 1){
			for (int i = 0; i < points.size(); ++i) {
				ln.drawLine(points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(),points.get(i).getX(), points.get(i).getY(),0xffff00);
			}
		}
		if (pointsR.size() > 1){
			for (int i = 0; i < pointsR.size()-1; i++) {
				rasterizer.drawLine(pointsR.get(i).getX(), pointsR.get(i).getY(), pointsR.get(i + 1).getX(), pointsR.get(i + 1).getY());
			}
		}
	}
	public void redrawPolygon(){
		if (points.size() > 1 && p != null){
			p.drawPolygon(img);
		}
	}

	public void draw() {
		img.clear(background);
		redrawLine();
		if (pressedP){
			modeP = "On";
		}
		else {
			modeP = "Off";
		}
		img.getGraphics().drawString("LMB - For drawing Polygons with Full Lines, Either click or dragg| RMB - Drawing with dashed lines - Holding the right mouse button to draw dashed lines and left click to accept",5,img.getHeight() - 20);
		img.getGraphics().drawString("MMB - For drawing white lines starting from [0,0] with FilledLineRasterizer class, P[" + modeP + "] - Is a mode for duplicating a second polygon with red color, moved by 50points with the Polygon Class",5,img.getHeight() - 5);
	}

	public void start() {
		x = img.getWidth() / 2;
		y = img.getHeight() / 2;
		draw();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(1000, 600).start());
	}

}