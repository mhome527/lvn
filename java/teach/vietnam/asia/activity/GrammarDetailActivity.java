package teach.vietnam.asia.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Locale;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class GrammarDetailActivity extends BaseActivity {

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_grammar_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int pos;
        String []arrName;
        String filename = "file:///android_asset/grammar/en-classifiers.html";
        WebView webView = getViewChild(R.id.webView);
        try {
            WebSettings settings = webView.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            settings.setDefaultFontSize(20);

            pos = getIntent().getIntExtra(Constant.INTENT_POSITION, 0);
            arrName = getResources().getStringArray(R.array.arr_name_grammar);
            if (pos < arrName.length)
                filename = "file:///android_asset/grammar/" + arrName[pos] +".html";
            webView.loadUrl(filename);

            Utility.setScreenNameGA("GrammarDetailActivity - lang:" + Locale.getDefault().getLanguage());

        }catch (Exception e){
            ULog.e(GrammarDetailActivity.class, "initView Error:" + e.getMessage());
        }
    }

    @Override
    protected void reloadData() {

    }


}
