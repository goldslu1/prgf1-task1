package rasterdata;

import java.awt.*;
import objectdata.Line;

public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void setColor(Color color){
        this.color = color;
    }
    public void setColor(int color) {
        this.color = new Color(color);
    }

    public void rasterize(Line line){
        this.drawLine(line.p1.getX(), line.p1.getY(), line.p2.getX(), line.p2.getY());
    }

    public void rasterize(double x1, double y1, double x2, double y2, Color color){
        this.setColor(color);
        this.drawLine(x1, y1, x2, y2);
    }

    protected void drawLine(double x1, double y1, double x2, double y2){}
    protected void drawDashedLine(double x1, double y1, double x2, double y2){}
}
