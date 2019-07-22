package com.lake.colorpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.lake.colorselecter.CircleSelectFrame;

public class MainActivity extends AppCompatActivity {

    private EditText colorEt;
    private CircleSelectFrame circleSelectFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorEt = findViewById(R.id.color_et);
        circleSelectFrame = findViewById(R.id.color_select_frame);


        circleSelectFrame.setOnColorChangeListener(new CircleSelectFrame.OnColorChangeListener() {
            @Override
            public void OnColorChanged(String rgb) {
                colorEt.setText(rgb);
            }
        });

        colorEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("lake", "afterTextChanged: "+s.toString());
                 if(isColorValue(s.toString())){
                     Log.e("lake", "afterTextChanged: 是颜色值");
                     circleSelectFrame.setColor(s.toString());
                 }
            }
        });

    }

    private boolean isColorValue(String text) {
        String regex = "^[0-9a-fA-F]{6}$";
        return text.matches(regex);
    }
}

