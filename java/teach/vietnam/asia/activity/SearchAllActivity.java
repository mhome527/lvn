package teach.vietnam.asia.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.SearchAllAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.entity.tblVietDao;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SearchAllActivity extends BaseActivity implements OnClickListener {

    private ListView lstSearch;
    private EditText edtSearch;
    private SearchAllAdapter adapter;
    private tblVietDao dao;
    private DaoMaster daoMaster;
    private ProgressDialog progressDialog;
    private final int REQ_CODE_SPEECH_INPUT = 1000;
    private AudioPlayer audio;

    @Override
    protected int getViewLayoutId() {
        return R.layout.search_all_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lstSearch = getViewChild(R.id.lstSearch);
        edtSearch = getViewChild(R.id.edtSearch);
        audio = new AudioPlayer(SearchAllActivity.this);

        setInitData();
        new LoadData().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgRecording:
                Utility.promptSpeechInput(SearchAllActivity.this, REQ_CODE_SPEECH_INPUT);
                break;
            case R.id.btnSearch:
                return;
        }
        super.onClick(v);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (audio != null)
            audio.stopAll();
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtSearch.setText(result.get(0));
                }
                break;
        }
    }


    private void setInitData() {
        setListenerView(R.id.imgRecording, this);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        lstSearch.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String phrases;
                if (Constant.isPro) {
                    phrases = String.format(adapter.getItem(position).getVi(), adapter.getItem(position).getDefault_word());
                    speakPhrases(phrases);
                }else {
                    ULog.i(LearnWordsActivity.class, "onItemClick NOT PREMIUM");
                    Utility.installPremiumApp(SearchAllActivity.this);
                }
            }
        });

    }

    private void speakPhrases(String phrases) {
        String soundName;

        try {
            soundName = phrases.replaceAll("!", "").replaceAll("\\?", "").replaceAll("[.]", "").replaceAll(",", "").replaceAll("<u>", "").replaceAll("</u>", "").toLowerCase();
            audio.speakWord(soundName);
        } catch (Exception e) {
            ULog.e(SearchAllActivity.class, "speakPhrases Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class LoadData extends AsyncTask<Void, Void, List<tblViet>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(SearchAllActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected List<tblViet> doInBackground(Void... params) {
            QueryBuilder<tblViet> qb;
            String lang;
            try {
                daoMaster = ((MyApplication) getApplication()).daoMaster;
                dao = daoMaster.newSession().getTblVietDao();
                qb = dao.queryBuilder();

                // qb.where(tblVietDao.Properties.Kind.eq(1));
                lang = SearchAllActivity.this.getString(R.string.language);
                if (lang.equals("ja")) {
                    qb.where(tblVietDao.Properties.Ja.notEq(""));
                    qb.orderAsc(tblVietDao.Properties.Ja);
                }else if (lang.equals("ko")) {
                    qb.where(tblVietDao.Properties.Ko.notEq(""));
                    qb.orderAsc(tblVietDao.Properties.Ko);
                }else{
                    qb.where(tblVietDao.Properties.En.notEq(""));
                    qb.orderAsc(tblVietDao.Properties.En);
                }
                // qb.orderDesc(tblVietDao.Properties.Show);
                ULog.i(this, "===data db:" + qb.list().size());
            } catch (Exception e) {
                ULog.e(SearchAllActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return qb.list();
        }

        @Override
        protected void onPostExecute(List<tblViet> lstData) {
            super.onPostExecute(lstData);

            if (!isFinishing()) {
                progressDialog.dismiss();
            }
            if (lstData != null && lstData.size() > 0) {
                adapter = new SearchAllAdapter(SearchAllActivity.this, lstData);
                lstSearch.setAdapter(adapter);

                // adapter.notifyDataSetChanged();
            }
        }

    }

}
