package dynamic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Graph {
    private final ArrayList<Node> nodes;
    private final HashMap<String, Node> map;
    private final ArrayList<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.map = new HashMap<>();
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
        while (scn.hasNext()) {
            var req = scn.nextLine();
            if (req.equals("exit"))
                break;
            var info = req.split(" ");
            System.out.println(dijkstraAlgorithm(map.get(info[1]), map.get(info[2]), Edge.time = Double.parseDouble(info[0])));
        }
    }

    public String dijkstraAlgorithm(String startId, String stopId, double time) {
        return dijkstraAlgorithm(map.get(startId), map.get(stopId), time);
    }

    public String dijkstraAlgorithm(Node start, Node stop, double time) {
        edges.forEach(e -> e.setColor(Color.RED.darker().darker()));

        var minHeap = new MinHeap(nodes.size());
        for (var n : nodes) {
            n.setPrevious(null);
            if (start.equals(n)) {
                n.setDistance(0);
                n.setVisited(true);
            } else {
                n.setVisited(false);
                n.setDistance(Double.MAX_VALUE);
                minHeap.push(n);
            }
        }

        var selected = start;
        var dTimeMap = new HashMap<Node, Double>();
        while (!stop.isVisited()) {
            for (var edge : selected.getNeighborEdges()) {
                var node = edge.getAnotherNode(selected);
                if (!node.isVisited()) {
                    var dTime = dTimeMap.getOrDefault(selected, time);
                    if (minHeap.updateDistance(node.getID(), Math.min(node.getDistance(), selected.getDistance() + edge.weight(dTime)))) {
                        dTimeMap.put(node, dTime + edge.weight(dTime) * 120);
                        node.setPrevious(selected);
                    }
                }
            }
            selected = minHeap.poll();
            selected.setVisited(true);
        }

        var routeEdges = new ArrayList<Edge>();
        while (selected.getPrevious() != null) {
            var edge = selected.getEdge(selected.getPrevious());
            routeEdges.add(edge);
            edge.setColor(Color.GREEN);
            selected = selected.getPrevious();
        }

        Collections.reverse(routeEdges);

        var sb = new StringBuilder(start.getID()).append(" -> ");
        selected = start;
        for (var e : routeEdges) {
            var next = e.getAnotherNode(selected);
            sb.append(next.getID()).append(" -> ");
            selected = next;
            e.addMachine(time, time += 120 * e.weight(time));
        }

        sb.append("END");
        return sb.toString();
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
