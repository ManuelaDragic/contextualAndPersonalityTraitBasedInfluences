package com.uniulm.social_media_interventions;

import org.json.JSONObject;

public interface VolleyCallBack {
    void onSuccess(JSONObject response);
    void onFailure(JSONObject response);
}
