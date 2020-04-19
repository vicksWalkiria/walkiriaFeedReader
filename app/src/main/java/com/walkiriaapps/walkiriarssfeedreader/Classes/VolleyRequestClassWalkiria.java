package com.walkiriaapps.walkiriarssfeedreader.Classes;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.walkiriaapps.walkiriarssfeedreader.MainActivity;
import com.walkiriaapps.walkiriarssfeedreader.R;

import org.json.JSONException;

import java.util.Map;

public class VolleyRequestClassWalkiria {

    public VolleyRequestClassWalkiria(final MainActivity ctx, String url, int requestMethod, final Map<String, String> params, final ProgressBar progressBar, final int requestId) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        Log.d("WALKIRIA"," URL: "+url);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest stringRequest = new StringRequest(requestMethod, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ctx.onVolleyResponse(response, requestId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.d("WALKIRIA", "ERROR: "+error);

                if (response != null && response.data != null) {
                    ctx.displayAlertDialog(ctx.getString(R.string.error), response.statusCode + "");
                }

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
}
