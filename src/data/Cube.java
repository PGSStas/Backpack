package data;

public class Cube extends Shape {
    double edge;

    public Cube(double edge) {
        this.edge = edge;
    }

    @Override
    public double getVolume() {
        return Math.pow(edge, 3);
    }

    @Override
    public String toString() {
        return "Cube with edge " + edge + " and Volume " + RoundAvoid(getVolume(), 4);
    }
}
