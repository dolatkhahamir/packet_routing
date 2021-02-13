package staticsol;

import java.util.*;

public class Graph {
    private final ArrayList<Node> nodes;
    private final HashMap<String, Node> map;
    private final ArrayList<Rout> routs;
    private final ArrayList<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.map = new HashMap<>();
        routs = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addNode(String id, double x, double y) {
        var node = new Node(id, x, y);
        map.put(id, node);
        nodes.add(node);
    }

    public void addEdge(String id1, String id2) {
        var node1 = map.get(id1);
        var node2 = map.get(id2);
        var edge = new Edge(node1, node2);
        node1.addNeighbor(edge);
        node2.addNeighbor(edge);
        edges.add(edge);
    }

    public void start(Scanner scn) {
        while (true) {
            var req = scn.nextLine();
            if (req.equals("exit"))
                break;
            var info = req.split(" ");
            var routeNodes = dijkstraAlgorithm(this, info[1], info[2], Double.parseDouble(info[0]));
            for (var node : routeNodes)
                System.out.print(node.getID() + " -> ");
            System.out.println("END");
        }
    }

    public static List<Node> dijkstraAlgorithm(Graph g, String startId, String stopId, double time) {
        var start = g.map.get(startId);
        var stop = g.map.get(stopId);
        for (int i = 0; i < g.routs.size(); i++)
            if (g.routs.get(i).check(time))
                g.routs.remove(i--);

        var minHeap = new MinHeap(g.nodes.size());
        for (var n : g.nodes) {
            n.setPrevious(null);
            if (start.equals(n)) {
                n.setDistance(0);
                n.setVisited(true);
            } else {
                n.setVisited(false);
                n.setDistance(Integer.MAX_VALUE);
                minHeap.push(n);
            }
        }

        var selected = start;
        while (!stop.isVisited()) {
            for (var edge : selected.getNeighborEdges()) {
                var node = edge.getAnotherNode(selected);
                if (!node.isVisited())
                    if (minHeap.updateDistance(node.getID(),
                            Math.min(node.getDistance(), selected.getDistance() + edge.weight())))
                        node.setPrevious(selected);
            }
            selected = minHeap.poll();
            selected.setVisited(true);
        }

        var edges = new ArrayList<Edge>();
        double expTime = 0;
        var routeNodes = new ArrayList<Node>();
        while (selected.getPrevious() != null) {
            routeNodes.add(selected);
            var edge = selected.getEdge(selected.getPrevious());
            edges.add(edge);
            selected = selected.getPrevious();
            expTime += edge.weight();
        }
        g.routs.add(new Rout(edges, expTime * 120 + time));
        routeNodes.add(start);
        Collections.reverse(routeNodes);
        return routeNodes;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
