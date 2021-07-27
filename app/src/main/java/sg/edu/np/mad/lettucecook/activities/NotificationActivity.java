package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.utils.AlarmReceiver;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private int notificationId = 1;
    private EditText alertMessage;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
//        Log.v("Meall", DataSingleton.getInstance().getMeal().getIdMeal());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        findViewById(R.id.setButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);

        if (getIntent().hasExtra("mealId")) {
            ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();
            alertMessage = findViewById(R.id.alertMessage);

            Bundle extras = getIntent().getExtras();
            String mealId = extras.getString("mealId");

            ApiService apiService = new ApiService(this);
            apiService.get(ApiURL.MealDB, "lookup.php?i=" + mealId, new VolleyResponseListener() {
                @Override
                public void onError(String message) { }

                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray _meals = response.getJSONArray("meals");
                        ApiMeal meal = apiJson.mergeIntoJSONArray(_meals).get(0);
                        alertMessage.setText("Time to make " + meal.getStrMeal() + "!");
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        alertMessage = findViewById(R.id.alertMessage);
        timePicker = findViewById(R.id.timePicker);

        // Set notificationId and Message
        Intent intent = new Intent(NotificationActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", alertMessage.getText().toString());

        if (getIntent().hasExtra("mealId")) {
            Bundle extras = getIntent().getExtras();
            String mealId = extras.getString("mealId");
            intent.putExtra("mealId", mealId);
        }

        // PendingIntent
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
            NotificationActivity.this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        // AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (v.getId()) {
            case R.id.setButton:
                if (alertMessage.getText().toString().isEmpty()) {
                    alertMessage.setError("Please write your message!");
                }

                else {
                    // set alarm
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();

                    // Create time
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, hour);
                    startTime.set(Calendar.MINUTE, minute);
                    startTime.set(Calendar.SECOND, 0);

                    long alarmStartTime = startTime.getTimeInMillis();

                    // set alarm
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.cancelButton:
                // cancel alarm
                alarmManager.cancel(alarmIntent);
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Class activity = MainActivity.class;

                int layoutId = getIntent().getIntExtra("layoutId", 0);
                if (layoutId == R.layout.activity_recipe_details) {
                    activity = RecipeDetailsActivity.class;
                } else if (layoutId == R.layout.activity_browse) {
                    activity = BrowseActivity.class;
                }

                startActivity(new Intent(getApplicationContext(), activity));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}