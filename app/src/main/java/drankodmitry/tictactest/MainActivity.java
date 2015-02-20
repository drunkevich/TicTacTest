package drankodmitry.tictactest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private boolean active;
    private TicTacToeCore core;
    private TicTacToeFieldView field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        core = new TicTacToeCore();
        field = new TicTacToeFieldView(core, this);
        setContentView(field);
        active = true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (active) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                Point cell = field.getSet().getCell(x, y);
                Move move = core.move(cell.x, cell.y);

                if ((move.status != Move.Status.ALREADY_FINISHED) && (move.status != Move.Status.WRONG_MOVE)) {
                    field.playerDraw(move.mark, x, y);
                    field.invalidate();
                    if (move.status != Move.Status.PLAYING) {
                        showFinishDialog(move.status);
                    } else {
                        active = false;
                        new Timer().schedule(new ResponseTimerTask(MainActivity.this), 200);
                    }
                }
            }
        }
        return true;
    }

    private void showFinishDialog(Move.Status status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        String winner;
        if (status == Move.Status.X_WON) winner = "Крестики выиграли";
        else if (status == Move.Status.O_WON) winner = "Нолики выиграли";
        else winner = "Ничья";
        builder.setMessage(winner + "\nЕще раз?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init();
            }
        });
        builder.setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void getResponse() {
        Move response = core.getMove(TicTacToeCore.Difficulty.NORMAL);
        if ((response.status != Move.Status.ALREADY_FINISHED) && (response.status != Move.Status.WRONG_MOVE)) {
            field.autoDraw(response.mark, response.x, response.y);
            field.invalidate();
            if (response.status != Move.Status.PLAYING) {
                showFinishDialog(response.status);
            }
        }
        active = true;
    }

    class ResponseTimerTask extends TimerTask {
        private MainActivity instance;

        ResponseTimerTask(MainActivity i) {
            instance = i;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.getResponse();
                }
            });
        }
    }
}
