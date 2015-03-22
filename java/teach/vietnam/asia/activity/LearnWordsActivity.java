package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.LearnWordAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class LearnWordsActivity extends BaseActivity implements OnClickListener {

    private GridView gridWord;
    private TextView tvViet;
    private TextView tvOther;

    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    private int kind = 1;
    private List lstData;

    @Override
    protected int getViewLayoutId() {
        return R.layout.learn_word_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        gridWord = getViewChild(R.id.gridWord);
        tvViet = getViewChild(R.id.tvViet);
        tvOther = getViewChild(R.id.tvOther);
        setListenerView(R.id.btnSpeak, this);
        setInitData();

        Utility.setScreenNameGA("LearnWordsActivity - lang:" + Locale.getDefault().getLanguage());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpeak:
                if (Constant.isPro)
                    speakWord();
                else
                    Utility.installPremiumApp(LearnWordsActivity.this);
                // ULog.i(LearnWordsActivity.class, "onClick NOT PREMIUM");
                break;
        }
        super.onClick(v);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (audio != null)
            audio.stopAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String tmp;
//        tmp = LearnWordsActivity.this.getString(R.string.language);
//        if (lang.equals("") || !lang.equals(tmp)) {
//            lang = tmp;
//            new LoadData().execute();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void reloadData() {
        new LoadData().execute();
    }

    private void speakWord() {
        audio.speakWord(tvViet.getText().toString());
    }

    private void setInitData() {
        audio = new AudioPlayer(LearnWordsActivity.this);
        kind = getIntent().getIntExtra(Constant.INTENT_KIND, 1);
        gridWord.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                setDefaultText(postion);
                if (Constant.isPro)
                    speakWord();
                else
                    ULog.i(LearnWordsActivity.class, "onItemClick NOT PREMIUM");
            }
        });

    }

    private void setDefaultText(int postion) {
        tvViet.setText(Utility.getVi(lstData.get(postion), lang));
        tvOther.setText(Utility.getO1(lstData.get(postion), lang));
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LearnWordsActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            QueryBuilder qb;
            AbstractDao dao;
            try {
//                daoMaster = ((MyApplication) getApplication()).daoMaster;
//                dao = daoMaster.newSession().getTblVietDao();

                dao = Utility.getDao(LearnWordsActivity.this, lang);
                qb = dao.queryBuilder();

                if (kind == 12)
                    qb.where(Utility.getO1(lang).notEq(""), qb.or(Utility.getKind(lang).eq(kind),
                            Utility.getKind(lang).eq(5)));
                else if (kind == 1)
                    qb.where(Utility.getO1(lang).notEq(""), qb.or(Utility.getKind(lang).eq(kind),
                            Utility.getKind(lang).eq(2)));
                else
                    qb.where(Utility.getO1(lang).notEq(""), Utility.getKind(lang).eq(kind));


                ULog.i(this, "===data db:" + qb.list().size());
                lstData = qb.list();
            } catch (Exception e) {
                ULog.e(LearnWordsActivity.class, lang + "; load data error:" + e.getMessage());
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            LearnWordAdapter adapter;

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (isFinishing()) {
                return;
            }
            if (lstData != null && lstData.size() > 0) {
                adapter = new LearnWordAdapter(LearnWordsActivity.this, lstData, lang);
                gridWord.setAdapter(adapter);
                setDefaultText(0);
            } else {
//                startActivity2(MainActivity.class);
                finish();
            }
        }

    }

}
