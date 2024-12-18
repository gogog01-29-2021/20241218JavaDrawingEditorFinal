package org.example.model;

import java.awt.*;

public class Line extends BaseShape {
    public Line(int id, int x1, int y1, int x2, int y2, Color color) {
        super(id, x1, y1, x2, y2, color);
    }

    public String getName() {
        return "Line";
    }
    @Override
    public void draw(Graphics g) {
        color = getDefaultColor();
        g.setColor(getDefaultColor());
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void redraw(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void setEndCoordinates(int x, int y) {
        this.x2 = x;
        this.y2 = y;
    }
    @Override
    public boolean contains(int x, int y) {
        double distance = Math.abs((x2 - x1) * (y1 - y) - (x1 - x) * (y2 - y1)) /
                Math.hypot(x2 - x1, y2 - y1);
        double tolerance = 5.0; // Adjust tolerance as needed
        return distance <= tolerance &&
                x >= Math.min(x1, x2) && x <= Math.max(x1, x2) &&
                y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    }
    @Override
    public BaseShape copy(int i) {
        return new Line(id + i, x1, y1, x2, y2, color);
    }

    @Override
    public void moveBy(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
    @Override
    public void highlight(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2)); // Thicker stroke
        g2d.drawLine(x1, y1, x2, y2); // Could enhance with parallel lines
    }
    @Override
    public String getBounds() {
        return "[" + x1 + ", " + y1 + " to " + x2 + ", " + y2 + "]";
    }

}
