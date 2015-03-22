package teach.vietnam.asia.api;

import com.android.volley.Request;

import teach.vietnam.asia.utils.Constant;

public abstract class InfoAppAPI<T> extends BaseApi<T> {

	public InfoAppAPI(Class<T> cls) {
		super(cls);
	}
	
	@Override
	public String getUrl() {
		return Constant.API_MARKER;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
//		return Request.Method.POST;
	}


}
