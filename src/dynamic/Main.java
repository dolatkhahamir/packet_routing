package dynamic;

import canvas.Graph2DPanel;
import jmath.datatypes.tuples.Point2D;
import swingutils.EnTextField;
import swingutils.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var graph = new Graph();
        var scn = new Scanner(new File("m4.txt"));
        var nums = scn.nextLine().split(" ");
        var n = Integer.parseInt(nums[0]);
        var m = Integer.parseInt(nums[1]);
        double xAvg = 0;
        double yAvg = 0;
        for (int i = 0; i < n; i++) {
            var info = scn.nextLine().split(" ");
            var x = Double.parseDouble(info[2]);
            var y = Double.parseDouble(info[1]);
            graph.addNode(info[0], x, y);
            xAvg += x;
            yAvg += y;
        }
        xAvg /= n;
        yAvg /= n;
        for (int i = 0; i < m; i++) {
            var info = scn.nextLine().split(" ");
            graph.addEdge(info[0], info[1]);
        }
        scn.close();

        var f = new MainFrame("Graph visualization", true);
        var gp = new Graph2DPanel();
        gp.setShowAxis(false);
        var tf = new EnTextField("time start end", true);
        tf.addActionListener(e -> {
            var info = tf.getText().split(" ");
            System.out.println(graph.dijkstraAlgorithm(info[1], info[2], Edge.time = Double.parseDouble(info[0])));
            f.repaint();
        });
        var tf2 = new EnTextField("special time", true);
        tf2.addActionListener(e -> {
            Edge.time = Double.parseDouble(tf2.getText());
            f.repaint();
        });
        var set = new HashSet<Point2D>();
        double finalXAvg = xAvg;
        double finalYAvg = yAvg;
        graph.getNodes().forEach(e -> set.add(e.getPoint().affectFunctionToX(x -> (x-finalXAvg)*1000).affectFunctionToY(x -> (x-finalYAvg)*1000)));
        Edge.cs = gp;
        gp.addPointSetToDraw(new Color[] {Color.RED}, set);
        gp.addRender(graph.getEdges().toArray(new Edge[]{}));
        SwingUtilities.invokeLater(f);
        f.add(gp);

        var l = new JLabel("0", JLabel.CENTER);
        final var timer = new Timer(30, e -> {
            Edge.time += 0.01;
            l.setText(String.valueOf(round(Edge.time, 3)));
            gp.repaint();
        });
        var b = new JButton("Animate");
        b.setForeground(Color.RED);
        b.addActionListener(e -> {
            if (timer.isRunning()) {
                b.setForeground(Color.RED);
                timer.stop();
            } else {
                b.setForeground(Color.GREEN);
                timer.start();
            }
        });
        var wrapper = new JPanel(new GridLayout());
        wrapper.add(tf);
        wrapper.add(tf2);
        wrapper.add(b);
        wrapper.add(l);
        var tf3 = new EnTextField("loop delay in millis", true);
        tf3.addActionListener(e -> timer.setDelay(Integer.parseInt(tf3.getText())));
        wrapper.add(tf3);
        var b2 = new JButton("reset");
        b2.addActionListener(e -> graph.getEdges().forEach(edge -> {
            edge.getMachines().clear();
            edge.setColor(Color.RED.darker().darker());
            timer.stop();
            b.setForeground(Color.RED);
            gp.repaint();
        }));
        b2.addActionListener(e -> Edge.time = 0);
        b2.addActionListener(e -> l.setText("0"));
        f.add(wrapper, BorderLayout.SOUTH);
        wrapper.add(b2);
        f.repaint();
        f.revalidate();
        scn = new Scanner(System.in);
        graph.start(scn);
    }

    private static double round(double num, int precision) {
        if (!Double.toString(num).contains("."))
            return num;
        String res = num + "0".repeat(20);
        return Double.parseDouble(res.substring(0, res.indexOf('.') + precision + 1));
    }
}
