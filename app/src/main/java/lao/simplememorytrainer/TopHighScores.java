package lao.simplememorytrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TopHighScores extends AppCompatActivity {

    private static int NUMBER_OF_HIGH_SCORE_TO_DISPLAY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_high_scores);

        fillTextWithTopScores();
    }

    private void fillTextWithTopScores() {

        TextView topHScores = (TextView) findViewById(R.id.idTxtTopHS);
        TextView topNames   = (TextView) findViewById(R.id.idTxtTopName);
        TextView topDates   = (TextView) findViewById(R.id.idTxtTopDate);

        String stringTopHScores = "";
        String stringTopNames = "";
        String stringTopDates = "";
        ArrayList<HighScoreBean> scores =  MainActivity.getDBHangler().getHighScoresList();

        int contador = 0;
        for ( HighScoreBean bean : scores ){

            contador++;
            stringTopHScores = stringTopHScores + bean.getScore() + "\n";
            stringTopNames   = stringTopNames + bean.getName().toUpperCase() + "\n";
            stringTopDates   = stringTopDates + bean.getDate() + "\n";

            if (NUMBER_OF_HIGH_SCORE_TO_DISPLAY > 0 && contador == NUMBER_OF_HIGH_SCORE_TO_DISPLAY) {
                break;
            }
        }
        if ( stringTopNames.length() == 0 ) {
            stringTopNames = "Play a game to put your name here!\nHave fun!";
        }
        topHScores.setText(stringTopHScores);
        topNames.setText(stringTopNames);
        topDates.setText(stringTopDates);
    }

    public void onClickButtonClose(View v) {
        finish();
    }
}
