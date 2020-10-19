package data;

public abstract class Shape {
    public abstract double getEdge();
    public abstract double getVolume();
    public abstract String getType();

    protected double RoundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}

