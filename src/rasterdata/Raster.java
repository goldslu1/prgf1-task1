package rasterdata;

import java.util.Optional;

/**
 * Represents a two-dimensional raster with pixels of type int
 */
public interface Raster {

    /**
     * Returns the number of columns in this raster
     * @return
     */
    int getWidth();

    /**
     * returns the number of rows in this raster
     * @return
     */
    int getHeight();

    /**
     * Sets the pixel at the specified address to the provided color value
     * @param c column address
     * @param r row address
     * @param color new color value
     * @return true if the provided address was valid; false otherwise
     */
    boolean setColor(int c, int r, int color);

    /**
     * Returns the pixel value at the specified address
     * @param c column address
     * @param r row address
     * @return Optional of the pixel value if the provided address was valid; empty Optional otherwise
     */
    Optional<Integer> getColor(int c, int r);

    /**
     * Sets all pixels of thi image to the provided color
     * @param background
     */
    void clear(int background);
}
