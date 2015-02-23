package drankodmitry.tictactest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;


public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button start = (Button) findViewById(R.id.ButtonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(MainMenuActivity.this, MainActivity.class);
                RadioGroup group1 = (RadioGroup) findViewById(R.id.RadioGroup1);
                switch (group1.getCheckedRadioButtonId()) {
                    case R.id.RadioButton11:
                        startGame.putExtra("player1", "");
                        break;
                    case R.id.RadioButton12:
                        startGame.putExtra("player1", TicTacToeCore.Difficulty.DUMB.toString());
                        break;
                    case R.id.RadioButton13:
                        startGame.putExtra("player1", TicTacToeCore.Difficulty.EASY.toString());
                        break;
                    case R.id.RadioButton14:
                        startGame.putExtra("player1", TicTacToeCore.Difficulty.NORMAL.toString());
                        break;
                }
                RadioGroup group2 = (RadioGroup) findViewById(R.id.RadioGroup2);
                switch (group2.getCheckedRadioButtonId()) {
                    case R.id.RadioButton21:
                        startGame.putExtra("player2", "");
                        break;
                    case R.id.RadioButton22:
                        startGame.putExtra("player2", TicTacToeCore.Difficulty.DUMB.toString());
                        break;
                    case R.id.RadioButton23:
                        startGame.putExtra("player2", TicTacToeCore.Difficulty.EASY.toString());
                        break;
                    case R.id.RadioButton24:
                        startGame.putExtra("player2", TicTacToeCore.Difficulty.NORMAL.toString());
                        break;
                }
                startActivity(startGame);
            }
        });
    }

}
