package com.example.david.se7enqtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TrainingActivity extends Activity {

    private static final String TAG = "Array = Niz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Button array = (Button)findViewById(R.id.arrayButton);

        array.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Uspesno kliknuto!");
            }
        });
    }
}
