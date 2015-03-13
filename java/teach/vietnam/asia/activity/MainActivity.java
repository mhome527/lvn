package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Locale;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.db.AndroidDatabaseManager;
import teach.vietnam.asia.db.DBDataLanguage;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class MainActivity extends BaseActivity implements OnClickListener {

    private boolean isLoading = true;
//    private Class clsForm;
//    private int mKind = 0;
    private boolean isClick = false;
    public ProgressDialog progressDialog;

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
        if(BuildConfig.DEBUG){
            Button btnShowDB = getViewChild(R.id.btnShowDB);
            btnShowDB.setVisibility(View.VISIBLE);
        }

        setListenerView(R.id.btnShowDB, this);
        Utility.setScreenNameGA("MainActivity - lang:" + lang);

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
                moveToForm(GrammarActivity.class);
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

}
