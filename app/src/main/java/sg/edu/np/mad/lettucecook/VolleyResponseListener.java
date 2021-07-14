package sg.edu.np.mad.lettucecook;

import org.json.JSONException;
import org.json.JSONObject;

// interface to implement when sending requests
public interface VolleyResponseListener {
    void onError(String message);
    void onResponse(JSONObject response) throws JSONException;
}
