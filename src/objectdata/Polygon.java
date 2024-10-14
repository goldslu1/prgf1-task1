package objectdata;


import rasterdata.RasterBI;
import rasterop.LinerTrivial;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a polygon defined a list of edges in 2D space
 */
public class Polygon {
    private List<Point> vertices;
    private int color;

    public Polygon(List<Point> vertices, int color) {
        this.vertices = new ArrayList<>(vertices);
        this.color = color;
    }

    public void addVertex(Point p){
        vertices.add(p);
    }

    public int findClosestVertex(int x, int y, int threshold) {
        int closestIndex = -1;
        double closestDistance = threshold;

        for (int i = 0; i < vertices.size(); i++) {
            Point vertex = vertices.get(i);
            double distance = Math.sqrt(Math.pow(x - vertex.getX(), 2) + Math.pow(y - vertex.getY(), 2));

            if (distance < closestDistance) {
                closestIndex = i;
                closestDistance = distance;
            }
        }
        return closestIndex;
    }

    public void updateVertex(int index, int x, int y) {
        if (index >= 0 && index < vertices.size()) {
            vertices.get(index).setX(x);
            vertices.get(index).setY(y);
        }
    }

    public void drawPolygon(LinerTrivial ln, int color) {
        for (int i = 0; i < vertices.size(); i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % vertices.size());
            ln.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
        }
    }

    public void setEdges(List<Point> vertices) {
        this.vertices = new ArrayList<>(vertices);
    }
}
