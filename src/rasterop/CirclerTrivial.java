package rasterop;

import rasterdata.Raster;

public class CirclerTrivial implements Circler{
    @Override
    public void drawCircle(Raster img, double x1, double y1, double x2, double y2, int color){

        double R = Math.sqrt((x2 - x1) + (y2 - y1));
        double y;

        for(int x = 50; x <= R; x++) {
            y = Math.sqrt(R*R-x*x);
            img.setColor(x, (int) y, color);
        }
    }
}