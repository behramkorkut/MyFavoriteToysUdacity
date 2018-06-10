package com.yenimobile.myfavoritetoysudacity.polishAppFiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yenimobile.myfavoritetoysudacity.MainActivity;
import com.yenimobile.myfavoritetoysudacity.R;

public class PolishappMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polishapp_main);

        // Four text views for four activities
        TextView colorAndFont = (TextView) findViewById(R.id.colorAndFont);
        TextView style = (TextView) findViewById(R.id.style);
        TextView responsiveLayouts = (TextView) findViewById(R.id.responsiveLayouts);
        TextView touchSelector = (TextView) findViewById(R.id.touchSelector);


        assert colorAndFont != null;
        colorAndFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(PolishappMain.this, ColorFontActivity.class);
                startActivity(numbersIntent);
            }
        });

        assert style != null;
        style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent familyIntent = new Intent(PolishappMain.this, StyleActivity.class);
                startActivity(familyIntent);
            }
        });

        assert responsiveLayouts != null;
        responsiveLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent familyIntent = new Intent(PolishappMain.this, ResponsiveLayoutActivity.class);
                startActivity(familyIntent);
            }
        });

        assert touchSelector != null;
        touchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent colorsIntent = new Intent(PolishappMain.this, SelectorsActivity.class);
                startActivity(colorsIntent);
            }
        });
    }
}
