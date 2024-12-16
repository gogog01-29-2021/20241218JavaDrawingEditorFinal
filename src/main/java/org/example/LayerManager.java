package org.example;

import org.example.model.Layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayerManager implements Serializable {
    private final List<Layer> layers;
    private int activeLayerIndex;

    public LayerManager() {
        layers = new ArrayList<>();
        layers.add(new Layer());  // Start with one layer
        activeLayerIndex = 0;
    }

    public Layer getActiveLayer() {
        return layers.get(activeLayerIndex);
    }

    public void setActiveLayer(int index) {
        activeLayerIndex = index;
    }

    public int getCurrentLayerIndex() {
        return activeLayerIndex;
    }
}
