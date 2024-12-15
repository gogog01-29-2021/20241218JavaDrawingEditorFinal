package org.example.model;

import org.example.model.BaseShape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Layer implements Serializable {
    private List<BaseShape> shapes;

    public Layer() {
        shapes = new ArrayList<>();
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

    public void updateShapeIds(int size) {
        for (int i = 0; i < size; i++) {
            shapes.get(i).setId(i);
        }
    }


    public void setShapes(List<BaseShape> newShapes) {
        this.shapes = newShapes;
    }
}
