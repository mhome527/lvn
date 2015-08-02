package teach.vietnam.asia.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import teach.vietnam.asia.api.RequestManager;
import teach.vietnam.asia.db.DbController;
import teach.vietnam.asia.db.SqlLiteCopyDbHelper;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.ULog;

public class MyApplication extends Application {
	public static MyApplication mInstance;
	private Tracker tracker;
	private RequestQueue mRequestQueue;
	public DaoMaster daoMaster;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// startService();

		//copy data
//		AssetDatabaseOpenHelper dbHelper = new AssetDatabaseOpenHelper(this);
//		dbHelper.openDatabase();

//		SqlLiteCopyDbHelper dbHelper = new SqlLiteCopyDbHelper(this);
//		dbHelper.openDataBase();
		//delete database
		Prefs pref = new Prefs(this.getApplicationContext());
		String strDB = pref.getStringValue("", Constant.KEY_UPDATE);
		if(strDB.equals("") || !strDB.equals(Constant.KEY_UPDATE) ) {
			ULog.i("a", "Delete database....");
			this.deleteDatabase(Constant.DB_NAME_V2);
		}

		SqlLiteCopyDbHelper dbHelper = new SqlLiteCopyDbHelper(this);
		if(dbHelper.openDataBase()) {
			pref.putStringValue(Constant.KEY_UPDATE, Constant.KEY_UPDATE);
		}
		else
			ULog.e("a", "Import Error!!!!!");

		////
		
		daoMaster = new DaoMaster(DbController.init(MyApplication.this, true));
		if (BaseActivity.pref == null)
			BaseActivity.pref = new Prefs(MyApplication.this);
		RequestManager.init(MyApplication.this);

	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

//	public Tracker getTrackerApp() {
//		if (tracker == null)
//			tracker = GoogleAnalytics.getInstance(this).getTracker(Constant.KEY_ANALYSIS);
//		return tracker;
//	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

    public Tracker getTrackerApp() {
        if (tracker == null)
            tracker = GoogleAnalytics.getInstance(this).getTracker(Constant.KEY_ANALYSIS);
        return tracker;
    }

	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.
	 */
//	private void createImageCache() {
//		ImageCacheManager.getInstance().init(this, this.getPackageCodePath(), DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT,
//				DISK_IMAGECACHE_QUALITY, ImageCacheManager.CacheType.MEMORY);
//	}

	

	// public <T> void addToRequestQueue(Request<T> req, String tag) {
	// // set the default tag if tag is empty
	// req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
	// getRequestQueue().add(req);
	// }

	/**
	 * Start service using AlarmManager
	 */
	// private void startService() {
	// ULog.i(this.getClass(), "startService...");
	// Calendar cal = Calendar.getInstance();
	// cal.add(Calendar.SECOND, 10);
	// Intent intent = new Intent(this, DownloadService.class);
	//
	// PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
	// AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	//
	// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pIntent); // 5 minute
	// startService(new Intent(getBaseContext(), DownloadService.class));
	//
	// }
}
