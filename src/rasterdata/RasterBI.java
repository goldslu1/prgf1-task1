package rasterdata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class RasterBI implements Raster, Presentable{
    private final BufferedImage img;
    private int color;

    public RasterBI(int width, int height) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    public void draw(RasterBI raster) {
        Graphics graphics = getGraphics();
        graphics.setColor(new Color(color));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
    }
    public BufferedImage getImg() {
        return img;
    }
    public Graphics getGraphics(){
        return img.getGraphics();
    }
    @Override
    public int getPixel(int x, int y) {
        return img.getRGB(x, y);
    }
    @Override
    public void setPixel(int x, int y, int color) {
        img.setRGB(x, y, color);
    }
    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    @Override
    public boolean setColor(int c, int r, int color) {
        if (c >= 0 && c < getWidth() && r >= 0 && r < getHeight()) {
            img.setRGB(c, r, color);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Integer> getColor(int c, int r) {
        if  (c >= 0 && c < getWidth() && r >= 0 && r < getHeight()) {
            return Optional.of(img.getRGB(c, r));
        }
        return Optional.empty();
    }

    @Override
    public void clear(int background) {
        Graphics gr= img.getGraphics();
        gr.setColor(new Color(background));
        gr.fillRect(0,0, img.getWidth(), img.getHeight());
    }

    @Override
    public void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }
}
