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
import java.util.Locale;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.SearchAdapter;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class SearchWordsActivity extends BaseActivity implements OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 1000;
    private ListView lstSearch;
    private EditText edtSearch;
    private SearchAdapter adapter;
    private ProgressDialog progressDialog;
    private int position;
    private List lstData;
//    private String lang;

    @Override
    protected int getViewLayoutId() {
        return R.layout.search_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lstSearch = getViewChild(R.id.lstSearch);
        edtSearch = getViewChild(R.id.edtSearch);

        setInitData();

        position = getIntent().getIntExtra(Constant.INTENT_POSITION, 0);

        Utility.setScreenNameGA("SearchWordsActivity - lang:" + Locale.getDefault().getLanguage());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgRecording:
                Utility.promptSpeechInput(SearchWordsActivity.this, REQ_CODE_SPEECH_INPUT);
                break;
        }
        super.onClick(v);
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtSearch.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        lang = SearchWordsActivity.this.getString(R.string.language);

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

    private void setInitData() {
        hideMenuHome();
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
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Intent intentData = new Intent();
                intentData.putExtra(Constant.INTENT_POSITION, position);
                intentData.putExtra(Constant.INTENT_WORD, Utility.getVi(lstData.get(pos), lang));
                SearchWordsActivity.this.setResult(RESULT_OK, intentData);
                SearchWordsActivity.this.finish();
            }
        });
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(SearchWordsActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected Void doInBackground(Void... params) {
            QueryBuilder qb;
            AbstractDao dao;

            try {
                dao = Utility.getDao(SearchWordsActivity.this, lang);
                qb = dao.queryBuilder();

                qb.where(Utility.getKind(lang).notEq(11));
                qb.orderAsc(Utility.getO1(lang));

                ULog.i(this, "===data db:" + qb.list().size());
                lstData = qb.list();
            } catch (Exception e) {
                ULog.e(SearchWordsActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (isFinishing())
                return;
            if (lstData != null && lstData.size() > 0) {

                adapter = new SearchAdapter(SearchWordsActivity.this, lstData);
                lstSearch.setAdapter(adapter);
                // adapter.notifyDataSetChanged();
            }else
                startActivity2(MainActivity.class);

        }

    }

}
