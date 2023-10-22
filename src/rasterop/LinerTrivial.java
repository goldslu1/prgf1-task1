package rasterop;

import rasterdata.Raster;
import rasterdata.RasterBI;
import java.awt.BasicStroke;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

/**
 * Represents a trivial algorithm for drawing lines
 */
public class LinerTrivial implements Liner{
    private RasterBI img;
    private int color;

    public LinerTrivial(RasterBI img) {
        this(img, 0xffff00);
    }
    public LinerTrivial(RasterBI img, int color) {
        this.img = img;
        this.color = color;
    }
    @Override
    public void drawLine(double x1, double y1, double x2, double y2, int color) {
        double temp;
        double k;
        double q;
        double y;

        if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
        if (x2 < x1) {
            temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }

            k = (y2 - y1) / (x2 - x1);
            q = y1 - (k * x1);

            for (int x = (int) Math.round(x1); x < x2; x++) {
                y = k * x + q;
                img.setColor(x, (int) y, color);
            }
        } else {
            if (y2 < y1) {
                temp = x1;
                x1 = x2;
                x2 = temp;

                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            k = (x2 - x1) / (y2 - y1);
            q = x1 - (k * y1);

            for (int x = (int) Math.round(y1); x < y2; x++) {
                y = k * x + q;
                img.setColor((int) y, x, color);
            }
        }
    }

    @Override
    public void drawDashLine(double x1, double y1, double x2, double y2, int color) {
        double temp;
        double k;
        double q;
        double y;
        int odd = 0;
        int even = 0;
        boolean dots = false;

        if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
            if (x2 < x1) {
                temp = x1;
                x1 = x2;
                x2 = temp;

                temp = y1;
                y1 = y2;
                y2 = temp;
            }

            k = (y2 - y1) / (x2 - x1);
            q = y1 - (k * x1);

            for (int x = (int) Math.round(x1); x < x2; x++) {
                y = k * x + q;
                if (dots == false) {
                    even++;
                    img.setColor(x, (int) y, color);
                    if (even == 10){
                        even = 0;
                        dots = true;
                    }
                }
                else {
                    odd++;
                    if (odd == 10){
                        odd = 0;
                        dots = false;
                    }
                }
            }
        } else {
            if (y2 < y1) {
                temp = x1;
                x1 = x2;
                x2 = temp;

                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            k = (x2 - x1) / (y2 - y1);
            q = x1 - (k * y1);

            for (int x = (int) Math.round(y1); x < y2; x++) {
                y = k * x + q;
                if (dots == false) {
                    even++;
                    img.setColor((int)y, x, color);
                    if (even == 10){
                        even = 0;
                        dots = true;
                    }
                }
                else {
                    odd++;
                    if (odd == 10){
                        odd = 0;
                        dots = false;
                    }
                }
            }
        }

    }
}
