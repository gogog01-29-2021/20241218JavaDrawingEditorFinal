package org.example;

import java.awt.*;

public abstract class BaseShape {
    protected int x1, y1, x2, y2;

    public BaseShape(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setEndCoordinates(int x2, int y2) {
        this.x2 = x2;
        this.y2 = y2;
    }

    public abstract void draw(Graphics g);
}
