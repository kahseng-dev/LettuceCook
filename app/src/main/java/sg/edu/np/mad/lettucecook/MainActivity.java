package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    ArrayList<ApiMeal> meals;
    ApiMealService apiMealService = new ApiMealService(MainActivity.this);
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();

    int browseType;
    Spinner browseTypeSpinner;
    Spinner browseTypeChoiceSpinner;
    Button browseButton;
    RecyclerView browseRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseTypeSpinner = findViewById(R.id.main_browse_type_spinner);
        browseTypeChoiceSpinner = findViewById(R.id.main_browse_type_choice_spinner);
        browseButton = findViewById(R.id.main_browse_button);
        browseRV = findViewById(R.id.main_browse_rv);

        fillSpinner(browseTypeSpinner, getResources().getStringArray(R.array.browse_types));

        browseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                browseType = i;
                String query = i == 0 ? "list.php?c=list" : "list.php?a=list";
                apiMealService.getMeals(query, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _filters = response.getJSONArray("meals");
                            String[] filters = apiMealJson.parseFilterArray(_filters);
                            fillSpinner(browseTypeChoiceSpinner, filters);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = browseTypeChoiceSpinner.getSelectedItem().toString();
                String query = "filter.php?" + (browseType == 0 ? "c=" : "a=") + filter;
                apiMealService.getMeals(query, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _meals = response.getJSONArray("meals");
                            meals = apiMealJson.mergeIntoJSONArray(_meals);
                            Log.v("Meal", String.valueOf(meals.get(0)));

                            ApiMealAdapter mAdapter = new ApiMealAdapter(meals, MainActivity.this);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

                            browseRV.setLayoutManager(mLayoutManager);
                            browseRV.setItemAnimator(new DefaultItemAnimator());
                            browseRV.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void fillSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}