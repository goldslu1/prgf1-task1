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
	private List<Point> points = new ArrayList();
	private List<Point> pointsR = new ArrayList();
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
		/**
		 * Happens when the window gets resized, so that everything stays - it gets redrawn
		 */
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
		/**
		 * Event that happens when a key is pressed
		 */
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_C) { //Clears the Canvas when C is pressed
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
				if (e.getKeyCode() == KeyEvent.VK_P){ //If P is pressed, it will draw a duplicate of already existing polygon
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
		/**
		 * Even that happens when the mouse is pressed on the panel
		 */
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1 && !holdingShift) { //When clicking with LMB
					if (points.size() == 0) {
						points.add(new Point(e.getX(),e.getY()));
					}
					line = true;
				} else if (e.getButton() == 2 && !holdingShift){ //When clicking with MMB
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
				} else if (e.getButton() == 3 && !holdingShift){ //When clicking with RMB
					mode = 3;
				} else if (e.getButton() == 1 && holdingShift){ //Shift + LMB
					if (points.size() > 4){
						int i = points.size();
						ln.drawLine(points.get(i - 3).getX(), points.get(i-3).getY(), points.get(i-1).getX(), points.get(i-1).getY(), 0x00ff00);
						ln.drawLine(points.get(i - 4).getX(), points.get(i-4).getY(), points.get(i-1).getX(), points.get(i-1).getY(), 0x00ff00);
					}
					panel.repaint();
				} else {
					mode = 0;
				}
			}
		});
		/**
		 * Event that happens when mouse is released
		 */
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1 && !holdingShift){ //When releasing LMB
					points.add(new Point(e.getX(),e.getY()));
					line = false;
				}
                else if (e.getButton() == 2 && !holdingShift){ //When releasing MMB
					mode = 2;
				} else if (e.getButton() == 3 && !holdingShift){ //When releasing RMB
					mode = 3;
				} else {
					mode = 0;
				}

				draw();
				if (pressedP){ //If P has been pressed -> redraws the polygon
					redrawPolygon();
				}
				panel.repaint();

			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			/**
			 * Event that happens when mouse is dragged on the panel
			 * @param e the event to be processed
			 */
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
				} else if (mode == 3 && points.size() > 0) { //When holding the RMB it draws a dashed line
					int i = points.size();
					ln.drawDashLine(points.get(i - 1).getX(), points.get(i - 1).getY(), e.getX(), e.getY(), 0xffff00);
					ln.drawDashLine(points.get(0).getX(), points.get(0).getY(), e.getX(), e.getY(), 0xffff00);
				}
				panel.repaint();
			}

			/**
			 * When mouse is moved
			 * @param e the event to be processed
			 */
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

	/**
	 * Redraws the lines onto the canvas that form polygons from the first instance(holding LMB) and also from MMB
	 */
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

	/**
	 * Redraws a polygon if there is any onto the canvas
	 */
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
		} else {
			modeP = "Off";
		}
		img.getGraphics().drawString("LMB - For drawing Polygons with Full Lines, Either click or dragg | RMB - Drawing with dashed lines - Holding the right mouse button to draw dashed lines and left click to accept | C - clear the canvas",5,img.getHeight() - 35);
		img.getGraphics().drawString("MMB - For drawing white lines starting from [0,0] with FilledLineRasterizer class, P[" + modeP + "] - Is a mode for duplicating a second polygon with red color, moved by 50points with the Polygon Class",5,img.getHeight() - 20);
		img.getGraphics().drawString("SHIFT + LMB - Not exactly working but if you hold shift and left click at the same time it will show you some lines from point to another - they are supposed to represent diagonals(Only if you have more than 5 edges)",5,img.getHeight() - 5);
	}

	public void start() {
		x = img.getWidth() / 2;
		y = img.getHeight() / 2;
		draw();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(1200, 600).start());
	}

}