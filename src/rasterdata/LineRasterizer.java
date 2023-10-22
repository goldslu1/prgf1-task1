package rasterdata;

import java.awt.*;
import objectdata.Line;

/**
 * Represents another way to draw a line onto 2D raster
 */
public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    /**
     * Sets the color of the line
     * @param color color of the line from class Color
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Draws a line from already defined instance of Class Line
     * @param line Represents the instance from class line
     */
    public void rasterize(Line line){
        this.drawLine(line.p1.getX(), line.p1.getY(), line.p2.getX(), line.p2.getY());
    }

    /**
     * Draws a line onto the 2D raster by defining coordinates and the color
     * @param x1 Represents the x from the first point
     * @param y1 Represents the y from the first point
     * @param x2 Represents the x from the second point
     * @param y2 Represents the y from the second point
     * @param color color of the line from the Color class
     */
    public void rasterize(double x1, double y1, double x2, double y2, Color color){
        this.setColor(color);
        this.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws a line onto the 2D raster by defining coordinates
     * @param x1 Represents the x from the first point
     * @param y1 Represents the y from the first point
     * @param x2 Represents the x from the second point
     * @param y2 Represents the y from the second point
     */
    protected void drawLine(double x1, double y1, double x2, double y2){}

}
