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
	private Polygon polygon;
	private int selectedVertex = -1;
	private final int clickThreshhold = 10;

	private boolean thickLineMode = false;
	private int lineThickness = 5;

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
		polygon = new Polygon(new ArrayList<Point>(), 0);
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				img.present(g);

				//polygon.drawPolygon(ln, 0xffff00);
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
					pressedP = false;
					img.present(panel.getGraphics());
					panel.repaint();
					draw();
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
							p.drawPolygon(ln, 0xffff00);

						}
						panel.repaint();
						pressedP = true;
					} else {
						panel.repaint();
						pressedP = false;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_T) {
					thickLineMode = !thickLineMode;
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
					rasterizer.drawLine(x, y, e.getX(), e.getY());
					pointsR.add(new Point(e.getX(), e.getY()));
					x = e.getX();
					y = e.getY();
				} else if (e.getButton() == 3 && !holdingShift){ //When clicking with RMB
					mode = 3;

					selectedVertex = polygon.findClosestVertex(e.getX(), e.getY(), clickThreshhold);
					if (selectedVertex == -1) {
						// If no vertex is found within the threshold, look for the nearest edge to add a new vertex
						addVertexToClosestEdge(e.getX(), e.getY());

					}
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
					selectedVertex = -1;
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

					Point firstPoint = points.get(points.size() - 1);

					double x2 = e.getX();
					double y2 = e.getY();
					double x1 = firstPoint.getX();
					double y1 = firstPoint.getY();

					redrawLine();
					if (points.size() > 0) {
						int i = points.size();
						if (holdingShift) {
								double deltaX = x2 - x1;
								double deltaY = y2 - y1;

							if (Math.abs(deltaX) > Math.abs(deltaY)) {
								// Snap to horizontal
								y2 = y1;  // Keep y constant for horizontal line
							} else if (Math.abs(deltaY) > Math.abs(deltaX)) {
								// Snap to vertical
								x2 = x1;  // Keep x constant for vertical line
							} else {
								// Snap to 45-degree diagonal (diagonal line)
								if (Math.abs(deltaX) > Math.abs(deltaY)) {
									y2 = y1 + deltaX;  // Ensure 45-degree slope
								} else {
									x2 = x1 + deltaY;
								}
							}
						}
						if (thickLineMode) {
							ln.drawThickLine(x1, y1, x2, y2, lineThickness, 0xffff00);
						} else {
							ln.drawLine(x1, y1, x2, y2, 0xffff00);
						}
					} else if (mode == 3) { //When holding the RMB

						polygon.updateVertex(selectedVertex, e.getX(), e.getY());
				}


				panel.repaint();
				}
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
				//ln.drawLine(points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(),points.get(i).getX(), points.get(i).getY(),0xffff00);
				ln.drawThickLine(points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(),points.get(i).getX(), points.get(i).getY(),lineThickness, 0xffff00);
			}
		}
		if (pointsR.size() > 1){
			for (int i = 0; i < pointsR.size()-1; i++) {
				rasterizer.drawLine((int)pointsR.get(i).getX(), (int)pointsR.get(i).getY(), (int)pointsR.get(i + 1).getX(), (int)pointsR.get(i + 1).getY());
			}
		}
	}

	/**
	 * Redraws a polygon if there is any onto the canvas
	 */
	public void redrawPolygon(){
		if (points.size() > 1 && p != null){
			p.drawPolygon(ln, 0xffff00);
		}
	}

	private void addVertexToClosestEdge(int x, int y) {
		double minDistance = Double.MAX_VALUE;
		int insertIndex = -1;
		Point newVertex = new Point(x, y);

		// Loop through edges to find the closest edge
		for (int i = 0; i < points.size(); i++) {
			Point p1 = points.get(i);
			Point p2 = points.get((i + 1) % points.size()); // Wrap around to the first point

			// Calculate distance from point (x, y) to the current edge
			double distance = distanceToSegment(p1, p2, newVertex);
			if (distance < minDistance) {
				minDistance = distance;
				insertIndex = i;
			}
		}

		// Insert the new vertex at the closest edge
		if (insertIndex != -1) {
			points.add(insertIndex + 1, newVertex);
		}
	}

	// Utility method to calculate the distance from a point to a segment
	private double distanceToSegment(Point p1, Point p2, Point p) {
		double xDelta = p2.getX() - p1.getX();
		double yDelta = p2.getY() - p1.getY();

		if ((xDelta == 0) && (yDelta == 0)) {
			return p.distance(p1);
		}

		double u = ((p.getX() - p1.getX()) * xDelta + (p.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
		u = Math.max(0, Math.min(1, u));

		double closestX = p1.getX() + u * xDelta;
		double closestY = p1.getY() + u * yDelta;

		return p.distance(new Point(closestX, closestY));
	}


	public void draw() {
		img.clear(background);
		redrawLine();

		int startY = img.getHeight()/3;
		int lineHeight = 15;

		img.getGraphics().drawString("=== Controls ===", 5, startY);
		startY += lineHeight;

		img.getGraphics().drawString("LMB (Left Mouse Button):", 5, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Draw lines and turn them into a polygon.", 20, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Click or drag to see the line follow your cursor.", 20, startY);
		startY += lineHeight + 5;

		img.getGraphics().drawString("MMB (Middle Mouse Button):", 5, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Draw lines using Bresenham's algorithm.", 20, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Starts from position (0, 0) (top-left corner).", 20, startY);
		startY += lineHeight + 5;

		img.getGraphics().drawString("RMB (Right Mouse Button):", 5, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Find and add a point to the closest vertex of the polygon.", 20, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Click where you want to add the point.", 20, startY);
		startY += lineHeight + 5;

		img.getGraphics().drawString("C Key:", 5, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Clear the entire canvas.", 20, startY);
		startY += lineHeight + 5;

		img.getGraphics().drawString("SHIFT + LMB:", 5, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Draw horizontal or vertical lines.", 20, startY);
		startY += lineHeight;
		img.getGraphics().drawString("- Drag to start a line, hold SHIFT to snap to horizontal or vertical.", 20, startY);
	}

	public void start() {
		x = img.getWidth() / 2;
		y = img.getHeight() / 2;
		draw();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(1400, 800).start());
	}

}