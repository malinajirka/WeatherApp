package cz.malinajiri.showcase.weatherapp.client;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;

public class GsonRequest<T> extends Request<T> {

    private final Gson mGson;
    private final Class<T> mClass;
    private final Map<String, String> mHeaders;
    private final Response.Listener<T> mResponseListener;

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);


        this.mGson = WeatherAppApplication.getInstance().getGson();
        this.mClass = clazz;
        this.mHeaders = headers;
        this.mResponseListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        mResponseListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
