package org.example;
import org.example.ui.LayerPanel;
import org.example.ui.Toolbar;

import javax.swing.*;
import java.awt.*;

public class MainApplication {

    public static void main(String[] args) {
        JLabel statusBar = new JLabel("0 objects.");
        LayerManager layerManager = new LayerManager();

        LayerPanel layerPanel = new LayerPanel(layerManager);
        Canvas canvas = new Canvas(layerManager, statusBar, layerPanel);
        layerPanel.setCanvas(canvas);
        // Create and set up the main application frame
        JFrame frame = new JFrame("Shape Drawing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout()); // Set layout manager

        // Create the toolbar and make it scrollable
        Toolbar toolbar = new Toolbar(canvas, statusBar);
        JScrollPane toolbarScrollPane = new JScrollPane(toolbar);
        toolbarScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        frame.add(toolbarScrollPane, BorderLayout.NORTH);  // Toolbar at the top
        frame.add(layerPanel, BorderLayout.EAST);          // Layer panel on the right
        frame.add(canvas, BorderLayout.CENTER);            // Canvas in the center
        frame.add(statusBar, BorderLayout.SOUTH);          // Status bar at the bottom

        // Make the frame visible
        frame.setVisible(true);
    }
}