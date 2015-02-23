package drankodmitry.tictactest;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TicTacToeCore {
    private Map map = new Map();
    private boolean isFinished = false;


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
        if (map.getField()[x][y] != Mark.NONE) {
            return new Move(Move.Status.WRONG_MOVE, Mark.NONE, -1, -1);
        }
        Mark moveMark = (map.getMoveNumber() % 2 == 0) ? Mark.X : Mark.O;
        map = map.set(x, y);
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

    public Mark getCurrentPlayer() {
        return (map.getMoveNumber() % 2 == 0) ? Mark.X : Mark.O;
    }

    private Move.Status evaluateStatus() {
        Mark[][] mapf = map.getField();
        for (int i = 0; i < 3; i++) {
            if ((mapf[i][0] != Mark.NONE) && (mapf[i][0] == mapf[i][1]) && (mapf[i][0] == mapf[i][2])) {
                isFinished = true;
                winLineBegin.x = i;
                winLineBegin.y = -1;
                winLineEnd.x = i;
                winLineEnd.y = 3;
                if (mapf[i][0] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }
            if ((mapf[0][i] != Mark.NONE) && (mapf[0][i] == mapf[1][i]) && mapf[0][i] == mapf[2][i]) {
                isFinished = true;
                winLineBegin.x = -1;
                winLineBegin.y = i;
                winLineEnd.x = 3;
                winLineEnd.y = i;
                if (mapf[0][i] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }
        }
        if ((mapf[0][0] != Mark.NONE) && (mapf[0][0] == mapf[1][1]) && (mapf[0][0] == mapf[2][2])) {
            isFinished = true;
            winLineBegin.x = -1;
            winLineBegin.y = -1;
            winLineEnd.x = 3;
            winLineEnd.y = 3;
            if (mapf[0][0] == Mark.X) return Move.Status.X_WON;
            else return Move.Status.O_WON;
        }
        if ((mapf[2][0] != Mark.NONE) && (mapf[2][0] == mapf[1][1]) && (mapf[2][0] == mapf[0][2])) {
            isFinished = true;
            winLineBegin.x = 3;
            winLineBegin.y = -1;
            winLineEnd.x = -1;
            winLineEnd.y = 3;
            if (mapf[2][0] == Mark.X) return Move.Status.X_WON;
            else return Move.Status.O_WON;
        }

        if (map.getMoveNumber() == 9) {
            isFinished = true;
            return Move.Status.DRAW;
        }

        return Move.Status.PLAYING;
    }

    private Point calculateNextMove(Difficulty diff) {
        switch (diff) {
            case DUMB:
                return randomMove();
            case EASY:
                Point p = forcedMove();
                if (p == null) return randomMove();
                else return p;
            case NORMAL:
                if (map.getMoveNumber() == 0) return randomMove();
                List<Point> moves = new ArrayList<Point>(Arrays.asList(map.getFreeCells()));
                Collections.shuffle(moves, rand);
                Point tmp = new Point(-1, -1);
                for (Point point : moves) {
                    int result = minimax(map.set(point.x, point.y), (map.getMoveNumber() % 2 == 0) ? Mark.O.n : Mark.X.n);
                    if ((result == -1) || (map.getMoveNumber() == 1 && result == 0)) {
                        return point;
                    } else {
                        if (result == 0) {
                            tmp = point;
                        }
                    }
                }
                if (tmp.x != -1) {
                    return tmp;
                } else {
                    Log.d("switch", "random");
                    return randomMove();
                }
        }
        Log.d("switch", "error");
        return null;
    }

    private Point forcedMove() {
        Point p;
        if (map.getMoveNumber() % 2 == 0) {
            p = forcedMove(Mark.X);
            if (p == null) p = forcedMove(Mark.O);
        } else {
            p = forcedMove(Mark.O);
            if (p == null) p = forcedMove(Mark.X);
        }
        return p;
    }

    private Point forcedMove(Mark x) {
        Mark[][] mapf = map.getField();
        for (int i = 0; i < 3; i++) {
            if ((mapf[i][0].n + mapf[i][1].n + mapf[i][2].n) * x.n == 2) {
                for (int j = 0; j < 3; j++) {
                    if (mapf[i][j] == Mark.NONE) return new Point(i, j);
                }
            }
            if ((mapf[0][i].n + mapf[1][i].n + mapf[2][i].n) * x.n == 2) {
                for (int j = 0; j < 3; j++) {
                    if (mapf[j][i] == Mark.NONE) return new Point(j, i);
                }
            }
        }
        if ((mapf[0][0].n + mapf[1][1].n + mapf[2][2].n) * x.n == 2) {
            for (int j = 0; j < 3; j++) {
                if (mapf[j][j] == Mark.NONE) return new Point(j, j);
            }
        }
        if ((mapf[2][0].n + mapf[1][1].n + mapf[0][2].n) * x.n == 2) {
            for (int j = 0; j < 3; j++) {
                if (mapf[j][2 - j] == Mark.NONE) return new Point(j, 2 - j);
            }
        }
        return null;
    }

    private Point randomMove() {
        Point[] cells = map.getFreeCells();
        int n = rand.nextInt(9 - map.getMoveNumber());
        return cells[n];
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

    private int minimax(Map node, int player) {

        Move.Status status = node.evaluateStatus();
        if (status != Move.Status.PLAYING) return status.n * player;

        int best = -1;
        Point[] moves = node.getFreeCells();
        for (Point p : moves) {
            int result = -minimax(node.set(p.x, p.y), -player);
            if (result > best) best = result;
        }
        return best;
    }

    public enum Difficulty {
        DUMB(1), EASY(2), NORMAL(3);
        int n;

        Difficulty(int _n) {
            n = _n;
        }
    }

    public enum Mark {
        NONE(0), X(1), O(-1);
        int n;

        Mark(int i) {
            n = i;
        }
    }

    private static class Map {

        private Mark[][] field;

        public Map() {
            field = new Mark[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    field[i][j] = Mark.NONE;
                }
            }
        }

        private Map(Mark[][] f) {
            field = f;
        }

        public Map copy() {
            return new Map(this.getField());
        }

        public int getMoveNumber() {
            int n = 9;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == Mark.NONE) n--;
                }
            }
            return n;
        }

        public Map set(int i, int j) {
            if (field[i][j] != Mark.NONE) return null;
            Mark[][] f = this.getField();
            int n = this.getMoveNumber();
            f[i][j] = (n % 2 == 0) ? Mark.X : Mark.O;
            return new Map(f);
        }

        public Mark[][] getField() {
            Mark[][] f = new Mark[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    f[i][j] = field[i][j];
                }
            }
            return f;
        }

        public Point[] getFreeCells() {
            Point[] cells = new Point[9 - getMoveNumber()];
            int n = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == Mark.NONE) {
                        cells[n] = new Point(i, j);
                        n++;
                    }
                }
            }
            return cells;
        }

        public Move.Status evaluateStatus() {
            for (int i = 0; i < 3; i++) {
                if ((field[i][0] != Mark.NONE) && (field[i][0] == field[i][1]) && (field[i][0] == field[i][2])) {
                    if (field[i][0] == Mark.X) return Move.Status.X_WON;
                    else return Move.Status.O_WON;
                }
                if ((field[0][i] != Mark.NONE) && (field[0][i] == field[1][i]) && field[0][i] == field[2][i]) {
                    if (field[0][i] == Mark.X) return Move.Status.X_WON;
                    else return Move.Status.O_WON;
                }
            }
            if ((field[0][0] != Mark.NONE) && (field[0][0] == field[1][1]) && (field[0][0] == field[2][2])) {
                if (field[0][0] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }
            if ((field[2][0] != Mark.NONE) && (field[2][0] == field[1][1]) && (field[2][0] == field[0][2])) {
                if (field[2][0] == Mark.X) return Move.Status.X_WON;
                else return Move.Status.O_WON;
            }

            if (getMoveNumber() == 9) {
                return Move.Status.DRAW;
            }

            return Move.Status.PLAYING;
        }
    }
}
