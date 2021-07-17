package io.masterplan.client.models;

import io.masterplan.client.components.Category;
import io.masterplan.client.components.Tag;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.observable.Observable;
import io.masterplan.client.observable.ObservableSet;
import io.masterplan.client.util.graph.*;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainModel {

    public static final MainModel model = new MainModel();

    public final ObservableGraph<TodoElement> obsGraph;
    public final Observable<ObservableVertex<TodoElement>> selectedVertex = new Observable<>();
    public final Observable<ObservableVertex<TodoElement>> editVertex = new Observable<>();

    public final ObservableSet<Tag> tags = new ObservableSet<>(new HashSet<>());

    public final JSONWriteRead json = new JSONWriteRead();

    private MainModel() {
        // deserialize graph
        this.obsGraph = new ObservableGraph<>(new Graph<>());
        selectedVertex.setValue(obsGraph.addVertex(new Category("Main")));

        obsGraph.startListen(this::onEditVertexRemoved);
        obsGraph.startListen(this::onSelectedVertexRemoved);

        importGoogleCalendar(selectedVertex.getValue());
    }

    private void onEditVertexRemoved(ObservableGraphChange<TodoElement> change) {
        if(change.getRemovedVertices().contains(editVertex.getValue()))
            editVertex.setValue(null);
    }

    private void onSelectedVertexRemoved(ObservableGraphChange<TodoElement> change) {
        if(change.getRemovedVertices().contains(selectedVertex.getValue()))
            selectedVertex.setValue(null);
    }

    public void importGoogleCalendar(ObservableVertex<TodoElement> rootVertex) {
        try{
            json.JSONWrite();
            json.JSONRead();
        } catch(Exception e) {
            e.printStackTrace();
        }

        String arrayC[] = json.getArrayC();
        String arrayT[] = json.getArrayT();

        String arrayD[] = json.getArrayD();

        Category notCompleted = new Category("NOT COMPLETED");
        var notCompletedVertex = obsGraph.addVertex(notCompleted, rootVertex);

        Category classes = new Category("Class");
        var classesVertex = obsGraph.addVertex(classes, notCompletedVertex);

        for(int i = 0; i < arrayC.length; i++) {
            if(arrayC[i].equals("Class")) {

                try {
                    Task task = new Task(arrayT[i]);
                    String str= arrayD[i];
                    DateFormat formatter;
                    Date date;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    date = (Date) formatter.parse(str);
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(date);
                    task.setDueDate(cal);
                    obsGraph.addVertex(task, classesVertex);

                } catch (ParseException e)
                {System.out.println("Exception :" + e);  }

            }

        }

        Category meetings = new Category("Meetings");
        var meetingsVertex = obsGraph.addVertex(meetings, notCompletedVertex);

        for(int i = 0; i < arrayC.length; i++) {
            if(arrayC[i].equals("Meeting")) {
                try {
                    Task task = new Task(arrayT[i]);
                    String str= arrayD[i];
                    DateFormat formatter;
                    Date date;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    date = (Date) formatter.parse(str);
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(date);
                    task.setDueDate(cal);
                    obsGraph.addVertex(task, meetingsVertex);

                } catch (ParseException e)
                {System.out.println("Exception :" + e);  }
            }
        }
    }

}
