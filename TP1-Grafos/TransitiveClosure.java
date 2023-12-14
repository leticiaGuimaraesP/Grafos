/**
 *  The {@code TransitiveClosure} class represents a data type for
 *  computing the transitive closure of a digraph.
 *  <p>
 *  This implementation runs depth-first search from each vertex.
 *  The constructor takes &Theta;(<em>V</em>(<em>V</em> + <em>E</em>))
 *  in the worst case, where <em>V</em> is the number of vertices and
 *  <em>E</em> is the number of edges.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em><sup>2</sup>) extra space (not including the digraph).
 *  <p>
 *  For large digraphs, you may want to consider a more sophisticated algorithm.
 *  <a href = "http://www.cs.hut.fi/~enu/thesis.html">Nuutila</a> proposes two
 *  algorithm for the problem (based on strong components and an interval representation)
 *  that runs in &Theta;(<em>E</em> + <em>V</em>) time on typical digraphs.
 *
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class TransitiveClosure {
    private DirectedDFS[] tc;  // tc[v] = reachable from v

    /**
     * Computes the transitive closure of the digraph {@code G}.
     * @param G the digraph
     */
    public TransitiveClosure(Digraph G) {
        tc = new DirectedDFS[G.V()];
        for (int v = 0; v < G.V(); v++)
            tc[v] = new DirectedDFS(G, v);
    }

    /**
     * Is there a directed path from vertex {@code v} to vertex {@code w} in the digraph?
     * @param  v the source vertex
     * @param  w the target vertex
     * @return {@code true} if there is a directed path from {@code v} to {@code w},
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
    public boolean reachable(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return tc[v].marked(w);
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = tc.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code TransitiveClosure} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String arq = "teste.txt";
        Digraph G = new Digraph(arq);

        TransitiveClosure tc = new TransitiveClosure(G);

        // print header
        System.out.print("     ");
        for (int v = 0; v < G.V(); v++)
            System.out.printf("%3d", v);
        System.out.println();
        System.out.println("--------------------------------------------");

        // print transitive closure
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%3d: ", v);
            for (int w = 0; w < G.V(); w++) {
                if (tc.reachable(v, w)) System.out.printf("  T"); 
                else                    System.out.printf("   ");
            }
            System.out.println();
        }
    }
}
