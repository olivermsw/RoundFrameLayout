package pers.olimsw.roundframelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pers.olimsw.rfllibrary.RoundFrameLayout;

public class MainActivity extends AppCompatActivity {

    private RoundFrameLayout rfy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rfy = (RoundFrameLayout) findViewById(R.id.rfy);
    }
}
