package util.graph;


import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.util.graph.Graph;
import io.masterplan.client.util.graph.IQuery;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

//TODO confirm that the sort test is working once sort is implemented
/**
 * This class performs an extensive testing of our util.Graph structure
 *
 * @author Daniel Henderson
 */

public class GraphUnitTest {

    private Graph<TodoElement> graph;
    private Task t1, t2, t3, t4;
    private Category c1, c2;
    private Category root;
    private Graph<TodoElement>.Vertex rootV;

    @Before
    public void setUp() {
        graph = new Graph<>();
        t1 = new Task("t1");
        t2 = new Task("t2");
        t3 = new Task("t3");
        t4 = new Task("t4");
        c1 = new Category("c1");
        c2 = new Category("c2");
        root = new Category("Root");
        rootV = graph.addVertex(root);
    }

    /** Adds a task to the graph, and confirm that the returned vertex's element is the task  **/
    @Test
    public void addVertex1() {
        var cVertex = graph.addVertex(c1);
        var tVertex = graph.addVertex(t1);

        assertEquals(t1, tVertex.getElement());
        assertEquals(c1, cVertex.getElement());
    }

    /** Adds a null vertex testing that the IllegalArgumentException is thrown **/
    @Test
    public void addVertex2() {
        try{
            graph.addVertex(null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Graph.addVertex() - element can not be null."));
            return;
        }
        fail("Exception never thrown");
    }

    /** Adds v again to test that the IllegalArgumentException is thrown **/
    @Test
    public void addVertex3() {
        var v = graph.addVertex(t1);

        try{
            graph.addVertex(v.getElement());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Graph.addVertex() - element already exists in graph; duplicate elements are not allowed."));
            return;
        }
        fail("Exception never thrown");
    }

    /** Adds a vertex and tests successful removal of the vertex **/
    @Test
    public void removeVertex1() {
//        /**
//         * G:
//         * root -> {v1, v4}
//         * v1 -> {v2, v3}
//         * v2 -> {}
//         *     :
//         * v3 -> {}
//         */
//        // Initialize
//        var v1 = graph.addVertex(t1);
//        var v2 = graph.addVertex(t2);
//        var v3 = graph.addVertex(t3);
//        var v4 = graph.addVertex(t4);
//
//        graph.addDirectedEdge(rootV, v1);
//        graph.addDirectedEdge(rootV, v4);
//        graph.addDirectedEdge(v1, v2);
//        graph.addDirectedEdge(v1, v3);
//
//        graph.removeVertex(v1);
//
//        assertTrue(!graph.getVertices().contains(v1));
////        assertTrue(!graph.getInVertices(v2).contains(v1));
//        assertTrue(!graph.getInVertices(v3).contains(v1));
    }

    /** Adds a vertex and tests successful removal of the vertex **/
    @Test
    public void removeVertexReachable1() {
        /**
         * G:
         * root -> {v1,v2, v4}
         * v1 -> {v2, v3}
         * v2 -> {}
         *     :
         * v3 -> {}
         */

        // Initialize
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        var v3 = graph.addVertex(t3);
        var v4 = graph.addVertex(t4);

        graph.addDirectedEdge(rootV, v1);
        graph.addDirectedEdge(rootV, v2);
        graph.addDirectedEdge(rootV, v4);

        graph.addDirectedEdge(v1, v2);
        graph.addDirectedEdge(v1, v3);

        graph.removeVertex(v1);

        assertTrue(!graph.getVertices().contains(v1));

    }

    /** Adds a Dependency between vertices from task 1 to task 2 **/
    @Test
    public void addDirectedEdge1() {
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        graph.addDirectedEdge(v1, v2);

        for (var v : graph.getOutVertices(v1))
            assertEquals(v.getElement(),v2.getElement());
    }

    /** Checks for thrown exception from adding a 3-cycle to the graph **/
    @Test
    public void addDirectedEdge2() {
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        var v3 = graph.addVertex(c1);
        graph.addDirectedEdge(v3, v2);
        graph.addDirectedEdge(v2, v1);


        try {
            graph.addDirectedEdge(v1, v2);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("v1 is reachable from v2, circularity detected"));
            return;
        }
        fail("Exception never thrown");
    }

    /** Adds a Dependency between vertices from task 1 to task 2 and checks for successful removal **/
    @Test
    public void removeDirectedEdge1() {
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        graph.addDirectedEdge(v1, v2);
        graph.removeDirectedEdge(v1, v2);

        assertEquals(0, graph.getOutVertices(v1).size());
    }

    /** Ensures that there is a one-to-one mapping between added vertices and vertices in the graph **/
    @Test
    public void getVertices1() {
        HashSet<Graph.Vertex> vertexSet = new HashSet<>();

        vertexSet.add(graph.addVertex(t1));
        vertexSet.add(graph.addVertex(t2));
        int size = vertexSet.size() + 1; // adding 1 b/c of root

        assertEquals(size, graph.getVertices().size());
    }

    /** Tests the sort of tasks by creation data **/
    @Test
    public void sort1() { // TODO: Ensure working when implemented
//        // comparator definition
//        Comparator<TodoElement> c = (t1, t2) -> {
//            return t1 == null ? -1 : t1.creationDate.compareTo(t2.creationDate);
//        };
//
//        // Initialize
//        var r = graph.addVertex(t1);
//        graph.addVertex(t2);
//        graph.addVertex(t3);
//        graph.addVertex(t4);
//        graph.sort(c);
//
//        TodoElement prev = r.getElement();
//
//        // Testing the sort within our root vertex adjacency list
//        for (var v : graph.getVertices()) {
//            assertTrue(c.compare(v.getElement(), prev) > 0);
//            prev = v.getElement();
//        }
//        fail("Not yet implmented in ObservableGraph");
    }

    /** Tests the sort of tasks by creation data **/
    @Test
    public void sortReachable1() {
        // comparator definition
        Comparator<TodoElement> c = (t1, t2) -> {
            return t2 == null ? 1 : t1.name.getValue().compareTo(t2.name.getValue());
        };

        // Initialize
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        var v3 = graph.addVertex(t3);
        var v4 = graph.addVertex(t4);

        graph.addDirectedEdge(v1, v2);
        graph.addDirectedEdge(v1, v3);
        graph.addDirectedEdge(v1, v4);
        graph.sortReachable(c, v1);

        TodoElement prev = null;

        // Testing the sort within our root vertex adjacency list
        for (var v : graph.getOutVertices(v1)) {
            assertTrue(c.compare(v.getElement(), prev) >= 0);
            prev = v.getElement();
        }
    }


    /** Testing a query for a specific vertex of Task t1 **/
    @Test
    public void query1() {
        IQuery<TodoElement> query = (element) -> { return element == t1; };

        // Initialize
        var v = graph.addVertex(t1);
        List<Graph<TodoElement>.Vertex> queryRes = graph.query(query);

        assertTrue(queryRes.contains(v));
        assertEquals(1, queryRes.size());
    }

    /** Testing a reachable query for a specific vertex of Task t **/
    @Test
    public void queryReachable1() {
        IQuery<TodoElement> query = (element) -> { return element == t2; };

        // Initialize
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        graph.addDirectedEdge(v1, v2);
        List<Graph<TodoElement>.Vertex> queryRes = graph.query(query);

        assertTrue(queryRes.contains(v2));
        assertEquals(1, queryRes.size());
    }

    /** Testing a reachable query for a specific vertex of Task t **/
    @Test
    public void getInVertices() {
        // Initialize
        var v1 = graph.addVertex(t1);
        var v2 = graph.addVertex(t2);
        var v3 = graph.addVertex(t3);
        var v4 = graph.addVertex(t4);

        graph.addDirectedEdge(rootV, v1);
        graph.addDirectedEdge(rootV, v2);
        graph.addDirectedEdge(rootV, v3);
        graph.addDirectedEdge(rootV, v4);

        graph.addDirectedEdge(v1, v2);
        graph.addDirectedEdge(v1, v3);

        var expected = new ArrayList<Graph<TodoElement>.Vertex>();
        expected.add(rootV);
        expected.add(v1);

        // test G1: expected -> G1
        assertTrue(graph.getInVertices(v2).containsAll(expected));

        // test G1: expected <- G1
        for (var v : graph.getInVertices(v2)) {
            System.out.println(v.getElement().getName());
            assertTrue(expected.contains(v));
            expected.remove(v);
        }

    }


    // TODO Validate vertex?



}
