package ortools.network;

import com.google.ortools.Loader;
import com.google.ortools.graph.MaxFlow;

/**
 * Minimal MaxFlow program.
 */
public final class SimpleMaxFlowProgram {

    public static void main(String[] args) throws Exception {
        Loader.loadNativeLibraries();
        // Instantiate a SimpleMaxFlow solver.
        MaxFlow maxFlow = new MaxFlow();

        // Define three parallel arrays: start_nodes, end_nodes, and the capacities
        // between each pair. For instance, the arc from node 0 to node 1 has a
        // capacity of 20.
        // From Taha's 'Introduction to Operations Research',
        // example 6.4-2.
        int[] startNodes = new int[]{0, 0, 0, 1, 1, 2, 2, 3, 3};
        int[] endNodes = new int[]{1, 2, 3, 2, 4, 3, 4, 2, 4};
        int[] capacities = new int[]{20, 30, 10, 40, 30, 10, 20, 5, 20};

        // Add each arc.
        for (int i = 0; i < startNodes.length; ++i) {
            int arc = maxFlow.addArcWithCapacity(startNodes[i], endNodes[i], capacities[i]);
            if (arc != i) {
                throw new Exception("Internal error");
            }
        }

        // Find the maximum flow between node 0 and node 4.
        MaxFlow.Status status = maxFlow.solve(0, 4);

        if (status == MaxFlow.Status.OPTIMAL) {
            System.out.println("Max. flow: " + maxFlow.getOptimalFlow());
            System.out.println();
            System.out.println("  Arc     Flow / Capacity");
            for (int i = 0; i < maxFlow.getNumArcs(); ++i) {
                System.out.println(maxFlow.getTail(i) + " -> " + maxFlow.getHead(i) + "    "
                        + maxFlow.getFlow(i) + "  /  " + maxFlow.getCapacity(i));
            }
        } else {
            System.out.println("Solving the max flow problem failed. Solver status: " + status);
        }
    }

    private SimpleMaxFlowProgram() {
    }

}
