package cz.malinajiri.showcase.weatherapp.client;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;


public class VolleyHelper {

    private static final String TAG = "VolleyHelperDefaultTAG";

    private static RequestQueue mRequestQueue;

    /**
     * @return The Volley Request queue, the queue will be created if it is
     * null.
     */
    public static RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(WeatherAppApplication.getInstance().getApplicationContext());
        }

        return mRequestQueue;
    }


    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }


    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public static <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }


    /**
     * Cancels all pending requests by the specified TAG.
     *
     * @param tag
     */
    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag == null ? TAG : tag);
        }
    }

}
