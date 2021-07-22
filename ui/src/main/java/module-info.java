module io.masterplan.ui {

    requires io.masterplan.infrastructure;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens io.masterplan.ui                                  to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.categorybar           to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.categorybar.category  to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.util.dialogue.category           to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.util.dialogue.task               to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.archivespace.task     to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.archivespace.category to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.tag                              to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.archivespace          to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.account.settings                 to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.account.settings.menu            to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.account.login                    to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.account.login.create             to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.util.icon                        to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces                       to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.editbar               to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.window                           to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.window.toolbar                   to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.window.toolbar.tab               to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.listspace             to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.listspace.category    to javafx.graphics, javafx.controls, javafx.fxml;
    opens io.masterplan.ui.workspaces.listspace.task        to javafx.graphics, javafx.controls, javafx.fxml;
}