package com.yenimobile.myfavoritetoysudacity.hydratationAppServices.hydatationUtilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.yenimobile.myfavoritetoysudacity.R;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.HydratationMainActivity;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.sync.ReminderTasks;
import com.yenimobile.myfavoritetoysudacity.hydratationAppServices.sync.WaterReminderIntentService;

public class NotificationUtils {

    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 2331;
    private static final int INCREMENT_WATER_COUNT_PENDING_INTENT_ID = 2339;
    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";


    public static void clearAllNotifications(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public static void remindUserBecauseCharging(Context context){

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //android O notificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.backgroundBlue))
                .setSmallIcon(R.drawable.ic_local_drink_black_24px)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreReminderAction(context))
                .addAction(drinkWaterAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }


    private static NotificationCompat.Action ignoreReminderAction(Context context){

        Intent ignorereminderIntent = new Intent(context, WaterReminderIntentService.class);

        ignorereminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignorereminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignorereminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Action ignoreReminderCompatAction = new NotificationCompat.Action(
                R.drawable.ic_cancel_black_24px,
                "No thanks",
                ignorereminderPendingIntent
        );
        return ignoreReminderCompatAction;
    }


    private static NotificationCompat.Action drinkWaterAction(Context context){
        Intent incrementWaterCountIntent = new Intent(context, WaterReminderIntentService.class);
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementWaterCountPendingIntent = PendingIntent.getService(
                context,
                INCREMENT_WATER_COUNT_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action incrementWaterCountAction = new NotificationCompat.Action(
                R.drawable.ic_local_drink_black_24px,
                "yes i've drunk some water",
                incrementWaterCountPendingIntent
        );

        return incrementWaterCountAction;
    }



    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, HydratationMainActivity.class);
        return PendingIntent.getBroadcast(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

    }

    private static Bitmap largeIcon(Context context){
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
        return largeIcon;
    }


}
