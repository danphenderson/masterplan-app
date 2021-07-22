module io.masterplan.infrastructure {
    exports io.masterplan.infrastucture.components;
    exports io.masterplan.infrastucture.components.task;
    exports io.masterplan.infrastucture.models;
    exports io.masterplan.infrastucture.observable;
    exports io.masterplan.infrastucture.util.graph;
    exports io.masterplan.infrastucture.util.collections;
    exports io.masterplan.infrastucture.util.vector;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;

    requires json.simple;
}