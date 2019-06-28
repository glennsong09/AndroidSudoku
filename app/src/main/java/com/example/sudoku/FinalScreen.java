package com.example.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        configureRetryButton();
    }

    public void configureRetryButton() {
        Button buttonMenu = findViewById(R.id.boardMainMenuButton);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(FinalScreen.this, MainActivity.class));
            } });
    }
}
