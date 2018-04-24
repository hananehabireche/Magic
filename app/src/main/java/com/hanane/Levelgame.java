package com.hanane;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Levelgame extends Activity {
    private static final String TAG = "LevelgameActivity";

    private EditText editLevel;
    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelgame);
        initialize();
        checkFieldsForEmptyValues();

    }
    private void initialize(){
        Log.i(TAG, "initialize");

        editLevel = (EditText)findViewById(R.id.Leveledittext);
        btnPlay = (Button)findViewById(R.id.play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGame();
            }
        });
        editLevel.addTextChangedListener(myEye);
    }

    void  checkFieldsForEmptyValues(){
        Log.i(TAG, "checkFieldsForEmptyValues");

        if ("".equals(editLevel.getText().toString())){
            btnPlay.setEnabled(false);
        }else{
            btnPlay.setEnabled(true);
        }
    }

    private void playGame(){
        Log.i(TAG, "playGame");

        if(Integer.parseInt(editLevel.getText().toString())!= 0 && Integer.parseInt(editLevel.getText().toString())< 4){
        Intent playintent = new Intent(getApplicationContext(), Magic.class);
        playintent.putExtra(Magic.LEVEL, Integer.parseInt(editLevel.getText().toString()));
        startActivity(playintent);}else {
            Toast.makeText(this, R.string.choose_value, Toast.LENGTH_SHORT).show();
        }

    }

    private TextWatcher myEye  = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, "beforeTextChanged "+s.toString()+" "+start+" "+count+" "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "onTextChanged "+s.toString()+" "+start+" "+before+" "+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i(TAG, "afterTextChanged");
            checkFieldsForEmptyValues();
        }
    };

}
