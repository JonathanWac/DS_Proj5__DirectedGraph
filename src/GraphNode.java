//====================================================================================================================================================================
// Name        : GraphNode.java
// Author      : Jonathan Wachholz (JHW190002)
// Course	   : UTDallas CS 3345.002 Fall 2020
// Version     : 1.0
// Copyright   : Nov. 2020
// Description :
//         A Class representing a single node/vertice in a graph
//      This class is meant to save space in the program by storing the graph in the form of Nodes rather than Edges.
//          Each instance of a node contains a List holding arrays with the Next Node/Vertex/Location
//          that this node has a path too, along with the distance to that next location
//
//====================================================================================================================================================================


import java.util.LinkedList;

public class GraphNode implements Comparable<GraphNode> {
    private String locationName;
    //The array that contains the connected city name + distance
    //      LL is chosen because each index will have to be searched when trying to find the destination city
    //      edgesList[0] == cityName, edgesList[1] == distance
    private LinkedList<String[]> edgesList;

    public GraphNode(String locationName) {
        this.locationName = locationName;
        edgesList = new LinkedList<>();
    }

    public void addEdge(String nextCity, double distance){
        String[] arr = new String[2];
        arr[0] = nextCity;
        arr[1] = String.valueOf(distance);
        edgesList.add(arr);
    }

    public String[] findDestination(String destinationName){
        for (String[] arr: edgesList){
            if (arr[0].equals(destinationName))
                return arr;
        }
        return null;
    }

    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(String.format("GraphNode(%s){\n", locationName));
        for (String[] arr: edgesList){
            sb.append(String.format("%s <----%s----> %s\n", locationName, arr[1], arr[0]));
        }
        sb.append('}');
        return sb.toString();
    }



    @Override
    public int compareTo(GraphNode o) {
        return 0;
    }
}