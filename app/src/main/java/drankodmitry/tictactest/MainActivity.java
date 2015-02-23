package drankodmitry.tictactest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private boolean active;
    private TicTacToeCore core;
    private TicTacToeFieldView field;
    private TicTacToeCore.Difficulty player1 = null;
    private TicTacToeCore.Difficulty player2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        String p1 = intent.getStringExtra("player1");
        if (!"".equals(p1)) player1 = TicTacToeCore.Difficulty.valueOf(p1);
        String p2 = intent.getStringExtra("player2");
        if (!"".equals(p2)) player2 = TicTacToeCore.Difficulty.valueOf(p2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((player1 != null) && (player2 == null)) {
            timerResponse(100);
        }
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
                if ((player1 == null) && (player2 == null)) {
                    playMove(Math.round(event.getX()), Math.round(event.getY()), false);
                } else if ((player1 != null) && (player2 != null)) {
                    getResponse();
                } else {
                    playMove(Math.round(event.getX()), Math.round(event.getY()), true);
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
                if ((player1 != null) && (player2 == null)) {
                    timerResponse(100);
                }
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
        Move response = core.getMove((core.getCurrentPlayer() == TicTacToeCore.Mark.X) ? player1 : player2);
        Log.d("response", "" + response.mark + " " + response.x + " " + response.y);
        if ((response.status != Move.Status.ALREADY_FINISHED) && (response.status != Move.Status.WRONG_MOVE)) {
            field.autoDraw(response.mark, response.x, response.y);
            field.invalidate();
            if (response.status != Move.Status.PLAYING) {
                showFinishDialog(response.status);
            }
        }
        active = true;
    }

    private void playMove(int x, int y, boolean needResponse) {
        Point cell = field.getSet().getCell(x, y);
        Move move = core.move(cell.x, cell.y);

        if ((move.status != Move.Status.ALREADY_FINISHED) && (move.status != Move.Status.WRONG_MOVE)) {
            field.playerDraw(move.mark, x, y);
            field.invalidate();
            if (move.status != Move.Status.PLAYING) {
                showFinishDialog(move.status);
            } else {
                if (needResponse) {
                    timerResponse(200);
                }
            }
        }
    }

    private void timerResponse(int ms) {
        active = false;
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getResponse();
                    }
                });
            }
        }, ms);
    }

}
