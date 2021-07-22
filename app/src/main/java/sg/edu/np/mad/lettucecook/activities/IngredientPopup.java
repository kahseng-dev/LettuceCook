package sg.edu.np.mad.lettucecook.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import sg.edu.np.mad.lettucecook.R;

public class IngredientPopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_ingredient);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Full width, 80% height
        getWindow().setLayout(dm.widthPixels, (int)(dm.heightPixels * 0.8));

        // Transparent background - allows corner radius to show properly
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();

        // Attach to bottom
        params.gravity = Gravity.BOTTOM;

        // Dim background
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.3f;

        getWindow().setAttributes(params);
    }
}