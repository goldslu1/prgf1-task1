package rasterop;

import objectdata.Line;
import objectdata.Point;
import rasterdata.Raster;

import java.awt.*;

/**
 * Represents an algorithm for drawing straight lines into Raster
 */
public interface Liner {

    /**
     * Draws a straight line represented by 2 points onto the given Raster using the given color
     * @param x1 x coordinate of the first point; values ranging from 0 to width of the Raster
     * @param y1 y coordinate of the first point; values ranging from 0 to height of the Raster
     * @param x2 x coordinate of the second point; values ranging from 0 to width of the Raster
     * @param y2 y coordinate of the second point; values ranging from 0 to height of the Raster
     * @param color color of the newly drawn line
     */
    void drawLine(double x1, double y1, double x2, double y2, int color);

    /**
     * Draws a dashed line represented by 2 points onto the given Raster using the given color
     * @param x1 x coordinate of the first point; values ranging from 0 to width of the Raster
     * @param y1 y coordinate of the first point; values ranging from 0 to height of the Raster
     * @param x2 x coordinate of the second point; values ranging from 0 to width of the Raster
     * @param y2 y coordinate of the second point; values ranging from 0 to height of the Raster
     * @param color color of the newly drawn line
     */
    void drawDashLine(double x1, double y1, double x2, double y2, int color);

    /**
     * Draws a straight line represented by 2 points
     * @param p1 first point of the line
     * @param p2 second point of the line
     * @param color color of the newly drawn line
     */
    default void drawLine(Point p1, Point p2, int color){
        drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
    }

    /**
     * Draws a straight line represented by a line
     * @param line represents an instance of Line, and a line to be drawn
     * @param color color of the newly drawn line
     */
    default void drawLine(Line line, int color){
        drawLine(line.p1.getX(), line.p1.getY(), line.p2.getX(), line.p2.getY(), color);
    }
}
