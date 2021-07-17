package io.masterplan.client.util.vector;

public class Vec2D implements IVec2D {

    private double x, y;

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public static Vec2D add(Vec2D v1, Vec2D v2) {
        return new Vec2D(v1.x + v2.x, v1.y + v2.y);
    }

    @Override
    public void set(Vec2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    @Override
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vec2D vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public void add(double dX, double dY) {
        this.x += dX;
        this.y += dY;
    }

    public static Vec2D sub(Vec2D v1, Vec2D v2) {
        return new Vec2D(v1.x - v2.x, v1.y - v2.y);
    }

    public void sub(Vec2D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    public void sub(double dX, double dY) {
        this.x -= dX;
        this.y -= dY;
    }

    public double dot(Vec2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    public static Vec2D mul(Vec2D v, double c) {
        return new Vec2D(v.x * c, v.y * c);
    }

    public void mul(double c) {
        this.x *= c;
        this.y *= c;
    }

    public static Vec2D div(Vec2D v, double c) {
        return new Vec2D(v.x / c, v.y / c);
    }

    public void div(double c) {
        this.x /= c;
        this.y /= c;
    }

    public double mag() {
        return Math.sqrt(x*x + y*y);
    }

}
