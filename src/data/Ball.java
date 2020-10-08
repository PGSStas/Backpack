package data;

public class Ball extends Shape {
    double radius;

    public Ball(double radius) {
        this.radius = radius;
    }

    @Override
    public double getVolume() {
        return 4.0 / 3 * Math.PI * Math.pow(radius, 3);
    }

    @Override
    public String toString() {
        return "Ball with radius " + radius + " and Volume " + RoundAvoid(getVolume(), 4);
    }
}
