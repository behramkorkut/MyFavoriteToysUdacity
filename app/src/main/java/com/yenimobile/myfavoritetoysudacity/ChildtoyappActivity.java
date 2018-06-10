package com.yenimobile.myfavoritetoysudacity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ChildtoyappActivity extends AppCompatActivity {

    private TextView mDisplayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childtoyapp);

        mDisplayTV = findViewById(R.id.child_tv_display);

        if (getIntent() != null  && getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            mDisplayTV.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}
