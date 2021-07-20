package io.masterplan.ui.workspaces.editbar;


import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.ui.util.Viewable;
import io.masterplan.infrastucture.util.graph.ObservableVertex;

public interface IEditBar extends Viewable {

    void setEditVertex(ObservableVertex<TodoElement> editVertex);

}
