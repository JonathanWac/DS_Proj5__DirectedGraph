import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private String startNode, endNode;
    private double distance;
    private boolean ignore;



    private int index;

    public Edge(String startNode, String endNode, double distance, int index) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = distance;
        this.ignore = false;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isIgnore() {
        return ignore;
    }
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public String getStartNode() {
        return startNode;
    }
    public String getEndNode() {
        return endNode;
    }
    public double getDistance() {
        return distance;
    }

    public boolean isSelfLoop(){
        if (startNode.equals(endNode))
            return true;
        else
            return false;
    }

    public boolean isParallel(Edge otherEdge){
        if (otherEdge == null)
            throw new NullPointerException();
        return (this.startNode.equals(otherEdge.startNode))&&(this.endNode.equals(otherEdge.endNode));
    }

    public boolean isBiDirectional(Edge otherEdge){
        if (otherEdge == null)
            throw new NullPointerException();
        return (this.startNode.equals(otherEdge.endNode))&&(this.endNode.equals(otherEdge.startNode));
    }

    public boolean equals(Edge otherEdge) {
        if (otherEdge == null)
            throw new NullPointerException();
        return (((this.startNode.equals(otherEdge.startNode))&&(this.endNode.equals(otherEdge.endNode)))
                && (this.distance == otherEdge.distance));
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?> edge = (Edge<?>) o;
        return Double.compare(edge.getDistance(), getDistance()) == 0 &&
                getStartNode().equals(edge.getStartNode()) &&
                getEndNode().equals(edge.getEndNode());
    }*/

    @Override
    public int hashCode() {
        return Objects.hash(getStartNode(), getEndNode(), getDistance());
    }

/*    public int compareTo(Edge<AnyType> otherEdge) {
        if (otherEdge == null)
            throw new NullPointerException();
        return (int) (this.distance - otherEdge.distance);
    }*/

    @Override
    public int compareTo(Edge otherEdge) {
        if (otherEdge == null)
            throw new NullPointerException();
        if (this.distance < otherEdge.distance)
            return -1;
        else if (this.distance > otherEdge.distance)
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return String.format("%20s <--- %.2f ---> %s", startNode, distance, endNode);
    }
}
