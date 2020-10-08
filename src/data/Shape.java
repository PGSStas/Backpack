package data;

public abstract class Shape {
    public abstract double getVolume();

    protected double RoundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}

