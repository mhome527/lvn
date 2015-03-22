package teach.vietnam.asia.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.GrammarDetailAdapter;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Utility;

public class GrammarDetailActivity extends BaseActivity {

    private ViewPager pagerGrammar;
    private final String PREF_PAGER_GRAMMAR ="pager_grammar";

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_grammar_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int pos;
        pagerGrammar = getViewChild(R.id.pagerGrammar);

        pos = getIntent().getIntExtra(Constant.INTENT_POSITION, 0);
        pagerGrammar.setAdapter(new GrammarDetailAdapter(this));
        pagerGrammar.setCurrentItem(pos);

        int currPage = pref.getIntValue(0, PREF_PAGER_GRAMMAR);
        pagerGrammar.setCurrentItem(currPage);

        pagerGrammar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pref.putIntValue(position, Constant.PREF_PAGE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Utility.setScreenNameGA("GrammarDetailActivity - lang:" + lang);
    }

    @Override
    protected void reloadData() {

    }


}
