package rasterdata;

import java.awt.*;
import java.awt.BasicStroke;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class FilledLineRasterizer extends LineRasterizer{

    public FilledLineRasterizer(Raster raster){
        super(raster);
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        Graphics g = ((RasterBI)raster).getImg().getGraphics();
        g.setColor(this.color);
        g.drawLine((int) x1, (int) y1,(int) x2,(int) y2);
    }
}
