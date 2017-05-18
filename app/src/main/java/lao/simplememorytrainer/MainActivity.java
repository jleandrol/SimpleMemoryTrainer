package lao.simplememorytrainer;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * SIMPLE MEMORY TRAINER
 * https://github.com/jleandrol/SimpleMemoryTrainer.git
 * Version 1.0 - MAR/2017
 * Author Leandro Oliveira ( jleandrol@gmail.com )
 */
public class MainActivity extends AppCompatActivity {

    private Random random;
    private ImageButton btnCyan = null;
    private ImageButton btnBlue = null;
    private ImageButton btnRed = null;
    private ImageButton btnGray = null;
    private ImageButton btnGreen = null;
    private String statusMessage = "";
    private static MyDBHandler myDBHandler;
    private AlertDialog alertDialog = null;
    private EditText txtName = null;
    private int userTryingPosition;
    private int actualScore;
    ImageButton imageButtonClicked = null;
    private MediaPlayer buttonSound = null;
    ArrayList<ImageButton> listSequenceGame = new ArrayList();
    Button btnStartGame = null;
    private boolean isGameRunning = false;

    public static MyDBHandler getDBHangler() {
        return myDBHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(lao.simplememorytrainer.R.layout.activity_main);

        myDBHandler = new MyDBHandler(this, null, null, 0);

        initializeImageButtons();
        updateScore("");

        btnStartGame = (Button) findViewById(R.id.idBtnStartGame);
    }

    private void initializeImageButtons() {
        btnCyan = (ImageButton) findViewById(lao.simplememorytrainer.R.id.idImgBtnCyan);
        btnBlue = (ImageButton) findViewById(lao.simplememorytrainer.R.id.idImgBtnBlue);
        btnRed = (ImageButton) findViewById(lao.simplememorytrainer.R.id.idImgBtnRed);
        btnGray = (ImageButton) findViewById(lao.simplememorytrainer.R.id.idImgGray);
        btnGreen = (ImageButton) findViewById(lao.simplememorytrainer.R.id.idImgBtnGreen);
    }

    Handler handlerImgButton = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            imageButtonClicked.startAnimation(GameUtils.getAnimation());
        }
    };

    Handler handlerScore = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TextView txtScore = (TextView) findViewById(lao.simplememorytrainer.R.id.txtScore);
            txtScore.setText(statusMessage.toUpperCase());
        }
    };

    public void onClickIdBtnStartGame(View v) {
        if ( isGameRunning ) {
            Toast.makeText(getApplicationContext(), "Game is running can not stop.", Toast.LENGTH_SHORT).show();
            return; // do nothing
        }
        updateScore("");
        random = new Random();
        listSequenceGame.clear();
        userTryingPosition = -1;
        updateActualScore();
        addStepAndPlayTheSequence();
    }

    private void endGame() {
        showDialogWhenIsNewHighScore();
        listSequenceGame.clear();
        updateScore("Game Over!");
    }

    private void updateScore(String message) {
        statusMessage = message;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                handlerScore.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    public void onClickBtnCyan(View v) {
        buttonLogic(btnCyan);
    }

    public void onClickBtnBlue (View v) {
        buttonLogic(btnBlue);
    }

    public void onClickBtnRed (View v) {
        buttonLogic(btnRed);
    }

    public void onClickBtnGreen (View v) {
        buttonLogic(btnGreen);
    }

    public void onClickBtnGray(View v) {
        buttonLogic(btnGray);
    }

    private synchronized void buttonLogic(ImageButton imgButton) {
        if ( listSequenceGame.isEmpty()) {
            return; // nothing to do
        }
        blinkWithSoundImgButton(imgButton);
        checkUserClick(imgButton);
    }

    private void checkUserClick(ImageButton button) {

        imageButtonClicked = button;
        userTryingPosition++;

        if ( isInCorrect() ) {
            endGame();
            return;
        }
        isGameRunning = true;
        if ( isTheLastOfSequence() ) {
            updateActualScore();
            updateScore("Score:" + actualScore);
            postExecuteAddStepAndPlayTheSequence(); //detach from thread
        }
        else {
            isGameRunning = false;
        }
    }

    private void postExecuteAddStepAndPlayTheSequence() {

        Runnable execute = new Runnable() {
            @Override
            public void run(){
                addStepAndPlayTheSequence();
            }
        };
        new Handler().postDelayed(execute, 100);
    }

    private boolean isInCorrect() {

        try {
            if (listSequenceGame.get(userTryingPosition) == imageButtonClicked) {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println( e.getMessage()) ;
        }
        return true;
    }

    private boolean isTheLastOfSequence() {
        return  userTryingPosition == listSequenceGame.size()-1 ;
    }

    private void addStepAndPlayTheSequence() {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    for ( ImageButton imgButton : listSequenceGame) {
                        blinkWithSoundImgButton(imgButton);
                        GameUtils.delayMilliSeconds(400);
                    }
                }
                catch (Exception e) {
                    System.out.print(e.getMessage());
                }
                isGameRunning = false;
            }
        });
        addNewRandonToSequence();
        userTryingPosition = -1;
        GameUtils.delayMilliSeconds(800);
        thread.start();
    }

    private void addNewRandonToSequence() {

        int nextSequence = random.nextInt(4);
        nextSequence++; // adjust 0..4 to 1..5

        if ( nextSequence == 1) {
            listSequenceGame.add(btnCyan);
        }
        else if ( nextSequence == 2) {
            listSequenceGame.add(btnBlue);
        }
        else if ( nextSequence == 3) {
            listSequenceGame.add(btnRed);
        }
        else if ( nextSequence == 4) {
            listSequenceGame.add(btnGray);
        }
        else if ( nextSequence == 5) {
            listSequenceGame.add(btnGreen);
        }
        else {
            throw new RuntimeException("Error when generate the next sequence = " + nextSequence);
        }
    }

    private void blinkWithSoundImgButton(ImageButton imgButton) {
        playSound(imgButton);
        imageButtonClicked = imgButton;
        handlerImgButton.sendEmptyMessage(0);
    }

    private void playSound(ImageButton imgButton) {

        if ( btnCyan == imgButton) {
            buttonSound = MediaPlayer.create(this, lao.simplememorytrainer.R.raw.sounda3);
        }
        else if ( btnBlue == imgButton) {
            buttonSound = MediaPlayer.create(this, lao.simplememorytrainer.R.raw.soundb4);
        }
        else if ( btnRed == imgButton) {
            buttonSound = MediaPlayer.create(this, lao.simplememorytrainer.R.raw.soundd3);
        }
        else if ( btnGray == imgButton) {
            buttonSound = MediaPlayer.create(this, lao.simplememorytrainer.R.raw.sounde4);
        }
        else if ( btnGreen == imgButton) {
            buttonSound = MediaPlayer.create(this, lao.simplememorytrainer.R.raw.soundg3);
        }

        buttonSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // free memory
            };
        });
        buttonSound.start();
    }

    // Others screens ...

    public void showDialogWhenIsNewHighScore() {

        if  ( !myDBHandler.isNewHighScore( actualScore ) ) {
            return;
        }

        AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        View myView = getLayoutInflater().inflate(R.layout.input_new_highscore, null);
        txtName = (EditText) myView.findViewById(R.id.idTxtDlgName);

        myDialog.setView(myView);
        alertDialog = myDialog.create();
        alertDialog.show();
    }

    private void updateActualScore() {
        actualScore = listSequenceGame.size();
    }

    public void onClickBtnDialogOk(View v) {
        String name = txtName.getText().toString();

        if ( name != null && name.trim().length() > 0 ) {
            myDBHandler.addHighScore(name, actualScore);
        }

        alertDialog.cancel();
        alertDialog = null;
    }

    public void onClickBtnDialogClose(View v) {
        alertDialog.cancel();
        alertDialog = null;
    }

    public void onClickBtnShowHighScores(View v) {

        Intent intent = new Intent(MainActivity.this, TopHighScores.class);
        startActivity(intent);
    }

    public void onClickBtnShowGitInfo(View v) {

        Intent intent = new Intent(MainActivity.this, GitSourcesInfo.class);
        startActivity(intent);
    }
}
