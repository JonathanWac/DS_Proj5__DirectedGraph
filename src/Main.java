import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        Graph graph = new Graph("assn9_data.csv", "Graph1");
        ArrayList<Edge> graphMST = graph.findMinSpanningTree();
        graph.printMinSpanningTree();
    }
}
