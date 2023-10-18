package rasterop;

import objectdata.Line;
import objectdata.Point;
import rasterdata.Raster;

/**
 * Represents an algorith for drawing straight lines into Raster
 */
public interface Liner {

    /**
     * Draws a straight line represented by 2 points onto the given Raster using the given color
     * @param img Raster to be used for drawing
     * @param x1 x coordinate of the first point; values ranging from 0 to width of the Raster
     * @param y1 y coordinate of the first point; values ranging from 0 to height of the Raster
     * @param x2 x coordinate of the second point; values ranging from 0 to width of the Raster
     * @param y2 y coordinate of the second point; values ranging from 0 to height of the Raster
     * @param color color of the newly drawn line
     */
    void drawLine(Raster img, double x1, double y1, double x2, double y2, int color);

    default void drawLine(Raster img, Point p1, Point p2, int color){
        //TODO
        drawLine(img, p1.x, p1.y, p2.x, p2.y, color);
    }

    default void drawLine(Raster img, Line line, int color){
        //TODO
        drawLine(img, line.p1.x, line.p1.y, line.p2.x, line.p2.y, color);
    }
}
