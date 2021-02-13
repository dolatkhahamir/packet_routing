package staticsol;

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
        Edge.cs = gp;
        gp.setShowAxis(false);
        var tf = new EnTextField("time start end", true);
        tf.addActionListener(e -> {
            var info = tf.getText().split(" ");
            System.out.println(Graph.dijkstraAlgorithm(graph, info[1], info[2], Double.parseDouble(info[0])));
            f.repaint();
        });
        var set = new HashSet<Point2D>();
        double finalXAvg = xAvg;
        double finalYAvg = yAvg;
        graph.getNodes().forEach(e -> set.add(e.getPoint().affectFunctionToX(x -> (x-finalXAvg)*1000).affectFunctionToY(x -> (x-finalYAvg)*1000)));
        dynamic.Edge.cs = gp;
        gp.addPointSetToDraw(new Color[] {Color.RED}, set);
        gp.addRender(graph.getEdges().toArray(new Edge[] {}));
        SwingUtilities.invokeLater(f);
        f.add(gp);

        var wrapper = new JPanel(new GridLayout());
        wrapper.add(tf);
        f.add(wrapper, BorderLayout.SOUTH);
        f.repaint();
        f.revalidate();
        scn = new Scanner(System.in);
        graph.start(scn);
    }
}
