package rasterop;

import rasterdata.Raster;

/**
 * Represents a trivial algorithm for drawing lines
 */
public class LinerTrivial implements Liner{
    @Override
    public void drawLine(Raster img, double x1, double y1, double x2, double y2, int color) {

        if (x2 < x1) {
          double temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;

            drawLine(img, x1, y1, x2, y2, color);
        } else if (y2 < y1) {
            double temp = y1;
            y1 = y2;
            y2 = temp;

            temp = x1;
            x1 = x2;
            x2 = temp;

            drawLine(img, x1, y1, x2, y2, color);
        } else {

            double k = (y2 - y1) / (x2 - x1);
            double q = y1 - (k * x1);

            double y;

            for (int x = (int) Math.round(x1);x < x2; x++) {
                y = k * x + q;
                img.setColor(x, (int) y, color);
            }
        }
        //TODO dodelat implementaci pro vsechny kvadranty
    }
}
