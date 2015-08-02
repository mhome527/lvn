package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.db.DBDataSound;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class SplashActivity extends BaseActivity implements DBDataSound.ICreateTable {

    private final String TAG = "SplashActivity";
    public DaoMaster daoMaster;
    public ProgressDialog progressDialog;

    @Override
    protected int getViewLayoutId() {
        return R.layout.splash_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            ULog.i(this, "======== Product value:" + Constant.isPro);
            TextView tvTitle = getViewChild(R.id.tvTitle);

//            new DBDataSound(this, this).execute();



            Typeface tf = Typeface.createFromAsset(getAssets(), "aachenb.ttf");
            tvTitle.setTypeface(tf, Typeface.BOLD);


            new Handler().postDelayed(new Runnable() {
                public void run() {
//                    Prefs pref = new Prefs(SplashActivity.this.getApplicationContext());
//                    String strDB = pref.getStringValue("", Constant.KEY_UPDATE);
//                    if(strDB.equals("") || !strDB.equals(Constant.KEY_UPDATE) ) {
//                        ULog.i(TAG, "Delete database....");
//                        SplashActivity.this.deleteDatabase(Constant.DB_NAME_V2);
//                    }
//
//                    SqlLiteCopyDbHelper dbHelper = new SqlLiteCopyDbHelper(SplashActivity.this);
//		            if(dbHelper.openDataBase()) {
//                        pref.putStringValue(Constant.KEY_UPDATE, Constant.KEY_UPDATE);
                        SplashActivity.this.startActivity2(MainActivity.class);
//                    }
//                    else
//                        ULog.e(TAG, "Import Error!!!!!");


                    SplashActivity.this.finish();
                }
            }, 1500);

        } catch (Exception e) {
            ULog.e(SplashActivity.class, "initView Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void reloadData() {

    }

    @Override
    public void iFinishCreate() {
        ULog.i(this, "======== load map data finish");
        SplashActivity.this.startActivity2(MainActivity.class);
        SplashActivity.this.finish();
    }

}
