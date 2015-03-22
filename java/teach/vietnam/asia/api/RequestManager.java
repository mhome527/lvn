package teach.vietnam.asia.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Manager for the queue
 *
 * @author Trey Robinson
 */
public class RequestManager {

    private static final String DEFAULT_HTTP_TAG = "volley_req";
    /**
     * the queue :-)
     */
    private static RequestQueue mRequestQueue;

    /**
     * the queue for img request
     */
    private static RequestQueue mImgRequestQueue;

    /**
     * Nothing to see here.
     */
    private RequestManager() {
        // no instances
    }

    /**
     * @param context application context
     */
    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImgRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * @return instance of the queue
     * @throws java.lang.IllegalStateExceptioneption if init has not yet been called
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    public static RequestQueue getImgRequestQueue() {
        if (mImgRequestQueue != null) {
            return mImgRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }


    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? DEFAULT_HTTP_TAG : tag);
        getRequestQueue().add(req);
    }

    public static <T> void addToRequestQueue(Request<T> req) {
        req.setTag(DEFAULT_HTTP_TAG);
        getRequestQueue().add(req);
    }
}
