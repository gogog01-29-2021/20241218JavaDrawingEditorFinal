package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionRectangle extends BaseShape {
    private int x1, y1, x2, y2;
    private final List<BaseShape> selectedShapes = new ArrayList<>();

    private int mouseOffsetX, mouseOffsetY;
    public SelectionRectangle(int x1, int y1, Color color) {
        super(-1, x1, y1, x1, y1, color);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1;  // Initially, x2 and y2 will be the same as x1 and y1
        this.y2 = y1;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        highlightSelectedShapes(g);
    }

    @Override
    public void setEndCoordinates(int x, int y) {
        this.x2 = x;
        this.y2 = y;
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        this.mouseOffsetX = x1 - centerX;
        this.mouseOffsetY = y1 - centerY;
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) &&
                y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    }

    public int[] getSelectedShapeIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < selectedShapes.size(); i++) {
            indexes.add(selectedShapes.get(i).id);
        }
        int[] result = new int[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            result[i] = indexes.get(i);
        }
        return result;
    }

    public void updateSelection(List<BaseShape> shapes) {
        selectedShapes.clear();
        for (BaseShape shape : shapes) {
            if (this.contains(shape.getX(), shape.getY()) || this.contains(shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight())) {
                selectedShapes.add(shape);
            }
        }
    }

    private void highlightSelectedShapes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));
        for (BaseShape shape : selectedShapes) {
            shape.highlight(g);
        }
    }

    public List<BaseShape> getSelectedShapes() {
        return selectedShapes;
    }

    @Override
    public String getName() {
        return "SelectionRectangle";
    }

    @Override
    public void moveBy(int dx, int dy) {
        // Not applicable for selection, as this is for dragging to select objects
    }

    @Override
    public BaseShape copy() {
        return new SelectionRectangle(x1, y1, color);
    }

    public void moveSelectedShapes(int dx, int dy) {
        for (BaseShape shape : selectedShapes) {
            shape.moveBy(dx, dy);
        }
    }

    public void updateWhileDragging(int mouseX, int mouseY) {
        int rectCenterX = (x1 + x2) / 2;
        int rectCenterY = (y1 + y2) / 2;
        int deltaX = mouseX - rectCenterX;
        int deltaY = mouseY - rectCenterY;
        x1 += deltaX;
        y1 += deltaY;
        x2 += deltaX;
        y2 += deltaY;
        moveSelectedShapes(deltaX, deltaY);
    }

    @Override
    public void highlight(Graphics g) {
        // Selection rectangle is already highlighted in the draw method
    }

    @Override
    public String getBounds() {
        return "[" + x1 + ", " + y1 + ", " + Math.abs(x2 - x1) + ", " + Math.abs(y2 - y1) + "]";
    }

    // Set the initial coordinates where the user starts the drag
    public void setStartCoordinates(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }
}
