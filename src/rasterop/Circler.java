package rasterop;

import objectdata.Circle;
import objectdata.Point;
import rasterdata.Raster;

public interface Circler {
    void drawCircle(Raster img, double x1, double y1, double x2, double y2, int color);

    default void drawCircle(Raster img, Point p1, Point p2, int color){
        drawCircle(img, p1.x, p1.y, p2.x, p2.y, color);
    }

    default void drawCircle(Raster img, Circle circle, int color){
        drawCircle(img, circle.p1.x, circle.p1.y, circle.p2.x, circle.p2.y, color);
    }
}
