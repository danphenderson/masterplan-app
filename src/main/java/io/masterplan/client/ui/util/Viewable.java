package io.masterplan.client.ui.util;

import javafx.scene.Node;

public interface Viewable {

    // returns this node of the controller
    Node node();

    // registers all listeners
    void registerListeners();

    // unregisters all listeners
    void unregisterListeners();

}
