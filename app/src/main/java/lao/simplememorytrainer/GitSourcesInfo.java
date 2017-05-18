package lao.simplememorytrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GitSourcesInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_sources_info);
    }

    public void onClickButtonClose(View v) {
        finish();
    }
}
