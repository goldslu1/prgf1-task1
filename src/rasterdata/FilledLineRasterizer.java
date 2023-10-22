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

    @Override
    public void drawDashedLine(double x1, double y1, double x2, double y2) {
        Graphics g = ((RasterBI)raster).getImg().getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine((int) x1,(int) y1,(int) x2,(int) y2);
        g2d.dispose();
    }
}
