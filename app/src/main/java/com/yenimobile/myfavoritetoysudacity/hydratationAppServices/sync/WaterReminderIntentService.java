package com.yenimobile.myfavoritetoysudacity.hydratationAppServices.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WaterReminderIntentService extends IntentService {



    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();

        ReminderTasks.executeTask(this, action);
    }
}
