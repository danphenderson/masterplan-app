package util.graph;

import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.util.graph.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class ObservableVertexUnitTest {
    /**
     * ObservableGraph G:
     *     root --> {v1, v2, v3, v4}
     *       v1 --> {v2, v3}
     *       v2 --> {}
     *       v3 --> {}
     *       v4 --> {}
     *
     *
     * In Testing for Observabl(vertexChange</TodoElement> and ObservableVertexChange</TodoElement>,
     * by listening to our graphs G1 and G2. Also, ensure that the only change is that being tested.
     *
     * Listeners are initially null.
     */

    private ObservableGraph<TodoElement> G;
    private ObservableVertex<TodoElement> v1, v2, v3, v4;
    private ObservableVertex<TodoElement> rootV;
    private ObservableVertexChange<TodoElement> vertexChange;
    private int numOnChangeCalls;


    @Before
    public void setup() {
        G = new ObservableGraph<>(new Graph<>());
        rootV = G.addVertex(new Category("root"));
        v1 = G.addVertex(new Task("t1"), rootV);
        v2 = G.addVertex(new Task("t2"), rootV);
        v3 = G.addVertex(new Task("t3"), rootV);
        v4 = G.addVertex(new Task("t4"), rootV);

        G.addDirectedEdge(v1, v2);
        G.addDirectedEdge(v1, v3);


        // register listeners and set changes to null
        vertexChange = null;
        rootV.startListen(this::onChange);
        v1.startListen(this::onChange);
        v2.startListen(this::onChange);
        v3.startListen(this::onChange);
        v4.startListen(this::onChange);

        numOnChangeCalls = 0;
    }

    @After
    public void exit() {
        rootV.stopListen(this::onChange);
        v1.stopListen(this::onChange);
        v2.stopListen(this::onChange);
        v3.stopListen(this::onChange);
        v4.stopListen(this::onChange);
    }


    @Test
    public void getOutDegreeTest() {
        // test G1 root
        assertEquals(rootV.getGraph().getOutDegree(rootV), 4);

        // Test no change to ObservableVertexChange
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test // TODO: Fix issue
    public void getInVerticesTest() {
        // hash set of G1's root vertices adjacency list
        var expected = new HashSet<ObservableVertex<TodoElement>>();
        expected.add(rootV);
        expected.add(v1);

        // test G1: expected -> G1
        assertTrue(rootV.getGraph().getInVertices(v2).containsAll(expected));

        // test G1: expected <- G1
        for (var v : rootV.getGraph().getInVertices(v2)) {
            assertTrue(expected.contains(v));
            expected.remove(v);
        }

        // Test no change to ObservableVertexChange
        assertEquals(0, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void getInDegreeTest() {
        // test G1 root
        assertEquals(2, rootV.getGraph().getInDegree(v2));


        // Test no change to ObservableVertexChange
        assertEquals(0, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void queryTest() {
        IQuery<TodoElement> query = (element) -> { return element == v1.getElement(); };
        List<ObservableVertex<TodoElement>> queryRes = rootV.getGraph().query(query);

        assertTrue(queryRes.contains(v1));
        assertEquals(1, queryRes.size());

        // Test no change to ObservableVertexChange
        assertEquals(0, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void queryReachableTest() {
        IQuery<TodoElement> query = (element) -> { return element == v3.getElement(); };
        List<ObservableVertex<TodoElement>> queryRes = rootV.getGraph().queryReachable(query, rootV);

        assertTrue(queryRes.contains(v3));
        assertEquals(1, queryRes.size());

        // Test no change to ObservableVertexChange
        assertEquals(0, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void size() {
        assertEquals(5, rootV.getGraph().size());
    }

    @Test
    public void addVertexTest() { ;
        var v = rootV.getGraph().addVertex(new Task(), rootV);

        // Test no change to ObservableVertexChange
        assertEquals(1, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(1, vertexChange.addedEdgesSize());
        assertTrue(vertexChange.getAddedEdges().contains(v));
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

//    @Test
//    public void removeVertexTest() {
//        rootV.getGraph().removeVertex(v1.vertex);
//
//        // Test no change to ObservableVertexChange
//        assertEquals(1, numOnChangeCalls);
//        assertNotNull(vertexChange);
//        assertEquals(0, vertexChange.addedEdgesSize());
//        assertTrue(vertexChange.getRemovedEdges().contains(v1));
//        assertEquals(1, vertexChange.removedEdgesSize());
//        assertFalse(vertexChange.getSorted());
//        assertNull(vertexChange.getSortingComparator());
//    }

    @Test
    public void addDirectedEdgeTest() {
        v1.getGraph().addDirectedEdge(v1, v4); // now v1 -> {v2,v3,v4}

        // Test no change to ObservableVertexChange
        assertEquals(1, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(1, vertexChange.addedEdgesSize());
        assertTrue(vertexChange.getAddedEdges().contains(v4));
        assertEquals(0, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void removeDirectedEdgeTest() {
        rootV.getGraph().removeDirectedEdge(rootV, v1); // now rootV -> {v2,v3,v4}

        // Test no change to ObservableVertexChange
        assertEquals(1, numOnChangeCalls);
        assertNotNull(vertexChange);
        assertEquals(0, vertexChange.addedEdgesSize());
        assertTrue(vertexChange.getRemovedEdges().contains(v1));
        assertEquals(1, vertexChange.removedEdgesSize());
        assertFalse(vertexChange.getSorted());
        assertNull(vertexChange.getSortingComparator());
    }

    @Test
    public void sortTest() {
//        fail("Test not yet implemented");
    }


    @Test
    public void sortReachableTest() {
        Comparator<TodoElement> c = (t1, t2) -> {
            return t2 == null ? 1 : t1.name.getValue().compareTo(t2.name.getValue());
        };

        // Testing the sort within our root vertex adjacency list
        rootV.getGraph().sortReachable(c, rootV);
        TodoElement prev = null;
        for (var v : G.getOutVertices(rootV)) {
            assertTrue(c.compare(v.getElement(), prev) >= 0);
            prev = v.getElement();
        }

        // TODO: test based upon the sort that is being performed. Currently sorting
        // all vertices reachable from rootV
    }


    protected void onChange(ObservableVertexChange<TodoElement> change) {
        numOnChangeCalls++;
        this.vertexChange = change;
    }

}