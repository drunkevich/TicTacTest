package drankodmitry.tictactest;

import android.graphics.Point;

import java.util.Random;

public class TicTacToeCore {
    private Mark[][] map = {{Mark.NONE, Mark.NONE, Mark.NONE}, {Mark.NONE, Mark.NONE, Mark.NONE}, {Mark.NONE, Mark.NONE, Mark.NONE}};
    private boolean isFinished = false;
    private int currentMove = 0;


    private Random rand = new Random();
    private Point winLineBegin = new Point();
    private Point winLineEnd = new Point();


    public TicTacToeCore() {
    }


    public Move move(int x, int y) {

        if (isFinished) {
            return new Move(Move.Status.ALREADY_FINISHED, Mark.NONE, -1, -1);
        }

        if ((x < 0) || (x > 2) || (y < 0) || (y > 2)) {

            return new Move(Move.Status.WRONG_MOVE, Mark.NONE, -1, -1);
        }
        if (map[x][y] != Mark.NONE) {
            return new Move(Move.Status.WRONG_MOVE, Mark.NONE, -1, -1);
        }
        currentMove++;
        Mark moveMark = (currentMove % 2 == 0) ? Mark.O : Mark.X;
        map[x][y] = moveMark;
        Move.Status moveStatus = evaluateStatus();
        return new Move(moveStatus, moveMark, x, y);
    }

    public Move getMove(Difficulty diff) {
        if (!isFinished) {
            Point p = calculateNextMove(diff);
            return move(p.x, p.y);
        } else {
            return move(-1, -1);
        }
    }

    private Move.Status evaluateStatus() {
        for (int i = 0; i < 3; i++) {
            if ((map[i][0] != Mark.NONE) && (map[i][0] == map[i][1]) && (map[i][0] == map[i][2])) {
                isFinished = true;
                winLineBegin.x = i;
                winLineBegin.y = -1;
                winLineEnd.x = i;
                winLineEnd.y = 3;
                if (map[i][0] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }
            if ((map[0][i] != Mark.NONE) && (map[0][i] == map[1][i]) && map[0][i] == map[2][i]) {
                isFinished = true;
                winLineBegin.x = -1;
                winLineBegin.y = i;
                winLineEnd.x = 3;
                winLineEnd.y = i;
                if (map[0][i] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }
        }
        if ((map[0][0] != Mark.NONE) && (map[0][0] == map[1][1]) && (map[0][0] == map[2][2])) {
            isFinished = true;
            winLineBegin.x = -1;
            winLineBegin.y = -1;
            winLineEnd.x = 3;
            winLineEnd.y = 3;
            if (map[0][0] == Mark.X) return Move.Status.X_WON;
            else return Move.Status.O_WON;
        }
        if ((map[2][0] != Mark.NONE) && (map[2][0] == map[1][1]) && (map[2][0] == map[0][2])) {
            isFinished = true;
            winLineBegin.x = 3;
            winLineBegin.y = -1;
            winLineEnd.x = -1;
            winLineEnd.y = 3;
            if (map[2][0] == Mark.X) return Move.Status.X_WON;
            else return Move.Status.O_WON;
        }

        if (currentMove == 9) {
            isFinished = true;
            return Move.Status.DRAW;
        }

        return Move.Status.PLAYING;
    }

    private Point calculateNextMove(Difficulty diff) {
        Point p = new Point(-1, -1);
        switch (diff) {
            case DUMB:
                p = randomMove();
                break;
            case NORMAL:
                p = forcedMove();
                if (p == null) p = randomMove();

                break;
        }

        //TODO
        return p;
    }

    private Point forcedMove() {
        Point p;
        if (currentMove % 2 == 0) {
            p = forcedMove(Mark.X);
            if (p == null) p = forcedMove(Mark.O);
        } else {
            p = forcedMove(Mark.O);
            if (p == null) p = forcedMove(Mark.X);
        }
        return p;
    }

    private Point forcedMove(Mark x) {
        for (int i = 0; i < 3; i++) {
            if ((map[i][0].n + map[i][1].n + map[i][2].n) * x.n == 2) {
                for (int j = 0; j < 3; j++) {
                    if (map[i][j] == Mark.NONE) return new Point(i, j);
                }
            }
            if ((map[0][i].n + map[1][i].n + map[2][i].n) * x.n == 2) {
                for (int j = 0; j < 3; j++) {
                    if (map[j][i] == Mark.NONE) return new Point(j, i);
                }
            }
        }
        if ((map[0][0].n + map[1][1].n + map[2][2].n) * x.n == 2) {
            for (int j = 0; j < 3; j++) {
                if (map[j][j] == Mark.NONE) return new Point(j, j);
            }
        }
        if ((map[2][0].n + map[1][1].n + map[0][2].n) * x.n == 2) {
            for (int j = 0; j < 3; j++) {
                if (map[j][2 - j] == Mark.NONE) return new Point(j, 2 - j);
            }
        }
        return null;
    }

    private Point randomMove() {
        Point p = new Point(-1, -1);
        int n = rand.nextInt(9 - currentMove);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (map[i][j] == Mark.NONE) {
                    n--;
                    if (n == -1) p = new Point(i, j);
                }
            }
        }
        return p;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Point getWinLineBegin() {
        return winLineBegin;
    }

    public Point getWinLineEnd() {
        return winLineEnd;
    }

    public Mark[][] getMap() {
        return map;
    }

    public enum Difficulty {DUMB, EASY, NORMAL}

    public enum Mark {
        NONE(0), X(1), O(-1);
        int n;

        Mark(int i) {
            n = i;
        }
    }
}
