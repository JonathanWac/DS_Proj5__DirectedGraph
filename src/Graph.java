//====================================================================================================================================================================
// Name        : Graph.java
// Author      : Jonathan Wachholz (JHW190002)
// Course	   : UTDallas CS 3345.002 Fall 2020
// Version     : 1.0
// Copyright   : Oct. 2020
// Description :
//         A Class representing a DirectedGraph.
//         Any operation involving a shortest path will only occur if the isDirected value is false
//         The isDirected value will only be set to true if, when attempting to calculate a shortest path, every Edge has
//              a corresponding opposite direction edge with the same distance.
//
//          The expected input format is a .csv file in this format:
//              Ex. line1: startingLocationName,nextLocationName1,distance1,nextLocationName2,distance2, ... etc etc
//
//====================================================================================================================================================================

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Graph {
    private String graphName;
    private LinkedList<GraphNode> nodesListOrig;
    private HashMap<String, Integer> indexMap;
    private boolean isDirected = false;

    // A list of all of the edge connections between nodes
    private ArrayList<Edge> edgeList;
    private ArrayList<Edge> shortestPath;
    private int nodeAmount = 0;

    /**
     * @param fileName the .csv filename containing the graph information
     * @param graphName name of the Graph
     */
    public Graph(String fileName, String graphName) {
        this.graphName = graphName;
        nodesListOrig = new LinkedList<>();
        edgeList = new ArrayList<>();
        indexMap = new HashMap<>();
        readInGraph(fileName);
    }

    /**
     * Will try to generate a shortest path and if it exists will print it
     */
    public void printShortestPath(){
        findShortestPath();
        if (shortestPath == null){
            System.out.println("\tCannot print shortest path");
        }
        else{
            System.out.printf("Shortest path of %s\n", this.graphName);
            for (Edge edge: shortestPath){
                System.out.println(edge);
            }
            printSPDistance();
        }
    }

    /**
     * Will try to generate a shortest path and if it exists will sum the distances
     */
    public void printSPDistance(){
        findShortestPath();
        if (shortestPath == null){
            System.out.println("\tCannot print shortest path");
        }
        else{
            System.out.printf("Shortest path distance of %s is ", this.graphName);
            double totalDistance = 0.0;
            for (Edge edge: shortestPath){
                totalDistance += edge.getDistance();
            }
            System.out.printf("%.2f\n", totalDistance);
        }
    }

    /**
     * @return the List containing the edges from the shortest path
     */
    public ArrayList<Edge> findShortestPath(){
        if (shortestPath == null){
            if (!isDirected){
                ArrayList<Edge> fixedEdges = correctEdges(this.edgeList);
                ArrayList<Edge> shortestPath = new ArrayList<>();
                PriorityQueue<Edge> edgeQueue = fillQueue(fixedEdges);
                DisjSets sets = new DisjSets(nodeAmount);
                int count = 0;
                while(count < nodeAmount){
                    Edge edge1 = edgeQueue.poll();
                    if (edge1 == null)
                        break;
                    int nodeIndex1 = indexMap.get(edge1.getStartNode()), nodeIndex2 = indexMap.get(edge1.getEndNode());
                    if (sets.find(nodeIndex1) != sets.find(nodeIndex2)){
                        sets.union(sets.find(nodeIndex1), sets.find(nodeIndex2));
                        shortestPath.add(edge1);
                        count++;
                    }
                    else{
                        continue;
                    }
                }
                this.shortestPath = shortestPath;
                return shortestPath;
            }
            else{
                System.out.println("This graph is Directed: meaning either 1 vertex does not have a partner return path or it does but the distances are not the same...");
                return null;
            }
        }
        else{
            return shortestPath;
        }

    }

    /**
     * An internal method to 'correct' the original edge list.
     *  Here correction means testing for self loops and checking for edges Directionality
     *      If any edge in the edgeList doesnt have a corresponding edge that leads back to the starting node
     *      then the graphs isDirected value will be set to true
     * @param edgeList the list of edges to be corrected
     * @return the corrected edge list
     */
    private ArrayList<Edge> correctEdges(ArrayList<Edge> edgeList){
        ArrayList<Edge> fixedList = new ArrayList<>();
        for (int i = 0; i < edgeList.size(); i++){
            Edge edge1 = edgeList.get(i);
            if (edge1.isIgnore())
                continue;
            if (edge1.isSelfLoop())
                continue;

            boolean edgeHasPartner = false;
            for (int j = 0; j < edgeList.size(); j++){
                if (i == j)
                    continue;
                Edge edge2 = edgeList.get(j);
                if (edge2.isIgnore())
                    continue;
                if (edge2.isSelfLoop())
                    continue;
                if (edge1.isParallel(edge2)){
                    double dist1 = edge1.getDistance(), dist2 = edge2.getDistance();
                    if (dist1 > dist2){
                        edge1.setIgnore(true);
                    }
                    else{
                        edge2.setIgnore(true);
                    }
                }
                else if (edge1.isBiDirectional(edge2)){
                    double dist1 = edge1.getDistance(), dist2 = edge2.getDistance();
                    if (dist1 != dist2){
                        isDirected = true;
                        edgeHasPartner = true;
                        fixedList.add(edge1);
                        fixedList.add(edge2);
                    }
                    else {
                        edgeHasPartner = true;
                        fixedList.add(edge1);
                    }
                }

            }
            if (!edgeHasPartner){
                isDirected = true;
            }
        }
        return fixedList;
    }

    /**
     *  Takes a standard List object and returns its PriorityQueue equivalent
     * @param edgeList the edge list to be converted to a priority queue
     * @return  a PriorityQueue
     */
    private PriorityQueue<Edge> fillQueue(List<Edge> edgeList){
        PriorityQueue<Edge> edgePriorityQueue = new PriorityQueue<>(edgeList.size());
        edgePriorityQueue.addAll(edgeList);
        return edgePriorityQueue;
    }

    /**
     *  Reads in graph information from a .csv file
     * @param fileName the .csv filename containing the graph information
     */
    private void readInGraph(String fileName){
        try {
            FileReader inFile = new FileReader(fileName);
            Scanner scanner = new Scanner(inFile);
            while (scanner.hasNextLine()){
                String[] tokens = scanner.nextLine().split(",");
                //System.out.println(Arrays.toString(tokens));
                if (tokens.length > 0){
                    String name = tokens[0];
                    GraphNode graphNode = new GraphNode(name);
                    for (int i = 1; i+1 < tokens.length; i += 2) {
                        graphNode.addEdge(tokens[i], Double.parseDouble(tokens[i+1]));
                        edgeList.add(new Edge(name, tokens[i], Double.parseDouble(tokens[i+1]), nodeAmount)); //Here node amount == the index of that starting node
                    }
                    //System.out.println(graphNode);
                    indexMap.put(name, nodeAmount);
                    nodeAmount++;
                    nodesListOrig.add(graphNode);
                }
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
