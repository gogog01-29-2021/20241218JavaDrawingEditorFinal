package org.example.model;

import java.awt.*;

public class Circle extends BaseShape {

    public Circle(int id, int x1, int y1, int x2, int y2, Color color) {
        super(id, x1, y1, x2, y2, color);
    }

    public String getName() {
        return "Circle";
    }

    @Override
    public void draw(Graphics g) {
        // Calculate center and radius
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radius = (int) Math.round(Point.distance(x1, y1, x2, y2) / 2);
        color = getDefaultColor();
        // Set color and draw the circle
        g.setColor(getDefaultColor());
        g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    @Override
    public void redraw(Graphics g) {
        // Calculate center and radius
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radius = (int) Math.round(Point.distance(x1, y1, x2, y2) / 2);

        // Set color and draw the circle
        g.setColor(color);
        g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    @Override
    public boolean contains(int x, int y) {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radius = (int) Math.round(Point.distance(x1, y1, x2, y2) / 2);
        return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius * radius;
    }
    @Override
    public void highlight(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2)); // Thicker stroke
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radius = (int) Math.round(Point.distance(x1, y1, x2, y2) / 2);
        g2d.drawOval(centerX - radius - 2, centerY - radius - 2, radius * 2 + 4, radius * 2 + 4);
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
        return new Circle(id + i, x1, y1, x2, y2, color);
    }
    @Override
    public String getBounds() {
        int radius = (int) Math.hypot(x2 - x1, y2 - y1);
        return "[Center: (" + x1 + ", " + y1 + "), Radius: " + radius + "]";
    }

}
