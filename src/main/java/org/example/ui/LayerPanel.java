package org.example.ui;

import org.example.Canvas;
import org.example.LayerManager;
import org.example.model.BaseShape;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LayerPanel extends JPanel {
    private final LayerManager layerManager;
    private final JList<String> layerList;
    private org.example.Canvas canvas;

    public LayerPanel(LayerManager layerManager) {
        this.layerManager = layerManager;
        setLayout(new BorderLayout());

        // Darker gray background for the layer panel
//        setBackground(Color.DARK_GRAY);

        layerList = new JList<>(new DefaultListModel<>());
        layerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(layerList);
        add(scrollPane, BorderLayout.CENTER);

        layerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = layerList.getSelectedIndex();
                if (index < 0) return;
                canvas.highlightSelectedObject(index);
            }
        });
    }

    public void setSelected(int index) {
        layerList.setSelectedIndex(index);
    }

    public void setSelectedIndexes(int [] indexes) {
        layerList.setSelectedIndices(indexes);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void updateLayerList() {
        DefaultListModel<String> model = (DefaultListModel<String>) layerList.getModel();
        model.clear();
        List<BaseShape> shapes = layerManager.getActiveLayer().getShapes(); // Get the list of shapes
        for (int i = 0; i < shapes.size(); i++) {
            BaseShape shape = shapes.get(i); // Get the shape at index i
            model.addElement(shape.getName() + ", element: " + (i + 1)); // Add index and name
        }
    }
}
