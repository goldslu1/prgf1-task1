package objectdata;


import rasterdata.RasterBI;
import rasterop.LinerTrivial;

import java.util.List;

public class Polygon {
    List<Point> edges;
    int color;
    LinerTrivial ln;
    public Polygon(List<Point> edges, int color){
        this.edges = edges;
        this.color = color;
    }

    public List<Point> getEdges() {
        return edges;
    }

    public void setEdges(List<Point> edges) {
        this.edges = edges;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void drawPolygon(RasterBI img){
        ln = new LinerTrivial(img);
        for (int i = 0; i < edges.size(); ++i){
            ln.drawLine(edges.get((i+1) % edges.size()).getX()+50, edges.get((i+1) % edges.size()).getY()+50, edges.get(i).getX()+50, edges.get(i).getY()+50, color);
        }
    }
}
