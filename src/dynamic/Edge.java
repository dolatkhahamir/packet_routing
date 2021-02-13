package dynamic;

import canvas.CoordinatedScreen;
import canvas.Render;

import java.awt.*;
import java.util.ArrayList;

public class Edge implements Render {
    private final double length;
    private final Node node1;
    private final Node node2;
    private final ArrayList<Machine> machines;
    private Color color;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
        length = node1.getPoint().distanceFrom(node2.getPoint());
        machines = new ArrayList<>();
        color = Color.RED.darker().darker();
    }

    private int onWayCars(double time) {
        int counter = 0;
        for (var m : machines)
            if (m.isOnWay(time))
                counter++;
        return counter;
    }

    public double weight(double time) {
        return (onWayCars(time) * 0.3 + 1) * length;
    }

    public Node getAnotherNode(Node node) {
        return node.equals(node1) ? node2 : node1;
    }

    public void addMachine(double start, double stop) {
        machines.add(new Machine(start, stop));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public static CoordinatedScreen cs;
    public static double time = 0;
    @Override
    public void render(Graphics2D g2d) {
        var p1 = node1.getPoint();
        var p2 = node2.getPoint();
        g2d.setStroke(new BasicStroke(1f));
        g2d.setFont(new Font("serif", Font.PLAIN, 12));
        g2d.setColor(color);
        g2d.drawLine(cs.screenX(p1.x), cs.screenY(p1.y), cs.screenX(p2.x), cs.screenY(p2.y));
        if (onWayCars(time) == 0)
            return;
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(onWayCars(time)),
                (cs.screenX(p1.x) + cs.screenX(p2.x))/2, (cs.screenY(p1.y) + cs.screenY(p2.y))/2);
    }
}
