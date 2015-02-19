package drankodmitry.tictactest;


import android.graphics.Point;

import java.util.Random;

public class PointsSet {

    private final int Width;
    private final int Height;
    private final Point TL;
    private final Point TR;
    private final Point BL;
    private final Point BR;
    private final Point LT;
    private final Point LB;
    private final Point RT;
    private final Point RB;
    private final int step;
    private final int diff;
    private Random rand = new Random();


    public PointsSet(int _Width, int _Height) {
        Width = _Width;
        Height = _Height;
        diff = (Height - Width) / 2;
        step = Width / 10;
        TL = new Point(Width / 3 + nextStep(), diff + nextStep());
        TR = new Point(2 * Width / 3 + nextStep(), diff + nextStep());
        BL = new Point(Width / 3 + nextStep(), Width + diff + nextStep());
        BR = new Point(2 * Width / 3 + nextStep(), Width + diff + nextStep());
        LT = new Point(nextStep(), Width / 3 + diff + nextStep());
        LB = new Point(nextStep(), 2 * Width / 3 + diff + nextStep());
        RT = new Point(Width + nextStep(), Width / 3 + diff + nextStep());
        RB = new Point(Width + nextStep(), 2 * Width / 3 + diff + nextStep());
    }

    public int nextStep() {
        return rand.nextInt(step) - step / 2;
    }

    public int nextAng() {
        return rand.nextInt(360);
    }

    public int getStep() {
        return step;
    }

    public Point getRB() {
        return RB;
    }

    public Point getRT() {
        return RT;
    }

    public Point getLT() {
        return LT;
    }

    public Point getLB() {
        return LB;
    }

    public Point getTL() {
        return TL;
    }

    public Point getTR() {
        return TR;
    }

    public Point getBL() {
        return BL;
    }

    public Point getBR() {
        return BR;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int getDiff() {
        return diff;
    }

    public Point getCell(int x, int y) {

        int X;
        int Y;
        Point o = new Point(x, y);
        if (isNearNull(o, TL, BL))
            X = 0;
        else if (isNearNull(o, TR, BR))
            X = 1;
        else
            X = 2;
        if (isNearNull(o, LT, RT))
            Y = 0;
        else if (isNearNull(o, LB, RB))
            Y = 1;
        else
            Y = 2;
        return new Point(X, Y);
    }

    private boolean isNearNull(Point x, Point a, Point b) {
        Point ab = minus(a, b);
        Point ax = minus(a, x);
        int abXax = ab.x * ax.y - ab.y * ax.x;
        int abXa = ab.x * a.y - ab.y * a.x;
        return (Math.signum(abXax) * Math.signum(abXa) >= 0);
    }

    private Point minus(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

}
