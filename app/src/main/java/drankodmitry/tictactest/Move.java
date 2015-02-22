package drankodmitry.tictactest;

public class Move {
    public final Status status;
    public final TicTacToeCore.Mark mark;
    public final int x;
    public final int y;

    public Move(Status _status, TicTacToeCore.Mark _mark, int _x, int _y) {
        mark = _mark;
        x = _x;
        y = _y;
        status = _status;
    }

    public enum Status {
        ALREADY_FINISHED(101), PLAYING(100), WRONG_MOVE(102), X_WON(1), O_WON(-1), DRAW(0);
        public int n;

        Status(int _n) {
            n = _n;
        }
    }
}
