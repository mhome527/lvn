package teach.vietnam.asia.api;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.utils.ULog;

public abstract class BaseApi<T> implements Response.Listener<T> {
    private static String TAG = "BaseApi";
    private static MyApplication mInstance;

    public abstract String getUrl();

    public abstract int getMethod();

    public abstract Map<String, String> getParamsAPI();

    public GsonRequest<T> gsonRequest;

    private Class<T> cls;

    public abstract void onErrorResponse(VolleyError error, String msg);


    public BaseApi(Class<T> cls) {
        this.cls = cls;
        callAPI(0, 0);
    }

    public BaseApi(Class<T> cls, int timeout, int retryTimes) {
        this.cls = cls;
        callAPI(timeout, retryTimes);
    }

    public void callAPI(int timeout, int retryTimes) {
        if (null == mInstance)
            mInstance = MyApplication.getInstance();
//        RequestQueue mRequestQueue = mInstance.getRequestQueue();
//        RequestQueue mRequestQueue = RequestManager.getRequestQueue();
        gsonRequest = new GsonRequest<T>(getMethod(), getUrlBase(), cls, this, new VolleyErrorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getParamsAPI();
            }
        };
        if (timeout > 0)
            gsonRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, retryTimes, 0));
//        mRequestQueue.add(gsonRequest);
        RequestManager.addToRequestQueue(gsonRequest);
    }

    private class VolleyErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            String msg = "";


            BaseApi.this.onErrorResponse(volleyError, msg);
        }
    }

    private String getUrlBase() {
        String url;
        Map<String, String> map = getParamsAPI();
        //log
        if (map != null) {
            for (String key : map.keySet()) {
                ULog.i(TAG, "param= " + key + ":" + map.get(key));
            }
        }
        ///

        if (getMethod() == Request.Method.GET) {
            Uri.Builder b = Uri.parse(getUrl()).buildUpon();
            if (map != null) {
                for (String key : map.keySet()) {
                    b.appendQueryParameter(key, map.get(key));
                }
            }
            ULog.i("BaseApi-htd", "get url:" + b.build().toString());

            return b.build().toString();
        } else {
            url = getUrl();
            ULog.i("BaseApi-htd", " url:" + url);

            return url;
        }
    }
}
