package com.yenimobile.myfavoritetoysudacity.hydratationAppServices;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.myfavoritetoysudacity.R;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.hydatationUtilities.NotificationUtils;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.hydatationUtilities.PreferenceUtilities;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.hydatationUtilities.ReminderUtilities;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.sync.ReminderTasks;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.sync.WaterReminderIntentService;

import java.util.prefs.PreferenceChangeListener;

public class HydratationMainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;
    private Button mTestNotifs;

    private Toast mToast;


    //intent filter created not in the manifest but dynamically
    IntentFilter mChargingIntentFilter;

    BroadcastReceiver mChargingBroadRecei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydratation_main);

        /** Get the views **/
        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        mChargingCountDisplay = (TextView) findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = (ImageView) findViewById(R.id.iv_power_increment);
        mTestNotifs = findViewById(R.id.test_notifications_button);

        mChargingIntentFilter = new IntentFilter();
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mChargingBroadRecei = new ChargingBroadcastReceiver();

        /** Set the original values in the UI **/
        updateWaterCount();
        updateChargingReminderCount();

        /** the Schedueld job **/
        ReminderUtilities.scheduleChargingReminder(this);

        /** Setup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = HydratationMainActivity.this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            boolean isCharging = batteryManager.isCharging();

            showCharging(isCharging);
        }else {
            IntentFilter stickyIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatusStickyIntent =
                    context.registerReceiver(null, stickyIntentFilter);
            int status = batteryStatusStickyIntent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

            showCharging(isCharging);
        }


        //registerReceiver(mChargingBroadRecei, mChargingIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mChargingBroadRecei);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** Cleanup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }


    /**
     * Updates the TextView to display the new water count from SharedPreferences
     */
    private void updateWaterCount() {
        int waterCount = PreferenceUtilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }

    /**
     * Updates the TextView to display the new charging reminder count from SharedPreferences
     */
    private void updateChargingReminderCount() {
        int chargingReminders = PreferenceUtilities.getChargingReminderCount(this);
        String formattedChargingReminders = getResources().getQuantityString(
                R.plurals.charge_notification_count, chargingReminders, chargingReminders);
        mChargingCountDisplay.setText(formattedChargingReminders);

    }


    private void showCharging(boolean isCharging){
        if (isCharging){
            mChargingImageView.setImageResource(R.drawable.ic_power_pink_80px);
        }else {
            mChargingImageView.setImageResource(R.drawable.ic_power_gery_80px);
        }
    }

    /**
     * Adds one to the water count and shows a toast
     */
    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT);
        mToast.show();


        Intent incrementWaterCountIntent = new Intent(this, WaterReminderIntentService.class);

        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        startService(incrementWaterCountIntent);

    }


    /**
     * This is a listener that will update the UI when the water count or charging reminder counts
     * change
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(key)) {
            updateChargingReminderCount();
        }
    }


    public void testNotifications(View view){
        NotificationUtils.remindUserBecauseCharging(this);
    }



    //as we only gonna use this specific broadcast recevier in this mainActivity only
    //we then are going to make it an inner class
    private class ChargingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String theIntentAction = intent.getAction();

            boolean isCharging = (theIntentAction.equals(Intent.ACTION_POWER_CONNECTED));
            showCharging(isCharging);
        }
    }






}
