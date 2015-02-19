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

    public enum Status {ALREADY_FINISHED, PLAYING, WRONG_MOVE, X_WON, O_WON, DRAW}
}
