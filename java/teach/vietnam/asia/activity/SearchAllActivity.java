package teach.vietnam.asia.activity;

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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.SearchAllAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class SearchAllActivity extends BaseActivity implements OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 1000;
    private ListView lstSearch;
    private EditText edtSearch;
    private SearchAllAdapter adapter;
    private ProgressDialog progressDialog;
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

        Utility.setScreenNameGA("SearchAllActivity");

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

    @Override
    protected void onResume() {
        super.onResume();
//        new LoadData().execute();
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
                String text = edtSearch.getText().toString().toLowerCase();
                if (adapter != null)
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
                    phrases = String.format(Utility.getVi(adapter.getItem(position), lang), Utility.getDefaultWord(adapter.getItem(position), lang));
                    speakPhrases(phrases);
                } else {
                    ULog.i(LearnWordsActivity.class, "onItemClick NOT PREMIUM");
                    Utility.installPremiumApp(SearchAllActivity.this);
                }
            }
        });

    }

    private void speakPhrases(String phrases) {
        String soundName;

        try {
            soundName = phrases.replaceAll("!", "").replaceAll("\\?", "").replaceAll("[.]", "").replaceAll(",", "").toLowerCase();
            soundName = android.text.Html.fromHtml(soundName).toString();
            audio.speakWord(soundName);
        } catch (Exception e) {
            ULog.e(SearchAllActivity.class, "speakPhrases Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class LoadData extends AsyncTask<Void, Void, List> {

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
        protected List doInBackground(Void... params) {
            QueryBuilder qb;
            AbstractDao dao;
            try {

                dao = Utility.getDao(SearchAllActivity.this, lang);
                qb = dao.queryBuilder();

                qb.where(Utility.getO1(lang).notEq(""));
                qb.orderAsc(Utility.getO1(lang));

                ULog.i(this, "===data db:" + qb.list().size());
            } catch (Exception e) {
                ULog.e(SearchAllActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return qb.list();
        }

        @Override
        protected void onPostExecute(List lstData) {
            super.onPostExecute(lstData);

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (isFinishing())
                return;

            if (lstData != null && lstData.size() > 0) {
                adapter = new SearchAllAdapter(SearchAllActivity.this, lstData);
                lstSearch.setAdapter(adapter);
                // adapter.notifyDataSetChanged();
            }else{
//                startActivity2(MainActivity.class);
                finish();
            }
        }

    }

}
