package staticsol;

import canvas.CoordinatedScreen;
import canvas.Render;

import java.awt.*;
import java.util.Objects;

public class Edge implements Render {
    private final double length;
    private final Node node1;
    private final Node node2;
    private int traffic;
    private Color color;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
        length = node1.getPoint().distanceFrom(node2.getPoint());
        color = Color.RED.darker().darker();
        traffic = 0;
    }

    public double weight() {
        return (traffic * 0.3 + 1) * length;
    }

    public void changeTraffic(int change) {
        traffic += change;
    }

    public Node getAnotherNode(Node node) {
        return node.equals(node1) ? node2 : node1;
    }

    public double getLength() {
        return length;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Edge edge = (Edge) o;
        return Objects.equals(node1.getPoint(), edge.node1.getPoint()) && Objects
                .equals(node2.getPoint(), edge.node2.getPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(node1.getPoint(), node2.getPoint());
    }

    public static CoordinatedScreen cs;
    @Override
    public void render(Graphics2D g2d) {
        var p1 = node1.getPoint();
        var p2 = node2.getPoint();
        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(color);
        try {
            g2d.drawLine(cs.screenX(p1.x), cs.screenY(p1.y), cs.screenX(p2.x), cs.screenY(p2.y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
