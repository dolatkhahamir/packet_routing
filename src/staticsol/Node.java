package staticsol;

import jmath.datatypes.tuples.Point2D;

import java.util.LinkedList;

public class Node implements Comparable<Node> {
    private final String ID;
    private final Point2D point;
    private double distance;
    private Node previous;
    private boolean isVisited;
    private final LinkedList<Edge> neighborEdges;

    public Node(String ID, double x, double y) {
        point = new Point2D(x, y);
        this.ID = ID;
        distance = Double.MAX_VALUE;
        previous = null;
        isVisited = false;
        neighborEdges = new LinkedList<>();
    }

    public Edge getEdge(Node anotherNode) {
        for (var e : neighborEdges)
            if (e.getAnotherNode(this).equals(anotherNode))
                return e;
        return null;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public double getDistance() {
        return distance;
    }

    public boolean setDistance(double distance) {
        if (distance == this.distance)
            return false;
        this.distance = distance;
        return true;
    }

    public void addNeighbor(Edge edge) {
        neighborEdges.add(edge);
    }

    public String getID() {
        return ID;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public Point2D getPoint() {
        return point;
    }

    public LinkedList<Edge> getNeighborEdges() {
        return neighborEdges;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(distance, o.distance);
    }

    @Override
    public String toString() {
        return ID;
    }
}
