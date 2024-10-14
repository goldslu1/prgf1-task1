package rasterdata;

import java.awt.*;
import java.awt.BasicStroke;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class FilledLineRasterizer extends LineRasterizer{
    private RasterBI img;

    public FilledLineRasterizer(RasterBI img){
        this.img = img;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) { /*Bresenham's Line Algorithm*/
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            img.setColor(x1, y1, 0xffffff);

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }
}
