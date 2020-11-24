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