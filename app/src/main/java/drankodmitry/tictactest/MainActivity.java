package drankodmitry.tictactest;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;


public class MainActivity extends Activity {

    private TicTacToeCore.Difficulty player1;
    private TicTacToeCore.Difficulty player2;
    private TicTacToeCore core = new TicTacToeCore();
    private TicTacToeFieldView field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        field = new TicTacToeFieldView(core, this);
        setContentView(field);
        player1 = null;
        player2 = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(event.getX());
            int y = Math.round(event.getY());
            Point cell = field.getSet().getCell(x, y);
            Move move = core.move(cell.x, cell.y);

            if ((move.status != Move.Status.ALREADY_FINISHED) && (move.status != Move.Status.WRONG_MOVE)) {
                field.playerDraw(move.mark, x, y);
                field.invalidate();
                if (move.status != Move.Status.PLAYING) {
                    Toast.makeText(getApplicationContext(), "" + move.status, Toast.LENGTH_LONG).show();
                } else {
                    Move response = core.getMove(TicTacToeCore.Difficulty.NORMAL);
                    if ((response.status != Move.Status.ALREADY_FINISHED) && (response.status != Move.Status.WRONG_MOVE)) {
                        field.autoDraw(response.mark, response.x, response.y);
                        field.invalidate();
                        if (response.status != Move.Status.PLAYING) {
                            Toast.makeText(getApplicationContext(), "" + response.status, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
