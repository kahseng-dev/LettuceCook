package sg.edu.np.mad.lettucecook.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import sg.edu.np.mad.lettucecook.activities.MainActivity;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "LettuceCook_channel";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get id & message from intent.
        int notificationId = intent.getIntExtra("notificationId", 0);
        int requestId = intent.getIntExtra("requestId", 0);
        String message = intent.getStringExtra("message");

        PendingIntent contentIntent;

        if (intent.hasExtra("mealId")) {
            // Call RecipeDetails when notification is tapped.
            Intent recipeDetailsIntent = new Intent(context, RecipeDetailsActivity.class);
            recipeDetailsIntent.putExtra("mealId", intent.getStringExtra("mealId"));
            recipeDetailsIntent.putExtra("notification", true);
            contentIntent = PendingIntent.getActivity(context, requestId, recipeDetailsIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        else {
            // Call MainActivity when notification is tapped.
            Intent mainIntent = new Intent(context, MainActivity.class);
            contentIntent = PendingIntent.getActivity(context, requestId, mainIntent, 0);
        }

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            CharSequence channelName = "LettuceCookNotification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Prepare notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LettuceCook Reminder")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Notify
        notificationManager.notify(notificationId, builder.build());
    }
}
