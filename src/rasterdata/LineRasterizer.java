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

    }

    public void rasterize(int x1, int y1, int x2, int y2, Color color){

    }

    protected void drawLine(int x1, int y1, int x2, int y2){

    }
}
