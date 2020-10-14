package data;

public class Tetrahedron extends Shape {
    double edge;

    public Tetrahedron(double edge) {
        this.edge = edge;
    }

    @Override
    public double getVolume() {
        return Math.pow(edge, 3) / (6 * Math.sqrt(2));
    }

    @Override
    public String toString() {
        return "Tetrahedron with edge " + edge + " and Volume " + RoundAvoid(getVolume(), 4);
    }
}
