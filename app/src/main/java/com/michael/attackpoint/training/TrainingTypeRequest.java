package com.michael.attackpoint.training;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 * Created by michael on 2/26/16.
 */
public class TrainingTypeRequest extends Request<Map<String, Integer>> {
    public TrainingTypeRequest(String url, Response.ErrorListener listener) {
        super(url, listener);
    }

    public TrainingTypeRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<Map<String, Integer>> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }

    @Override
    protected void deliverResponse(Map<String, Integer> stringIntegerMap) {

    }
}
