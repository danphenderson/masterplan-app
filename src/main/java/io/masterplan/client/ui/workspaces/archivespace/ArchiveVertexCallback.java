package io.masterplan.client.ui.workspaces.archivespace;


import io.masterplan.client.components.TodoElement;
import io.masterplan.client.observable.IListener;
import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.util.graph.ObservableVertex;

public class ArchiveVertexCallback {

    public class ChangeInfo {

        public final boolean isArchived;

        public ChangeInfo(boolean isArchived){
            this.isArchived = isArchived;
        }

        public ObservableVertex<TodoElement> getVertex() {
            return ArchiveVertexCallback.this.vertex;
        }
        
    }

    private final ObservableManager observableManager = new ObservableManager();

    private ObservableVertex<TodoElement> vertex;

    private final IListener<ChangeInfo> callBack;
    
    public ArchiveVertexCallback(ObservableVertex<TodoElement> vertex, IListener<ChangeInfo> callBack) {
        this.vertex = vertex;
        this.callBack = callBack;
        observableManager.addListener(vertex.getElement().isArchived, this::onArchiveChange);
    }

    public IListener<ChangeInfo> getCallBack() {
        return callBack;
    }
    
    private void onArchiveChange(boolean isArchived) {
        if(callBack != null) {
            callBack.onChange(new ChangeInfo(isArchived));
        }
    }
    
    public void registerListeners() {
        observableManager.startListen();
    }
    
    public void unregisterListeners() {
        observableManager.stopListen();
    }
    
    
}
