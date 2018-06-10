package com.yenimobile.myfavoritetoysudacity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.myfavoritetoysudacity.BoardingAppFiles.BoardingpassMainActivity;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.HydratationMainActivity;
import com.yenimobile.myfavoritetoysudacity.polishAppFiles.PolishappMain;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonToyApp,
            mButtonSunshine,
            mButtonSomeRecyclerView, mButtonToyImplicit, mLifeCycleButton,
            mVisualizerButton, mWaitListButton, mQuizContentProviderButton,
            mBuildingContentProviderButton, mHydradationButton, mBoardingPassButton,
            mPolishAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSunshine = findViewById(R.id.button_sunshine);
        mButtonToyApp = findViewById(R.id.button_toy_app);
        mButtonSomeRecyclerView = findViewById(R.id.button_somerecyclerview);
        mButtonToyImplicit = findViewById(R.id.button_toyimplicit);
        mLifeCycleButton = findViewById(R.id.button_lifecycle);
        mVisualizerButton = findViewById(R.id.button_visualizer);
        mWaitListButton = findViewById(R.id.button_waitList);
        mQuizContentProviderButton = findViewById(R.id.button_quizprovider);
        mBuildingContentProviderButton = findViewById(R.id.button_buildingcontentprovider);
        mHydradationButton = findViewById(R.id.button_hydratationApp);
        mBoardingPassButton = findViewById(R.id.button_boardingpassApp);
        mPolishAppButton = findViewById(R.id.button_PolishApp);

        mButtonSomeRecyclerView.setOnClickListener(this);
        mButtonToyApp.setOnClickListener(this);
        mButtonSunshine.setOnClickListener(this);
        mButtonToyImplicit.setOnClickListener(this);
        mLifeCycleButton.setOnClickListener(this);
        mVisualizerButton.setOnClickListener(this);
        mWaitListButton.setOnClickListener(this);
        mQuizContentProviderButton.setOnClickListener(this);
        mBuildingContentProviderButton.setOnClickListener(this);
        mHydradationButton.setOnClickListener(this);
        mBoardingPassButton.setOnClickListener(this);
        mPolishAppButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == mButtonSunshine) {
            //Toast.makeText(this, "button sunshine", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SunshineActivity.class));

        }else if (view == mButtonToyApp) {
            //Toast.makeText(this, "button toy app", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ToyappActivity.class));
        }else if (view == mButtonSomeRecyclerView) {
            startActivity(new Intent(MainActivity.this, SomerecyclerviewActivity.class));
        }else if (view == mButtonToyImplicit) {
            startActivity(new Intent(MainActivity.this, ToyimplicitActivity.class));
        }else if (view == mLifeCycleButton) {
            startActivity(new Intent(MainActivity.this, LifecycleActivity.class));
        }else if (view == mVisualizerButton){
            startActivity(new Intent(MainActivity.this, VisualiserActivity.class));
        }else if (view == mWaitListButton) {
            startActivity(new Intent(MainActivity.this, WaitlistActivity.class));
        }else if(view == mQuizContentProviderButton) {
            startActivity(new Intent(MainActivity.this, QuizcontentproviderActivity.class));
        }else if(view == mBuildingContentProviderButton){
            startActivity(new Intent(MainActivity.this, BuildingcontentproviderActivity.class));
        }else if (view == mHydradationButton){
            startActivity(new Intent(MainActivity.this, HydratationMainActivity.class));
        }else if(view == mBoardingPassButton) {
            startActivity(new Intent(MainActivity.this, BoardingpassMainActivity.class));
        }else if (view == mPolishAppButton){
            startActivity(new Intent(MainActivity.this, PolishappMain.class));
        }
    }
}
