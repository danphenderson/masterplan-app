package util.graph;


import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.util.graph.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class ObservableGraphUnitTest {
    /**
     * ObservableGraph G:
     *     root --> {v1, v2, v3, v4}
     *       v1 --> {v2, v3}
     *       v2 --> {}
     *       v3 --> {}
     *       v4 --> {}
     *
     * In Testing for ObservableGraphChange</TodoElement> we listen to changes in G
     * and ensure that the only change is that being tested.
     *
     * Listeners are initially null.
     */
    private ObservableGraph<TodoElement> G;
    private ObservableVertex<TodoElement> v1, v2, v3, v4;
    private ObservableVertex<TodoElement> rootV;
    private ObservableGraphChange<TodoElement> graphChange;
    private int numOnChangeCalls;



    @Before
    public void setup() {
        G = new ObservableGraph<>(new Graph<>());
        rootV = G.addVertex(new Category("root"));

        v1 = G.addVertex(new Task("v1"), rootV);
        v2 = G.addVertex(new Task("v2"), rootV);
        v3 = G.addVertex(new Task("v3"), rootV);
        v4 = G.addVertex(new Task("v4"), rootV);

        G.addDirectedEdge(v1, v2);
        G.addDirectedEdge(v1, v3);

        // register listeners and set changes to null
        numOnChangeCalls = 0;
        graphChange = null;
    }

    @Test
    public void getVerticesTest() {
        // register listener
        G.startListen(this::onChange);

        // hash set of G1 the vertices
        var expected = new HashSet<ObservableVertex<TodoElement>>();
        expected.add(rootV);
        expected.add(v1);
        expected.add(v2);
        expected.add(v3);
        expected.add(v4);

        // test G1: verticesG1 -> G1
        assertTrue(G.getVertices().containsAll(expected));

        // test G1: G1.getVertives -> vertivesG1
        for (var v : G.getVertices()) {
            assertTrue(expected.contains(v));
            expected.remove(v); // remove to ensure we aren't getting repeats
        }

        // check the listener is only called on initialization
        assertEquals(1, numOnChangeCalls);

        // stop listners
        G.stopListen(this::onChange); }

    @Test
    public void getOutVerticesTest() {
        // register listener
        G.startListen(this::onChange);

        // hash set of G1's root vertices adjacency list
        var expected = new ArrayList<ObservableVertex<TodoElement>>();
        expected.add(v1);
        expected.add(v2);
        expected.add(v3);
        expected.add(v4);

        // test G1: expected -> G1
        assertTrue(G.getOutVertices(rootV).containsAll(expected));

        // test G1: expected <- G1
        for (var v : G.getOutVertices(rootV)) {
            assertTrue(expected.contains(v));
            expected.remove(v);
        }

        // check the listener is only called on initialization
        assertEquals(1, numOnChangeCalls);

        // stop listners
        G.stopListen(this::onChange);
    }

    @Test
    public void getOutDegreeTest() {
        // register listener
        G.startListen(this::onChange);

        // test G1 root
        assertEquals(G.getOutDegree(rootV), 4);

        // check the listener is only called on initialization
        assertEquals(1, numOnChangeCalls);

        // stop listeners
        G.stopListen(this::onChange);
    }

    @Test // TODO: Fix issue
    public void getInVerticesTest() {
        // register listener
        G.startListen(this::onChange);

        // hash set of G1's root vertices adjacency list
        var expected = new ArrayList<ObservableVertex<TodoElement>>();
        expected.add(rootV);
        expected.add(v1);

        // test G1: expected -> G1
        assertTrue(G.getInVertices(v2).containsAll(expected));

        // test G1: expected <- G1
        for (var v : G.getInVertices(v2)) {
            assertTrue(expected.contains(v));
            expected.remove(v);
        }

        // check the listener is only called on initialization
        assertEquals(1, numOnChangeCalls);

        // stop listners
        G.stopListen(this::onChange);
    }

    @Test
    public void getInDegreeTest() {
        // register listener
        G.startListen(this::onChange);

        // test G1 root
        assertEquals(2, G.getInDegree(v2));

        // check the listener is only called on initialization
        assertEquals(1, numOnChangeCalls);

        // stop listeners
        G.stopListen(this::onChange);
    }

    @Test
    public void queryTest() {
        // Register listener
        G.startListen(this::onChange);

        IQuery<TodoElement> query = (element) -> { return element == v1.getElement(); };

        List<ObservableVertex<TodoElement>> queryRes = G.query(query);

        System.out.println(queryRes.get(0).getElement().name.getValue());

        System.out.println(queryRes.get(0) == v1);

        assertTrue(queryRes.contains(v1));

        assertEquals(1, queryRes.size());

        // Test no change to ObservableGraphChange
        assertEquals(1, numOnChangeCalls);

        assertNotNull(graphChange);

        assertEquals(5, graphChange.addedVerticesSize());

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    @Test
    public void queryReachableTest() {
        // Register listener and define a query for v3
        G.startListen(this::onChange);
        IQuery<TodoElement> query = (element) -> { return element == v3.getElement(); };

        List<ObservableVertex<TodoElement>> queryRes = G.queryReachable(query, rootV);

        assertTrue(queryRes.contains(v3));

        // Test no change to ObservableGraphChange
        assertEquals(1, queryRes.size());

        assertEquals(1, numOnChangeCalls);

        assertNotNull(graphChange);

        assertEquals(5, graphChange.addedVerticesSize());

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    @Test
    public void size() {
        // Register listener
        G.startListen(this::onChange);

        assertEquals(5, G.size());

        G.stopListen(this::onChange);
    }

    @Test
    public void VertexTest()  {
        G.startListen(this::onChange);

        var v = G.addVertex(new Task());

        assertEquals(2, numOnChangeCalls);

        assertNotNull(graphChange);

        assertEquals(1, graphChange.addedVerticesSize());

        assertTrue(graphChange.getAddedVertices().contains(v));

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    @Test
    public void removeVertexTest() {
        G.startListen(this::onChange);

        G.removeVertex(v1);

        assertEquals(2, numOnChangeCalls);

        assertNotNull(graphChange);

        assertEquals(0, graphChange.addedVerticesSize());

        assertEquals(1, graphChange.removedVerticesSize());

        assertTrue(graphChange.getRemovedVertices().contains(v1));

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }
    @Test
    public void removeVertexReachableTest() {
//        for (var v : rootV.getGraph().getVertices()) {
//            System.out.println(v.getElement().getName() + "->");
//            for (var u: rootV.getGraph().getOutVertices(v)) {
//                System.out.println("     " + u.getElement().getName());
//
//            }
//        }
//
//        G.removeVertexReachable(v1.vertex);
//
//        System.out.println(" ");
//        for (var v : rootV.getGraph().getVertices()) {
//            System.out.println(v.getElement().getName() + "->");
//            for (var u: rootV.getGraph().getOutVertices(v)) {
//                System.out.println("     " + u.getElement().getName());
//
//            }
//        }
//        fail("not yet implemented");
    }

    @Test
    public void addDirectedEdgeTest() {
        G.startListen(this::onChange);
        G.addDirectedEdge(v1, v4); // now v1 -> {v2,v3,v4}

        assertEquals(1, numOnChangeCalls);

        // Test no change to ObservableGraphChange
        assertNotNull(graphChange);

        assertEquals(5, graphChange.addedVerticesSize());

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    @Test
    public void removeDirectedEdgeTest() {
        G.startListen(this::onChange);
        G.removeDirectedEdge(rootV, v1); // now rootV -> {v2,v3,v4}

        assertEquals(1, numOnChangeCalls);

        // Test no change to ObservableGraphChange
        assertNotNull(graphChange);

        assertEquals(5, graphChange.addedVerticesSize());

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    @Test
    public void sortTest() { // TODO Must implement
//        // comparator definition
//        Comparator<TodoElement> c = (t1, t2) -> {
//            return t2 == null ? 1 : t1.name.getValue().compareTo(t2.name.getValue());
//        };
//        TodoElement prev = null;
//
//        G.sort(c);
//        // Testing the sort within our root vertex adjacency list
//        for (var v : G.getVertices()) {
//            assertTrue(c.compare(v.getElement(), prev) > 0);
//            prev = v.getElement();
//        }
//        fail("Not yet implmented in ObservableGraph");
    }


    @Test
    public void sortReachableTest() {
        // register listner and comparator sorts creation data of v1 childern
        G.startListen(this::onChange);
        Comparator<TodoElement> c = (t1, t2) -> {
            return t2 == null ? 1 : t1.name.getValue().compareTo(t2.name.getValue());
        };

        // Testing the sort within our root vertex adjacency list
        G.sortReachable(c, rootV);
        TodoElement prev = null;
        for (var v : G.getOutVertices(rootV)) {
            assertTrue(c.compare(v.getElement(), prev) >= 0);
            prev = v.getElement();
        }

        // Test no change to ObservableGraphChange
        assertEquals(1, numOnChangeCalls);

        assertNotNull(graphChange);

        assertEquals(5, graphChange.addedVerticesSize());

        assertEquals(0, graphChange.removedVerticesSize());

        assertFalse(graphChange.getSorted());

        assertNull(graphChange.getSortingComparator());

        G.stopListen(this::onChange);
    }

    protected void onChange(ObservableGraphChange<TodoElement> change) {
        numOnChangeCalls++;
        this.graphChange = change;
    }
}
