package org.example.model;

import java.awt.*;

public class Rectangle extends BaseShape {

    public Rectangle(int id, int x1, int y1, int x2, int y2, Color color) {
        super(id, x1, y1, x2, y2, color);
    }

    public String getName() {
        return "Rectangle";
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) &&
                y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    }

    @Override
    public void setEndCoordinates(int x, int y) {
        this.x2 = x;
        this.y2 = y;
    }

    @Override
    public void moveBy(int dx, int dy) {
        this.x1 += dx;
        this.y1 += dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    @Override
    public BaseShape copy(int i) {
        return new Rectangle(id + 1, x1, y1, x2, y2, color);
    }
    @Override
    public void highlight(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED); // Highlight color
        g2d.setStroke(new BasicStroke(2)); // Thicker stroke
        g2d.drawRect(Math.min(x1, x2) - 2, Math.min(y1, y2) - 2, Math.abs(x2 - x1) + 4, Math.abs(y2 - y1) + 4);
    }
    @Override
    public String getBounds() {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }

}
