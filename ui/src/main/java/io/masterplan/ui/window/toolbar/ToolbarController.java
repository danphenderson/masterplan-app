package io.masterplan.ui.window.toolbar;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import io.masterplan.ui.window.toolbar.tab.Tab;

/**
 * Toolbar Controller
 */
public class ToolbarController {
    @FXML private VBox tabBar;          // TabBar instance
    @FXML private Tab homeTab;          // HomeTab instance
    @FXML private Tab workspaceTab;     // WorkspaceTab instance
    @FXML private Tab settingsTab;      // SettingsTab instance



    /**
     * Initializes initial active tab
     */
    @FXML
    private void initialize() {
    }


}
