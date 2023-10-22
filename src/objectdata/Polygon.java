package objectdata;


import rasterdata.RasterBI;
import rasterop.LinerTrivial;

import java.util.List;

/**
 * Represent a polygon defined a list of edges in 2D space
 */
public class Polygon {
    List<Point> edges;
    int color;
    LinerTrivial ln;
    public Polygon(List<Point> edges, int color){
        this.edges = edges;
        this.color = color;
    }

    /**
     * Getting for returning a list with Points
     * @return List of edges
     */
    public List<Point> getEdges() {
        return edges;
    }

    /**
     * Setter for setting the List of edges
     * @param edges List of edges
     */
    public void setEdges(List<Point> edges) {
        this.edges = edges;
    }

    /**
     * Getter for returning color of this polygon
     * @return int color
     */
    public int getColor() {
        return color;
    }

    /**
     * Setter for setting the color of this polygon
     * @param color numeric representation of a color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Draws the polygon onto the 2D raster
     * @param img represents the raster on which is the polygon drawn
     */
    public void drawPolygon(RasterBI img){
        ln = new LinerTrivial(img);
        for (int i = 0; i < edges.size(); ++i){
            ln.drawLine(edges.get((i+1) % edges.size()).getX()+50, edges.get((i+1) % edges.size()).getY()+50, edges.get(i).getX()+50, edges.get(i).getY()+50, color);
        }
    }
}
