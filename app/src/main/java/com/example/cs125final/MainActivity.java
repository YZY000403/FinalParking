package com.example.cs125final;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Button search = findViewById(R.id.Result);
       // search.setOnClickListener(unused
                //-> nothing());
       // startActivity(new Intent(this, ReplyActivity.class));
        //finish();
        TextView it = findViewById(R.id.textView);
        it.setText();
    }
    public void nothing() {
        int nothing1 = 1;
    }
}
