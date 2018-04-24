package com.hanane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;


public class Homepage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void game(View v){
            startActivity(new Intent(getApplicationContext(), Levelgame.class));
    }


    public void aboutGame(View v){
        Uri uri = Uri.parse("https://en.wikipedia.org/wiki/Magic_square");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }




}
