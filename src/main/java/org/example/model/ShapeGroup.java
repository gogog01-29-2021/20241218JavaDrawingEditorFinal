package org.example.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends BaseShape {
    private final List<BaseShape> shapes;

    public ShapeGroup(int id) {
        super(id, 0, 0, 0, 0, getDefaultColor());
        this.shapes = new ArrayList<>();
    }

    public void addShape(BaseShape shape) {
        shapes.add(shape);
    }

    public void removeShape(BaseShape shape) {
        shapes.remove(shape);
    }

    public List<BaseShape> getShapes() {
        return shapes;
    }

    @Override
    public String getName() {
        return "Group";
    }

    @Override
    public void draw(Graphics g) {
        for (BaseShape shape : shapes) {
            shape.draw(g);
        }
    }

    @Override
    public void redraw(Graphics g) {

        for (BaseShape shape : shapes) {
            shape.redraw(g);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        for (BaseShape shape : shapes) {
            if (shape.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setEndCoordinates(int x, int y) {
        // Groups don't have an explicit end coordinate, so propagate to individual shapes
        for (BaseShape shape : shapes) {
            shape.setEndCoordinates(x, y);
        }
    }

    @Override
    public void moveBy(int dx, int dy) {
        for (BaseShape shape : shapes) {
            shape.moveBy(dx, dy);
        }
    }

    @Override
    public BaseShape copy(int i) {
        ShapeGroup copy = new ShapeGroup(id);
        for (BaseShape shape : shapes) {
            copy.addShape(shape.copy(i));
        }
        return copy;
    }

    @Override
    public String getBounds() {
        // Calculate bounding box for the group
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (BaseShape shape : shapes) {
            minX = Math.min(minX, shape.getX());
            minY = Math.min(minY, shape.getY());
            maxX = Math.max(maxX, shape.getX() + shape.getWidth());
            maxY = Math.max(maxY, shape.getY() + shape.getHeight());
        }

        return "Bounds: [(" + minX + ", " + minY + "), (" + maxX + ", " + maxY + ")]";
    }

    @Override
    public Point[] getBoundsXY() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (BaseShape shape : shapes) {
            if (shape instanceof ShapeGroup) {
                // Recursive call for ShapeGroup to get its bounds
                Point[] groupBounds = shape.getBoundsXY();
                minX = Math.min(minX, groupBounds[0].x);
                minY = Math.min(minY, groupBounds[0].y);
                maxX = Math.max(maxX, groupBounds[1].x);
                maxY = Math.max(maxY, groupBounds[1].y);
            } else {
                // Simple shape bounds calculation
                minX = Math.min(minX, shape.getX());
                minY = Math.min(minY, shape.getY());
                maxX = Math.max(maxX, shape.getX() + shape.getWidth());
                maxY = Math.max(maxY, shape.getY() + shape.getHeight());
            }
        }
        return new Point[]{new Point(minX, minY), new Point(maxX, maxY)};
    }

    @Override
    public void highlight(Graphics g) {
        for (BaseShape shape : shapes) {
            shape.highlight(g);
        }
    }
}