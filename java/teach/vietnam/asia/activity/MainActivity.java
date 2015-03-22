package teach.vietnam.asia.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.VolleyError;

import java.util.Locale;
import java.util.Map;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.api.InfoAppAPI;
import teach.vietnam.asia.db.AndroidDatabaseManager;
import teach.vietnam.asia.db.DBDataLanguage;
import teach.vietnam.asia.entity.InfoAppEntity;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class MainActivity extends BaseActivity implements OnClickListener {

    private boolean isLoading = true;
    //    private Class clsForm;
//    private int mKind = 0;
    private boolean isClick = false;
    public ProgressDialog progressDialog;
    private final int NOTIFICATION_ID = 111;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setListenerView(R.id.btnSearch, this);
        setListenerView(R.id.btnFruit, this);
        setListenerView(R.id.btnRecognize, this);
        setListenerView(R.id.btnFood, this);
        setListenerView(R.id.btnAlphabet, this);
        setListenerView(R.id.btnNumber, this);
        setListenerView(R.id.btnColor, this);
        setListenerView(R.id.btnAnimal, this);
        setListenerView(R.id.btnBody, this);
//        setListenerView(R.id.btnFurniture, this);
        setListenerView(R.id.btnOther, this);
        setListenerView(R.id.btnDialog, this);
        setListenerView(R.id.btnPractice, this);
        setListenerView(R.id.btnGrammar, this);

        ////test db
        if (BuildConfig.DEBUG) {
            Button btnShowDB = getViewChild(R.id.btnShowDB);
            btnShowDB.setVisibility(View.VISIBLE);
        }

        setListenerView(R.id.btnShowDB, this);
        Utility.setScreenNameGA("MainActivity - lang:" + lang);

        //check update version
        checkUpdate();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFruit:
                moveToForm(LearnWordsActivity.class, 1);
                break;
            case R.id.btnRecognize:
                moveToForm(RecognizeMainActicity.class, 0);
                break;
            case R.id.btnFood:
                moveToForm(LearnFoodActivity.class, 3);
                break;
            case R.id.btnAlphabet:
                moveToForm(AlphabetActivity.class);
                break;
            case R.id.btnNumber:
                moveToForm(NumberActivity.class);
                break;
            case R.id.btnColor:
                moveToForm(ColorActivity.class);
                break;
            case R.id.btnAnimal:
                moveToForm(LearnWordsActivity.class, 4);
                break;
            case R.id.btnBody:
                moveToForm(BodyActivity.class);
                break;
//            case R.id.btnFurniture:
//                moveToForm(LearnWordsActivity.class, 5);
//                break;
            case R.id.btnOther:
                moveToForm(LearnWordsActivity.class, 12);
                break;
            case R.id.btnDialog:
                moveToForm(PhrasesActivity.class);
                break;
            case R.id.btnPractice:
                moveToForm(PracticeActivity.class);
                break;
            case R.id.btnGrammar:
                moveToForm(GrammarDetailActivity.class);
                break;
            case R.id.btnShowDB:
                startActivity2(AndroidDatabaseManager.class);
                break;
//		case R.id.btnSearch:
//			startActivity2(SearchAllActivity.class);
//			break;		
        }

        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
        isLoading = true;
        Locale current = this.getResources().getConfiguration().locale;
        ULog.i(MainActivity.class, "resume ========================= lang:" + current.toString().toLowerCase());

        new DBDataLanguage(this, new DBDataLanguage.ICreateTable() {
            @Override
            public void iFinishCreate() {
                ULog.i(MainActivity.class, "Load data finish");
                isLoading = false;
                isClick = false;
            }
        }).execute();
    }


    @Override
    protected void reloadData() {

    }

    @Override
    public void onBackPressed() {
        if (!isLoading) {
            Utility.confirmCloseApp(MainActivity.this);
//            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void moveToForm(Class cls) {
        moveToForm(cls, 0);
    }

    private void moveToForm(Class cls, int kind) {
        if (isClick)
            return;
        isClick = true;
        if (isLoading) {
//            clsForm = cls;
//            mKind = kind;
            if (progressDialog != null)
                progressDialog.show();
        } else {
            startActivity2(cls, kind);
        }
    }

    private void checkUpdate() {
        new InfoAppAPI<InfoAppEntity>(InfoAppEntity.class) {
            @Override
            public void onResponse(InfoAppEntity infoAppEntity) {
                try {
                    if (!isFinishing()) {
                        if (infoAppEntity != null) {
                            ULog.i(MainActivity.class, "checkUpdate onResponse " + infoAppEntity.version_name);

                            if (infoAppEntity.version_name != null &&
                                    !infoAppEntity.version_name.equals("") &&
                                    !infoAppEntity.version_name.equals(BuildConfig.VERSION_NAME)) {
                                //show msg
//                                Utility.confirmUpdate(MainActivity.this);
                                pushNotification();

                            } else {
                                ULog.i(MainActivity.class, "Don't update app");
                            }
                        }
                    }
                } catch (Exception e) {
                    ULog.e(MainActivity.class, "checkUpdate error: " + e.getMessage());
                }

            }

            @Override
            public Map<String, String> getParamsAPI() {
                return null;
            }

            @Override
            public void onErrorResponse(VolleyError error, String msg) {
                ULog.e(MainActivity.class, "checkUpdate Error: " + msg);
            }
        };

    }

    private void pushNotification() {

        Notification myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(MainActivity.this.getString(R.string.msg_update_app))
                .setContentText(MainActivity.this.getString(R.string.msg_update_app))
                .setTicker(MainActivity.this.getString(R.string.msg_update_app))
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, myNotification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setAutoCancel(true);
        builder.setTicker(MainActivity.this.getString(R.string.msg_update_app));
        builder.setContentTitle(MainActivity.this.getString(R.string.vietnamese));
        builder.setContentText(MainActivity.this.getString(R.string.msg_update_app));
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.icon);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));

        builder.setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, intent, 0));
        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

}
