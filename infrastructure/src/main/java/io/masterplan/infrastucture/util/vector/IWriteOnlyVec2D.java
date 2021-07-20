package io.masterplan.infrastucture.util.vector;

public interface IWriteOnlyVec2D {

    void set(Vec2D vec);
    void set(double x, double y);

    void add(Vec2D vec);
    void add(double dX, double dY);

    void sub(Vec2D vec);
    void sub(double dX, double dY);

    void mul(double c);
    void div(double c);
}
