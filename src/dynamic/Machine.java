package dynamic;

public class Machine {
    private final double startTime;
    private final double arriveTime;

    public Machine(double startTime, double arriveTime) {
        this.startTime = startTime;
        this.arriveTime = arriveTime;
    }

    public boolean isOnWay(double time) {
        return time >= startTime && time < arriveTime;
    }

    @Override
    public String toString() {
        return "Machine{" + "startTime=" + startTime + ", arriveTime="
                + arriveTime + '}';
    }
}
