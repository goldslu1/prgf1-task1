package objectdata;

/**
 * Represents a line in a 2D space; immutable
 */
public class Line {
    public final Point p1;
    public final Point p2;

    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }
}
