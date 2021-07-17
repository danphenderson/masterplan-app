package io.masterplan.client.ui.workspaces.editbar;


import io.masterplan.client.components.TodoElement;
import io.masterplan.client.ui.util.Viewable;
import io.masterplan.client.util.graph.ObservableVertex;

public interface IEditBar extends Viewable {

    void setEditVertex(ObservableVertex<TodoElement> editVertex);

}
