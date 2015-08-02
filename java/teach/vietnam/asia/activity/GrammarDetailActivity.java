package teach.vietnam.asia.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.GrammarDetailAdapter;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class GrammarDetailActivity extends BaseActivity {

    private String TAG = "GrammarDetailActivity";
    private ViewPager pagerGrammar;
    private final String PREF_PAGER_GRAMMAR ="pager_grammar";
    private ImageButton imgLeft;
    private ImageButton imgRight;

    private GrammarDetailAdapter adapter;
    private boolean isTouchL = false;
    private boolean isTouchR = false;
    private int currPage = 0;
    private int amount = 0;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_grammar_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int pos;
        pagerGrammar = getViewChild(R.id.pagerGrammar);

        pos = getIntent().getIntExtra(Constant.INTENT_POSITION, 0);

        adapter = new GrammarDetailAdapter(this);
        pagerGrammar.setAdapter(adapter);
        pagerGrammar.setCurrentItem(pos);

        amount = adapter.getCount();

        currPage = pref.getIntValue(0, PREF_PAGER_GRAMMAR);
        pagerGrammar.setCurrentItem(currPage);
//        pagerGrammar.setPageMargin(-60);

        pagerGrammar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ULog.i(TAG, "onPageScrollStateChanged curr:" + currPage);
            }

            @Override
            public void onPageSelected(int position) {
                pref.putIntValue(position, PREF_PAGER_GRAMMAR);
                ULog.i(TAG, "onPageSelected curr:" + currPage);
                currPage = position;
//                tvNumber.setText((currPage + 1) + "");
//                tvAmount.setText(amount + "");

//                pref.putIntValue(currPage, Constant.PREF_PAGE + PREF_PAGER_GRAMMAR);
                if (currPage == 0)
                    imgLeft.setVisibility(View.GONE);
                else if (currPage == amount - 1)
                    imgRight.setVisibility(View.GONE);
                else {
                    imgLeft.setVisibility(View.VISIBLE);
                    imgRight.setVisibility(View.VISIBLE);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        isTouchL = false;
                        isTouchR = false;
                    }
                }, 200);


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ULog.i(TAG, "onPageScrollStateChanged curr:" + currPage);
//                isTouchL = false;
//                isTouchR = false;
            }
        });

        imgLeft = getViewChild(R.id.imgLeft);
        imgRight = getViewChild(R.id.imgRight);

//        setListenerView(R.id.imgLeft);
//        setListenerView(R.id.imgRight);

        imgLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isTouchL)
                    return false;
                ULog.i(TAG, "left touch: " + isTouchL);

                isTouchL = true;

                if (currPage == 0) {
                    return false;
                }
                currPage = currPage - 1;

                if (currPage == 0)
                    imgLeft.setVisibility(View.GONE);

                imgRight.setVisibility(View.VISIBLE);
                pagerGrammar.setCurrentItem(currPage);
                return false;
            }
        });

        imgRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isTouchR)
                    return false;
                ULog.i(TAG, "Right touch: " + isTouchR);
                if (currPage == amount - 1) {
                    return false;
                }

                isTouchR = true;

                currPage = currPage + 1;

                if (currPage == amount - 1)
                    imgRight.setVisibility(View.GONE);

                imgLeft.setVisibility(View.VISIBLE);
                pagerGrammar.setCurrentItem(currPage);
                return false;
            }
        });


        pref.putIntValue(currPage, Constant.PREF_PAGE);
        if (currPage == 0)
            imgLeft.setVisibility(View.GONE);
        else if (currPage == amount - 1)
            imgRight.setVisibility(View.GONE);
        else {
            imgLeft.setVisibility(View.VISIBLE);
            imgRight.setVisibility(View.VISIBLE);
        }

        Utility.setScreenNameGA(TAG);
    }

    @Override
    protected void reloadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isTouchL = false;
        isTouchR = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgLeft:
                if (currPage == 0) {
                    return;
                }
                currPage = currPage - 1;

                if (currPage == 0)
                    imgLeft.setVisibility(View.GONE);

                imgRight.setVisibility(View.VISIBLE);
                pagerGrammar.setCurrentItem(currPage);

                break;
            case R.id.imgRight:
                if (currPage == amount - 1) {
                    return;
                }
                currPage = currPage + 1;

                if (currPage == amount - 1)
                    imgRight.setVisibility(View.GONE);

                imgLeft.setVisibility(View.VISIBLE);
                pagerGrammar.setCurrentItem(currPage);

                break;
        }
    }
}
