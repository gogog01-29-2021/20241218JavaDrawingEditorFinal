package org.example;

import org.example.model.*;
import org.example.model.Rectangle;
import org.example.ui.LayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;

public class Canvas extends JPanel {
    private String currentTool = "select";
    private boolean isShapesSelected = false;
    private BaseShape tempShape;
    private List<BaseShape> tempShapes;
    private BaseShape selectedShape;
    private Color currentColor = BaseShape.getDefaultColor();
    private int lastX, lastY;
    private final LayerManager layerManager;
    private final JLabel statusBar;
    private final LayerPanel layerPanel;
    private SelectionRectangle selectionRectangle;
    private int shapeCounter = 0;

    private int groupCounter = 0;
    private boolean isMoved = false;

    public Canvas(LayerManager layerManager, JLabel statusBar, LayerPanel layerPanel) {
        this.layerManager = layerManager;
        this.statusBar = statusBar;
        this.layerPanel = layerPanel;

        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    // Override paintComponent to draw both permanent shapes and the placeholder line
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw all permanent shapes
        for (BaseShape shape : layerManager.getActiveLayer().getShapes()) {
            shape.redraw(g);
        }

        // Highlight the selected shape, if any
        if (selectedShape != null) {
            selectedShape.highlight(g);
        }

        // Draw the temporary shape (line preview) if it exists
        if (tempShape != null) {
            tempShape.draw(g);  // This shows the placeholder line while dragging
        }

        if (selectionRectangle != null) {
            selectionRectangle.draw(g);
        }
    }

    private void handleMousePressed(MouseEvent e) {
        if ("select".equals(currentTool)) {
            if (!isShapesSelected) {
                selectionRectangle = new SelectionRectangle(e.getX(), e.getY(), Color.BLACK);
            }
        } else {
            selectedShape = null;
            // Loop through shapes in the active layer to check for selection
            for (BaseShape shape : layerManager.getActiveLayer().getShapes()) {
                if (shape.contains(e.getX(), e.getY())) {
                    selectedShape = shape;
                    lastX = e.getX();
                    lastY = e.getY();
                    layerPanel.setSelected(shape.getId());
                    return; // Early exit if a shape is selected
                }
            }

            // If no shape is selected, and a drawing tool is active, create a new shape
            if (currentTool != null) {
                int i = layerManager.getActiveLayer().getShapes().size();
                switch (currentTool) {
                    case "line" ->
                            tempShape = new Line(i, e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                    case "rectangle" ->
                            tempShape = new Rectangle(i, e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                    case "circle" ->
                            tempShape = new Circle(i, e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                    default -> tempShape = null; // Ensure tempShape is null if the tool is unrecognized
                }
            }
        }
        repaint(); // Trigger repaint to update the UI
    }


    private void handleMouseDragged(MouseEvent e) {
        if (isShapesSelected){
            selectionRectangle.updateWhileDragging(e.getX(), e.getY());
            isMoved = true;
        } else if (selectionRectangle != null) {
            selectionRectangle.setEndCoordinates(e.getX(), e.getY());
            selectionRectangle.updateSelection(layerManager.getActiveLayer().getShapes());
            layerPanel.setSelectedIndexes(selectionRectangle.getSelectedShapeIndexes());
            selectedShape = null;
        } else if (selectedShape != null) {
            selectedShape.moveBy(e.getX() - lastX, e.getY() - lastY);
            lastX = e.getX();
            lastY = e.getY();
        } else if (tempShape != null) {
            tempShape.setEndCoordinates(e.getX(), e.getY());
        }
        repaint();
    }

    private void handleMouseReleased(MouseEvent e) {
        if (selectionRectangle != null) {
            isShapesSelected = !selectionRectangle.getSelectedShapes().isEmpty();
            if (isShapesSelected && isMoved) {
                selectionRectangle = null;
                isMoved = false;
                isShapesSelected = false;
            } else if (selectionRectangle.getSelectedShapes().isEmpty()) {
                selectionRectangle = null;
            }
            selectedShape = null;
        } else if (tempShape != null) {
            layerManager.getActiveLayer().addShape(tempShape);
            shapeCounter++;
            layerPanel.updateLayerList();
            tempShape = null;
        }
        repaint();
    }

    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("shapes.dat"))) {
            out.writeObject(layerManager.getActiveLayer().getShapes());
            JOptionPane.showMessageDialog(this, "Shapes saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving shapes: " + ex.getMessage());
        }
    }

    public void load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("shapes.dat"))) {
            List<BaseShape> shapes = (List<BaseShape>) in.readObject();
            layerManager.getActiveLayer().setShapes(shapes);
            repaint();
            JOptionPane.showMessageDialog(this, "Shapes loaded successfully!");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading shapes: " + ex.getMessage());
        }
    }

    public void setCurrentTool(String tool) {
        currentTool = tool;
    }

    public void unselect() {
        selectedShape = null;
    }

    public void copyShape() {
        if (selectedShape != null) {
            tempShape = selectedShape.copy(1);
        } else if (selectionRectangle != null) {
            if (selectionRectangle.getSelectedShapes().size() > 0) {
                tempShapes = selectionRectangle.getSelectedShapes();
            }
        }
    }

    public void groupShapes() {
        if (selectionRectangle != null && !selectionRectangle.getSelectedShapes().isEmpty()) {
            ShapeGroup group = new ShapeGroup(shapeCounter-groupCounter);
            for (BaseShape shape : selectionRectangle.getSelectedShapes()) {
                layerManager.getActiveLayer().removeShape(shape);
                group.addShape(shape);
            }
            layerManager.getActiveLayer().addShape(group);
            layerPanel.updateLayerList();
            groupCounter++;
            layerManager.getActiveLayer().updateShapeIds(layerManager.getActiveLayer().getShapes().size());
            isShapesSelected = false;
            selectionRectangle = null;
            repaint();
            JOptionPane.showMessageDialog(this, "Shapes grouped successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "No shapes selected to group.");
        }

    }

    public void pasteShape() {
        if (tempShape != null) {
            BaseShape copiedShape = tempShape.copy(1);
            tempShape = null;
            layerManager.getActiveLayer().addShape(copiedShape);
            layerPanel.updateLayerList();
            shapeCounter++;
            updateStatusBar();
            repaint();
        } else if (tempShapes.size() > 0) {
            for (BaseShape shape: tempShapes) {
                BaseShape copiedShape = shape.copy(tempShapes.size());
                layerManager.getActiveLayer().addShape(copiedShape);
                layerPanel.updateLayerList();
                if (shape instanceof ShapeGroup) {
                    groupCounter++;
                } else {
                    shapeCounter++;
                }
                layerManager.getActiveLayer().updateShapeIds(layerManager.getActiveLayer().getShapes().size());
                updateStatusBar();
                repaint();
            }
            tempShapes = null;
        }
    }

    public void deleteShape() {
        if (selectedShape != null) {
            layerManager.getActiveLayer().removeShape(selectedShape);
            layerPanel.updateLayerList();
            selectedShape = null;
            updateStatusBar();
            if (selectedShape instanceof ShapeGroup) {
                groupCounter--;
            } else {
                shapeCounter--;
            }
            layerManager.getActiveLayer().updateShapeIds(layerManager.getActiveLayer().getShapes().size());
            repaint();
        } else if (selectionRectangle != null) {
            List<BaseShape> shapes = selectionRectangle.getSelectedShapes();
            for (BaseShape shape : shapes) {
                layerManager.getActiveLayer().removeShape(shape);
                if (shape instanceof ShapeGroup) {
                    groupCounter--;
                } else {
                    shapeCounter--;
                }
            }
            isShapesSelected = false;
            selectionRectangle = null;
            layerPanel.updateLayerList();
            updateStatusBar();
            layerManager.getActiveLayer().updateShapeIds(layerManager.getActiveLayer().getShapes().size());
            repaint();
        }
    }

    public void openColorPicker() {
        Color newColor = JColorChooser.showDialog(this, "Pick a Color", currentColor);
        if (newColor != null) {
            BaseShape.setDefaultColor(newColor);

            if (selectedShape != null) {
                selectedShape.setColor(newColor);
            }
        }
    }

    private void updateStatusBar() {
        if (selectedShape != null) {
            statusBar.setText("Selected: " + selectedShape.getName() + " at " + selectedShape.getBounds());
        } else {
            statusBar.setText(layerManager.getActiveLayer().getShapes().size() + " objects.");
        }
    }

    public void highlightSelectedObject(int index) {
        selectedShape = layerManager.getActiveLayer().getShapes().get(index);
        repaint();
    }
}
