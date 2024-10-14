package objectdata;

/**
 * Represents a point in a 2D space; immutable
 */
public class Point {

    public double x;
    public double y;

    public Point(final double x, final double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for returning the first coordinate in a point
     * @return double x
     */
    public double getX(){
        return x;
    }

    /**
     * Getter for returning the second coordinate in a point
     * @return double y
     */
    public double getY(){
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
