package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.db.DBDataSound;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class SplashActivity extends BaseActivity implements DBDataSound.ICreateTable {

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

            new DBDataSound(this, this).execute();

            Typeface tf = Typeface.createFromAsset(getAssets(), "aachenb.ttf");
            tvTitle.setTypeface(tf, Typeface.BOLD);
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
