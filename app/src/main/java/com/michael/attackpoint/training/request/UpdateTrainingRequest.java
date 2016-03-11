package com.michael.attackpoint.training.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by michael on 3/11/16.
 */
public class UpdateTrainingRequest extends AddTrainingRequest {
    private static final String URL = "http://www.attackpoint.org/changetraining.jsp";
    private static final String FIELD_ID = "sessionid";

    public UpdateTrainingRequest(LogInfo li, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        super(li, URL, listener, errorListener);
    }

    @Override
    public Map<String, String> createParams(LogInfo li) {
        Map<String, String> params = super.createParams(li);

        int id = li.getID();
        if (id > 0)
            params.put(FIELD_ID, Integer.valueOf(id).toString());

        return params;
    }
}
