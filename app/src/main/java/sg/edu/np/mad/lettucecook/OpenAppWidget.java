package sg.edu.np.mad.lettucecook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import sg.edu.np.mad.lettucecook.activities.MainActivity;

public class OpenAppWidget extends AppWidgetProvider {

    private String mealId;

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            // When open widget, go to main activity
            Intent intent = new Intent(context, MainActivity.class);
            //intent.putExtra("mealId", mealId);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Set RemoteViews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.open_app_widget);
            views.setOnClickPendingIntent(R.id.open_app_widget_browse_recipe_button, pendingIntent);

            // Update Widget with views according to widget Id
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}