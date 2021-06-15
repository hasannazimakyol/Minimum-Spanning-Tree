
import sun.applet.resources.MsgAppletViewer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class FindMST {

    int numberOfVertices; //Number of vertices
    int numberOfEdges; //number of edges
    private int[] vertices; // Vertices array
    private Edge[] MST; // Minimum Spanning Tree Array
    private int mstCost; // Minimum Spanning Tree Cost

    /**
     * Constructor
     *
     * @param edges
     */
    public FindMST(Edge[] edges) {
        //Write codes here

        this.numberOfEdges = edges.length;
        Arrays.sort(edges, new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                if (e1.getVertex2() > e2.getVertex2()) return +1;
                else if (e1.getVertex2() == e2.getVertex2()) return 0;
                else return -1;
            }
        });
        this.numberOfVertices = edges[numberOfEdges - 1].getVertex2() + 1;

        // Hata giderilmesi için tekrar sort işlemi yapıldı.
        Arrays.sort(edges, new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                if (e1.getVertex1() > e2.getVertex1()) return +1;
                else if (e1.getVertex1() == e2.getVertex1()) return 0;
                else return -1;
            }
        });

    }

    /**
     * A utility function to find set of an element i (uses path compression technique)
     *
     * @param subsets
     * @param v
     * @return
     */
    private int find(Subset subsets[], int v) {
        //Write codes here
        // find root and make root as parent of i
        // (path compression)
        if (subsets[v].parent != v)
            subsets[v].parent
                    = find(subsets, subsets[v].parent);

        return subsets[v].parent;
    }


    /**
     * A function that does union of two sets of x and y (uses union by rank)
     *
     * @param subsets
     * @param x
     * @param y
     */
    private void Union(Subset subsets[], int x, int y) {
        //Write codes here

        int xRoot = find(subsets, x);
        int yRoot = find(subsets, y);

        // Attach smaller rank tree under root
        // of high rank tree (Union by Rank)
        if (subsets[xRoot].rank
                < subsets[yRoot].rank)
            subsets[xRoot].parent = yRoot;
        else if (subsets[xRoot].rank
                > subsets[yRoot].rank)
            subsets[yRoot].parent = xRoot;

            // If ranks are same, then make one as
            // root and increment its rank by one
        else {
            subsets[yRoot].parent = xRoot;
            subsets[xRoot].rank++;
        }
    }

    /**
     * Define all vertices from Edge array and return Vertex array
     *
     * @param edges
     * @return
     */
    private int[] CreateVertices(Edge[] edges) {
        //Write codes here
        Arrays.sort(edges, new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                if (e1.getVertex2() > e2.getVertex2()) return +1;
                else if (e1.getVertex2() == e2.getVertex2()) return 0;
                else return -1;
            }
        });
        int[] vertices = new int[edges[numberOfEdges - 1].getVertex2() + 1];
        for (int i = 0; i < edges[numberOfEdges - 1].getVertex2() + 1; i++)
            vertices[i] = i;

        return vertices;
    }

    /**
     * Main calculate Minimum Spanning Tree method
     *
     * @param edges
     */
    public void calculateMST(Edge[] edges) {
        //Write codes here

        //Edge result[] = new Edge[vertices];
        MST = new Edge[numberOfVertices];
        // An index variable, used for result[]
        int e = 0;

        // An index variable, used for sorted edges
        int i = 0;
        for (i = 0; i < numberOfVertices - 1; ++i)
            MST[i] = new Edge(0, 0, 0);

        Arrays.sort(edges);

        Subset subsets[] = new Subset[numberOfVertices];

        for (i = 0; i < numberOfVertices; ++i)
            subsets[i] = new Subset();

        // Create V subsets with single elements
        for (int v = 0; v < numberOfVertices; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0; // Index used to pick next edge

        // Number of edges to be taken is equal to vertices-1
        while (e < numberOfVertices - 1) {
            // Step 2: Pick the smallest edge. And increment
            // the index for next iteration
            Edge next_edge = edges[i++];

            int x = find(subsets, next_edge.getVertex1());
            int y = find(subsets, next_edge.getVertex2());

            // If including this edge doesn't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            if (x != y) {
                MST[e++] = next_edge;
                Union(subsets, x, y);
            }
            // Else discard the next_edge
        }
        // print the contents of result[] to display
        // the built MST

        //System.out.println("Following are the edges in "
        //        + "the constructed MST");

        mstCost = 0;
        for (i = 0; i < e; ++i) {
            mstCost += MST[i].getWeight();
        }

    }


    /**
     * Get calculated Minimum Spanning Tree ArrayList
     *
     * @return
     */
    public Edge[] getMST() {
        return MST;
    }

    /**
     * Check vertex is included given arr[] or not
     *
     * @param arr
     * @param vertex
     * @return
     */
    private boolean isVertexIncluded(int[] arr, int vertex) {
        boolean isVertexIncluded = false;
        for (int i = 0; i < arr.length; i++){
            if (arr[i] == vertex)
                isVertexIncluded = true;
            else
                isVertexIncluded = false;
        }
        //Write codes here
        return isVertexIncluded; //This line will deleted
    }

    /**
     * Get cost of Minimum Spanning Tree
     *
     * @return
     */
    public int getMstCost() {
        return mstCost;
    }

    /**
     * Print Minimum Spanning Tree info
     */
    public void printMST() {
        //Write codes here

        for (int i = 0; i < numberOfVertices -1; ++i) {
            System.out.println(MST[i].getVertex1() + " -- "
                    + MST[i].getVertex2()
                    + " == " + MST[i].getWeight());
        }
        System.out.println("Minimum Cost Spanning Tree " + mstCost);
    }
}
