package io.masterplan.infrastucture.util.vector;

public interface IReadOnlyVec2D {
    public double getX();
    public double getY();

    public double dot(Vec2D v);
    public double mag();
}
