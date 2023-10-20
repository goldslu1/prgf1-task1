package objectdata;

/**
 * Represents a point in a 2D space; immutable
 */
public class Point {

    public final double x;
    public final double y;

    public Point(final double x, final double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
