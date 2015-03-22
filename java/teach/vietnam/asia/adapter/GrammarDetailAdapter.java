package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import teach.vietnam.asia.R;

public class GrammarDetailAdapter extends PagerAdapter {

    //    private Activity activity;
    String[] arrName;

    public GrammarDetailAdapter(Activity activity) {
//        this.activity = activity;
        arrName = activity.getResources().getStringArray(R.array.arr_name_grammar);
    }

    @Override
    public int getCount() {
        return arrName.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @SuppressLint("InflateParams")
    public Object instantiateItem(ViewGroup collection, final int position) {
        String filename;
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.detail_layout_grammar, null);

        WebView webView = (WebView) view.findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDefaultFontSize(20);

        filename = "file:///android_asset/grammar/" + arrName[position] + ".html";
        webView.loadUrl(filename);

        ((ViewPager) collection).addView(view, 0);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
