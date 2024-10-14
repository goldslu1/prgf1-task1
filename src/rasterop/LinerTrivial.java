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

    public void plot(double x, double y, int color) {
        // Assuming you have a method in your image buffer to set pixel color
        img.setPixel((int) Math.round(x), (int) Math.round(y), color);
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

    @Override
    public void drawThickLine(double x1, double y1, double x2, double y2, int thickness, int color) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        // Length of the line
        double length = Math.sqrt(dx * dx + dy * dy);

        // Calculate the unit vector perpendicular to the line
        double perpX = -dy / length;
        double perpY = dx / length;

        // Draw multiple parallel lines around the original line to simulate thickness
        for (int i = -thickness / 2; i <= thickness / 2; i++) {
            // Offset the original line by "i" in both perpendicular directions
            int offsetX = (int) (i * perpX);
            int offsetY = (int) (i * perpY);

            // Draw the offset line
            drawLine(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY, color);
        }

        // Round the ends by drawing circles at the start and end points
        drawCircle(x1, y1, thickness / 2, color); // Start cap
        drawCircle(x2, y2, thickness / 2, color); // End cap
    }

    public void drawCircle(double cx, double cy, double radius, int color) {
        // Implement a simple filled circle drawing algorithm, e.g., Bresenham's circle
        int r = (int) radius;
        int x = r, y = 0;
        int decisionOver2 = 1 - x;   // Decision criterion divided by 2 evaluated at x=r, y=0

        while (y <= x) {
            // Draw horizontal lines to fill the circle
            drawHorizontalLine(cx - x, cy + y, 2 * x, color);
            drawHorizontalLine(cx - x, cy - y, 2 * x, color);
            drawHorizontalLine(cx - y, cy + x, 2 * y, color);
            drawHorizontalLine(cx - y, cy - x, 2 * y, color);

            y++;
            if (decisionOver2 <= 0) {
                decisionOver2 += 2 * y + 1;   // Change decision criterion for y -> y+1
            } else {
                x--;
                decisionOver2 += 2 * (y - x) + 1;   // Change for y -> y+1, x -> x-1
            }
        }
    }

    // Method to draw a horizontal line, helper for drawing the circle
    public void drawHorizontalLine(double x, double y, int length, int color) {
        for (int i = 0; i < length; i++) {
            // Plot each pixel (assuming you have a method to plot points)
            plot(x + i, y, color);
        }
    }
}
