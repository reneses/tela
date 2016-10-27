package io.reneses.tela.modules.instagram.models;

/**
 * Position class.
 */
public class Position {

    private double x, y;

    /**
     * Position constructor
     */
    public Position() {
    }

    /**
     * Position constructor
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the field <code>x</code>.
     *
     * @return a double.
     */
    public double getX() {
        return x;
    }

    /**
     * Setter for the field <code>x</code>.
     *
     * @param x a double.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Getter for the field <code>y</code>.
     *
     * @return a double.
     */
    public double getY() {
        return y;
    }

    /**
     * Setter for the field <code>y</code>.
     *
     * @param y a double.
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (Double.compare(position.x, x) != 0) return false;
        return Double.compare(position.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
