package com.example.prm391x_asm1_anhnh07178funixeduvn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnMessage;
    Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMessage = findViewById(R.id.btnMessage);
        btnCall = findViewById(R.id.btnCall);

        btnMessage.setOnClickListener(view -> {
            Intent intent = new Intent(this, PhoneActivity.class);
            startActivity(intent);
        });

        btnCall.setOnClickListener(view -> {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        });
    }
}