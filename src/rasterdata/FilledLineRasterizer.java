package rasterdata;

import java.awt.*;

public class FilledLineRasterizer extends LineRasterizer{

    public FilledLineRasterizer(Raster raster){
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        Graphics g = ((RasterBI)raster).getImg().getGraphics();
        g.setColor(this.color);
        g.drawLine(x1, y1, x2, y2);
    }
}
