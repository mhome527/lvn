package teach.vietnam.asia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.ULog;

public abstract class BaseActivity extends Activity implements OnClickListener {
    public static Prefs pref;
    public String lang = "";
    private String tag = BaseActivity.class.getSimpleName();
    private View viewMain;
    private RelativeLayout rlHome;
    private boolean isClick = false;
    private int theme = 0;
    private RelativeLayout rlScreen;

    protected abstract int getViewLayoutId();

    protected abstract void initView(final Bundle savedInstanceState);

    protected abstract void reloadData();

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // ActionBar mActionBar = this.getActionBar();
            // if (mActionBar != null)
            // mActionBar.hide();
            // mActionBar.setDisplayShowHomeEnabled(false);

            ULog.i(tag, "======class: " + this.getClass().getSimpleName());
            if (pref == null)
                pref = new Prefs(getApplicationContext());
            // //////////
            // requestWindowFeature(Window.FEATURE_NO_TITLE);
            // if (getViewLayoutId() > 0) {
            // viewMain = this.getLayoutInflater().inflate(getViewLayoutId(), null);
            // setContentView(viewMain);
            // }

            if (this.getClass().getSimpleName().equals(SplashActivity.class.getSimpleName())) {
                setContentView(getViewLayoutId());
                initView(savedInstanceState);
            } else {

                setContentView(R.layout.layout_base);
                LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                FrameLayout frmMain = (FrameLayout) findViewById(R.id.frameMain);

                int rid = getViewLayoutId();
                if (rid != -1) {
                    viewMain = inflator.inflate(rid, null, false);
                    frmMain.removeAllViews();
                    frmMain.addView(viewMain);
                }

                setListenerView(R.id.btnSearch, this);
                rlScreen = getViewChild(R.id.rlScreen);
                rlHome = getViewChild(R.id.rlHome);
                initView(savedInstanceState);
                setListenerView(R.id.btnFlag, this);
            }
            // ///////ad
            if (!Constant.isPro) {
                AdView adView = getViewChild(R.id.adView);
                if (adView != null) {
                    adView.setVisibility(View.VISIBLE);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.loadAd(adRequest);
                }
            }
            // //
            // changeThemeBG();
            // setContentView(getViewLayoutId());
            // initView();


            // setAction();


        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(this, "onCreate error:" + e.getMessage());

        }
        // ////////////////////////

    }

    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
        if (!this.getClass().getSimpleName().equals(SplashActivity.class.getSimpleName())) {
            ULog.i("BaseAct", "onResume: class:" + this.getClass().getSimpleName());
            theme = pref.getIntValue(0, Constant.PREF_BG_THEME);
            changeThemeBG();
        }

        String tmp;
        tmp = this.getString(R.string.language);
        if (lang.equals("") || !lang.equals(tmp)) {
            lang = tmp;
            reloadData();
        }
        // GA
//        Utility.setScreenNameGA(this.getClass().getSimpleName() + " - lang:" + lang);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearch:
                if (isClick)
                    return;
                isClick = true;
                startActivity2(SearchAllActivity.class);
                break;

            case R.id.btnFlag:
                if (theme == 0)
                    theme = 1;
                else if (theme == 1)
                    theme = 2;
                else
                    theme = 0;
                changeThemeBG();
                break;
        }
    }

    private void changeThemeBG() {
        int drawable;
        if (theme == 0) {
            drawable = R.drawable.bg_home_green;
        } else if (theme == 1) {
            drawable = R.drawable.bg_home_pink;
        } else {
            drawable = R.drawable.bg_home_green2;
        }
        pref.putIntValue(theme, Constant.PREF_BG_THEME);
        rlScreen.setBackgroundResource(drawable);

        if (this instanceof RecognizeMainActicity) {
            RelativeLayout rlRecognizeMain = getViewChild(R.id.rlRecognizeMain);
            if (rlRecognizeMain != null)
                rlRecognizeMain.setBackgroundResource(drawable);
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getViewChild(int id) {
        return (V) this.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getViewLayout() {
        return (V) viewMain;
    }

    public void hideMenuHome() {
        rlHome.setVisibility(View.GONE);
    }

    public void setListenerView(int id, OnClickListener lis) {
        View v = getViewChild(id);
        v.setOnClickListener(lis);
    }

    public void startActivity2(Class<?> cls) {
        Intent i = new Intent(BaseActivity.this, cls);
        startActivity(i);
    }

    public void startActivity2(Class<?> cls, int param) {
        Intent i = new Intent(BaseActivity.this, cls);
        i.putExtra(Constant.INTENT_KIND, param);
        startActivity(i);
    }

    public void setVisibilityView(int id, boolean b) {
        View v = getViewChild(id);
        if (b)
            v.setVisibility(View.VISIBLE);
        else
            v.setVisibility(View.GONE);

    }


    public void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkie,
                                  DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, onOkie);
        builder.setNegativeButton(R.string.cancel, onCancel);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
