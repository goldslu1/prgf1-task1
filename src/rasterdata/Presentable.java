package rasterdata;

import java.awt.*;

/**
 * Represents a data structure capable of drawing itself on a given Graphics
 */
public interface Presentable {

    /**
     * Draws itself onto the given Graphics object
     * @param graphics
     */
    void present(Graphics graphics);
}
