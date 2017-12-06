package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by caucse on 2017-11-29.
 */

public class IntroActivity extends Activity {

    public Button controller_btn = null;
    public Button controllee_btn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        controllee_btn = (Button)findViewById(R.id.controlleeButton);
        controller_btn = (Button)findViewById(R.id.controllerButton);
        // Button의 객체를 다 가져왔다.

        controllee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button","Go to Controllee");

                Intent i = new Intent(IntroActivity.this,ControlleeActivity.class);
                startActivity(i);
            }
        });

        controller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button","Go to Controller");

                Intent i = new Intent(IntroActivity.this,ControllerActivity.class);
                startActivity(i);
            }
        });

    }
}
