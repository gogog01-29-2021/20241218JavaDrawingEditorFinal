package org.example.ui;

import org.example.Canvas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Toolbar extends JPanel {

    private JButton activeToolButton = null;

    public Toolbar(JComponent canvas, JLabel statusBar) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create buttons with icons
        JButton copyButton = createButton("Copy", "images/copy.png");
        JButton pasteButton = createButton("Paste", "images/paste.png");
        JButton deleteButton = createButton("Delete", "images/delete.png");
        JButton groupButton = createButton("Group", "images/group.png");

        // File operations buttons
        JButton newFileButton = createButton("New", "images/new_file.png");
        JButton saveFileButton = createButton("Save", "images/save_file.png");
        JButton loadFileButton = createButton("Load", "images/load_file.png");

        // Divider between groups
        JSeparator separator1 = createSeparator();

        JButton lineButton = createButton("", "images/draws/line.png");
        JButton circleButton = createButton("", "images/draws/ellipse.png");
        JButton rectangleButton = createButton("", "images/draws/rectangle.png");
        JButton colorButton = createButton("color", null);

        // Divider between groups
        JSeparator separator2 = createSeparator();

        JButton undoButton = createButton("", "images/undo.png");
        JButton redoButton = createButton("", "images/redo.png");

        // Add buttons and dividers to the toolbar
        add(copyButton);
        add(pasteButton);
        add(deleteButton);
        add(groupButton);
        add(newFileButton);  // Add new file button
        add(saveFileButton);  // Add save file button
        add(loadFileButton);  // Add load file button
        add(separator1);  // Add first divider
        add(circleButton);
        add(rectangleButton);
        add(lineButton);
        add(colorButton);
        add(separator2);  // Add second divider
        add(undoButton);
        add(redoButton);

        // Add hover effects to buttons
        addHoverEffect(copyButton);
        addHoverEffect(pasteButton);
        addHoverEffect(deleteButton);
        addHoverEffect(groupButton);
        addHoverEffect(newFileButton);  // Hover effect for new file button
        addHoverEffect(saveFileButton);  // Hover effect for save file button
        addHoverEffect(loadFileButton);  // Hover effect for load file button
        addHoverEffect(colorButton);
        addHoverEffect(circleButton);
        addHoverEffect(rectangleButton);
        addHoverEffect(lineButton);
        addHoverEffect(undoButton);
        addHoverEffect(redoButton);

        // Add actions to buttons (example placeholder)
        addAction(groupButton, () -> {
            ((Canvas) canvas).groupShapes();
        });
        addAction(copyButton, () -> {
            ((Canvas) canvas).copyShape();
        });
        addAction(pasteButton, () -> ((Canvas) canvas).pasteShape());
        addAction(deleteButton, () -> ((Canvas) canvas).deleteShape());
        lineButton.addActionListener(e -> toggleToolButton(lineButton, "line", canvas, statusBar));
        circleButton.addActionListener(e -> toggleToolButton(circleButton, "circle", canvas, statusBar));
        rectangleButton.addActionListener(e -> toggleToolButton(rectangleButton, "rectangle", canvas, statusBar));
        colorButton.addActionListener(e->canvas.getColorModel());
        // Add actions for file operations
        addAction(newFileButton, () -> newFile(canvas, statusBar));
        addAction(saveFileButton, () -> saveFile(canvas, statusBar));
        addAction(loadFileButton, () -> loadFile(canvas, statusBar));
        // Add actions for buttons
        addAction(groupButton, () -> {
            ((Canvas) canvas).groupShapes();
        });
        addAction(copyButton, () -> {
            ((Canvas) canvas).copyShape();
        });
        addAction(pasteButton, () -> ((Canvas) canvas).pasteShape());
        addAction(deleteButton, () -> ((Canvas) canvas).deleteShape());

// Add action for color button
        colorButton.addActionListener(e -> {
            ((Canvas) canvas).openColorPicker();
            statusBar.setText("Color picker opened.");
        });


// Add actions for undo and redo buttons
        addAction(undoButton, () -> {
            // Placeholder for undo action
            statusBar.setText("Undo action triggered.");
        });

        addAction(redoButton, () -> {
            // Placeholder for redo action
            statusBar.setText("Redo action triggered.");
        });

    }

    private void toggleToolButton(JButton button, String tool, JComponent canvas, JLabel statusBar) {
        // If clicking the already active button, deactivate it
        if (button == activeToolButton) {
            button.setBackground(Color.WHITE);  // Reset to default color
            activeToolButton = null;
            statusBar.setText(tool.substring(0, 1).toUpperCase() + tool.substring(1) + " tool deselected");
            ((Canvas) canvas).unselect();
            ((Canvas) canvas).setCurrentTool("select");  // Deselect tool
        } else {
            // Deactivate the currently active tool, if any
            if (activeToolButton != null) {
                activeToolButton.setBackground(Color.WHITE);
            }
            // Activate the selected tool button
            button.setBackground(Color.GRAY);  // Set to gray for active state
            activeToolButton = button;
            statusBar.setText(tool.substring(0, 1).toUpperCase() + tool.substring(1) + " tool selected");
            ((Canvas) canvas).setCurrentTool(tool);  // Set the tool to the selected one
        }
    }

    // Utility method to create a button with enforced padding and a visible border
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton();
        if (iconPath != null) {
            button.setIcon(new ImageIcon(iconPath));
        }
        button.setText(text);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // Text below the icon

        // Set a visible border around the button and add padding using EmptyBorder
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),  // Button border
                new EmptyBorder(5, 5, 5, 5)              // Padding inside the border
        ));

        button.setBackground(Color.WHITE);  // Set a white background for the button

        return button;
    }

    // Utility method to add a visible separator
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(5, 40));  // Increased width for better visibility
        separator.setForeground(Color.BLACK);  // Set separator color
        separator.setOpaque(true);            // Ensure separator is not transparent
        return separator;
    }

    // Utility method to add actions to a button
    private void addAction(JButton button, Runnable action) {
        button.addActionListener(e -> action.run());
    }

    // Utility method to add hover effects to a button
    private void addHoverEffect(JButton button) {
        Color defaultColor = button.getBackground();
        button.setOpaque(true);
        button.setBorderPainted(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Only change background if button is not active
                if (button != activeToolButton) {
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Reset to default color if not active, otherwise keep gray
                button.setBackground(button == activeToolButton ? Color.GRAY : defaultColor);
            }
        });
    }

    // New File Action
    private void newFile(JComponent canvas, JLabel statusBar) {
        // Clear the canvas (or reset it to a default state)
        canvas.removeAll();
        canvas.repaint();
        statusBar.setText("New file created");
    }

    // Save File Action
    private void saveFile(JComponent canvas, JLabel statusBar) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                // Example: write some data from the canvas
                writer.write("Canvas content to save...");
                statusBar.setText("File saved: " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                statusBar.setText("Error saving file");
            }
        }
    }

    // Load File Action
    private void loadFile(JComponent canvas, JLabel statusBar) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Example: load some data into the canvas
                String content = new String(Files.readAllBytes(file.toPath()));
                statusBar.setText("File loaded: " + file.getName());
                // You can modify this to load the content into the canvas
            } catch (IOException e) {
                e.printStackTrace();
                statusBar.setText("Error loading file");
            }
        }
    }
}
