package teach.vietnam.asia.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

import teach.vietnam.asia.utils.ULog;

/**
 * Wrapper for Volley requests to facilitate parsing of json responses.
 *
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {

    /**
     * Gson parser
     */
    private final Gson mGson;

    /**
     * Class type for the response
     */
    private final Class<T> mClass;

    /**
     * Callback for response delivery
     */
    private final Listener<T> mListener;

    /**
     * @param method        Request type.. Method.GET etc
     * @param url           path for the requests
     * @param objectClass   expected class type for the response. Used by gson for serialization.
     * @param listener      handler for the response
     * @param errorListener handler for errors
     */
    public GsonRequest(int method, String url, Class<T> objectClass, Listener<T> listener, ErrorListener errorListener) {

        super(method, url, errorListener);
        ULog.i("GsonRequest", "url:" + url);
        this.mClass = objectClass;
        this.mListener = listener;
        mGson = new Gson();

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ULog.i("GsonRequest", "parseNetworkResponse json:" + json);

            return Response.success(mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);

    }

}
