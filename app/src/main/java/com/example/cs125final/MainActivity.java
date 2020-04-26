package com.example.cs125final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = findViewById(R.id.reply);
        search.setOnClickListener(unused
                -> nothing());
        startActivity(new Intent(this, ReplyActivity.class));
        finish();
    }
    public void nothing() {
        int nothing1 = 1;
    }
}
