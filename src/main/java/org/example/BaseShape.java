package org.example;

import java.awt.*;
import java.io.Serializable;

public abstract class BaseShape implements Serializable {
    protected int x1, y1, x2, y2;
    protected Color color;
    private static Color defaultColor = Color.BLACK;

    public abstract String getName();

    public BaseShape(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public abstract void draw(Graphics g);
    public abstract boolean contains(int x, int y);
    public abstract void setEndCoordinates(int x, int y);
    public abstract void moveBy(int dx, int dy);
    public abstract BaseShape copy();
    public abstract String getBounds();
    public abstract void highlight(Graphics g);

    // Add getter methods
    public int getX() {
        return Math.min(x1, x2);
    }

    public int getY() {
        return Math.min(y1, y2);
    }

    public int getWidth() {
        return Math.abs(x2 - x1);
    }

    public int getHeight() {
        return Math.abs(y2 - y1);
    }

    public static Color getDefaultColor() {
        return defaultColor;
    }

    public static void setDefaultColor(Color newColor) {
        defaultColor = newColor;
    }
}
