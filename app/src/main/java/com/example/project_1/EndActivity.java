package com.example.project_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class EndActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        TextView resultTextView = findViewById(R.id.resultTextView);
        TextView timerView = findViewById(R.id.TimeView);
        Button playAgainButton = findViewById(R.id.playAgainButton);

        // Get the result from the intent
        boolean isWin = getIntent().getBooleanExtra("isWin", false);
        int time = getIntent().getIntExtra("Time", 0);
        resultTextView.setText(isWin ? "WIN" : "LOSE");
        timerView.setText("It took you " + time + " seconds");

        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

