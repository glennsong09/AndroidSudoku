package com.muninn.sudoku;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureEasyBoardButton();
        configureMediumBoardButton();
        configureHardBoardButton();
    }

    public void configureEasyBoardButton() {
        Button buttonEasy = findViewById(R.id.easyButton);
        buttonEasy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EasyBoard.class));
            } });
    }

    public void configureMediumBoardButton(){
        Button buttonMedium = findViewById(R.id.mediumButton);
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MediumBoard.class));
            } });
    }
    public void configureHardBoardButton(){
        Button buttonHard = findViewById(R.id.hardButton);
        buttonHard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HardBoard.class));
            } });
    }
}
