package com.example.firstproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EventHandling extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_handling);

        Button btnHello = findViewById(R.id.btnHello);

//        btnHello.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(EventHandling.this, "You clickec me", Toast.LENGTH_LONG).show();
//            }
//        });

        btnHello.setOnClickListener(v->
                Toast.makeText(EventHandling.this,
                        "You Clicked me",
                        Toast.LENGTH_LONG
                        ).show());

    }
}
