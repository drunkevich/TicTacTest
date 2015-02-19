package drankodmitry.tictactest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

import java.util.LinkedList;
import java.util.Random;

public class TicTacToeFieldView extends View {
    private TicTacToeCore core;
    private Random rand = new Random();
    private PointsSet set;
    private Paint pen;
    private Paint back;
    private LinkedList<Figure> figures = new LinkedList<Figure>();

    public TicTacToeFieldView(TicTacToeCore _core, Context context) {
        super(context);
        core = _core;
        back = new Paint();
        back.setColor(Color.argb(255, 230 + nextCol(), 220 + nextCol(), 200 + nextCol()));
        back.setStyle(Paint.Style.FILL);
        pen = new Paint();
        pen.setStyle(Paint.Style.STROKE);
        pen.setColor(Color.argb(255, 30 + nextCol(), 100 + nextCol(), 200 + nextCol()));
        pen.setAntiAlias(true);
        pen.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (set == null) set = new PointsSet(getWidth(), getHeight());
        drawField(canvas);

        for (Figure f : figures) {
            drawFigure(f, canvas);
        }

        if (core.isFinished()) {
            int x1 = (int) Math.round((.5 + core.getWinLineBegin().x) * set.getWidth() / 3);
            int y1 = (int) Math.round((.5 + core.getWinLineBegin().y) * set.getWidth() / 3) + set.getDiff();
            int x2 = (int) Math.round((.5 + core.getWinLineEnd().x) * set.getWidth() / 3);
            int y2 = (int) Math.round((.5 + core.getWinLineEnd().y) * set.getWidth() / 3) + set.getDiff();
            canvas.drawLine(x1, y1, x2, y2, pen);
        }
    }

    private int nextCol() {
        return rand.nextInt(40) - 20;
    }

    private void drawFigure(Figure f, Canvas canvas) {
        canvas.save();
        canvas.rotate(f.angle, f.rect.centerX(), f.rect.centerY());
        if (f.mark == TicTacToeCore.Mark.O) {
            canvas.drawOval(f.rect, pen);
        } else {
            canvas.drawLine(f.rect.left, f.rect.bottom, f.rect.right, f.rect.top, pen);
            canvas.drawLine(f.rect.left, f.rect.top, f.rect.right, f.rect.bottom, pen);
        }
        canvas.restore();
    }

    private void drawField(Canvas canvas) {
        canvas.drawPaint(back);

        drawLine(set.getTL(), set.getBL(), canvas);
        drawLine(set.getTR(), set.getBR(), canvas);
        drawLine(set.getLT(), set.getRT(), canvas);
        drawLine(set.getLB(), set.getRB(), canvas);
    }

    private void drawLine(Point p1, Point p2, Canvas canvas) {
        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, pen);
    }

    public void playerDraw(TicTacToeCore.Mark mark, int x, int y) {
        figures.add(new Figure(mark, x, y));
    }

    public void autoDraw(TicTacToeCore.Mark mark, int X, int Y) {
        int x = (int) Math.round((.5 + X) * set.getWidth() / 3);
        int y = (int) Math.round((.5 + Y) * set.getWidth() / 3) + set.getDiff();
        playerDraw(mark, x + set.nextStep(), y + set.nextStep());
    }

    public PointsSet getSet() {
        return set;
    }

    private class Figure {
        TicTacToeCore.Mark mark;
        RectF rect;
        int angle;

        Figure(TicTacToeCore.Mark _mark, int x, int y) {
            mark = _mark;
            int line = set.getStep();
            rect = new RectF(x - line + set.nextStep() / 2, y - line + set.nextStep() / 2, x + line + set.nextStep() / 2, y + line + set.nextStep() / 2);
            angle = set.nextAng();
        }
    }
}
