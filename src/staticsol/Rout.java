package staticsol;

import java.util.ArrayList;

public class Rout extends ArrayList<Edge> {

    private final double arriveTime;

    public Rout(ArrayList<Edge> edges, double arriveTime) {
        addAll(edges);
        for (var e : edges)
            e.changeTraffic(1);
        this.arriveTime = arriveTime;
    }

    public boolean check(double time) {
        if (time < arriveTime)
            return false;
        forEach(e -> e.changeTraffic(-1));
        return true;
    }
}
