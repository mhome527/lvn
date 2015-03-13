package teach.vietnam.asia.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.RecognizeMainActicity;
import teach.vietnam.asia.adapter.RecognizePagerAdapter;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.Utility;

public class LearnRecoginzeFragment extends BaseFragment {
    private static final String ARG_AMOUNT = "arg_pager";
    private static final String ARG_CURRPAGE = "arg_currpage";

    private int amount = 0;

    private TextView tvNumber;
    private TextView tvAmount;

    private ViewPager pagerRecognize;
    private RecognizePagerAdapter adapterPage;
    //    private DaoMaster daoMaster;
//    private AudioPlayer audio;
    private int currPage = 0;
    private ProgressDialog progress;
    private Prefs pref;
    private ImageButton imgLeft;
    private ImageButton imgRight;
    private TextView tvSearch;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param amount   Parameter 1.
     * @param currPage Parameter 2.
     * @return A new instance of fragment LearnRecoginzeFragment.
     */
    public static LearnRecoginzeFragment newInstance(int amount, int currPage) {
        LearnRecoginzeFragment fragment = new LearnRecoginzeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_AMOUNT, amount);
        args.putInt(ARG_CURRPAGE, currPage);
        fragment.setArguments(args);
        return fragment;
    }


    public LearnRecoginzeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amount = getArguments().getInt(ARG_AMOUNT);
            currPage = getArguments().getInt(ARG_CURRPAGE);
        }
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_learn_recognize;
    }

    @Override
    protected void initView(View view) {
        setListenerView(R.id.btnTest);
        pagerRecognize = getViewChild(R.id.pagerRecognize);
        tvNumber = getViewChild(R.id.tvNumber);
        tvAmount = getViewChild(R.id.tvAmount);
        imgLeft = getViewChild(R.id.imgLeft);
        imgRight = getViewChild(R.id.imgRight);
        tvSearch = getViewChild(R.id.tvSearch);

        setListenerView(R.id.imgLeft);
        setListenerView(R.id.imgRight);
        setListenerView(R.id.imgMenu);
        setListenerView(R.id.imgRecording);
        setListenerView(R.id.llRecognize);
//        audio = new AudioPlayer(getActivity());
        setInitData();

        /////GA
//        Utility.setScreenNameGA(LearnRecoginzeFragment.class.getSimpleName());

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgMenu:
                ((RecognizeMainActicity) getActivity()).showMenu();
                break;
            case R.id.btnTest:
                startFragment(TestRecognizeFragment.class, currPage);
                break;
            case R.id.imgLeft:
                if (currPage == 0) {
                    return;
                }
                currPage = currPage - 1;

                if (currPage == 0)
                    imgLeft.setVisibility(View.GONE);

                imgRight.setVisibility(View.VISIBLE);
                pagerRecognize.setCurrentItem(currPage);

                ((RecognizeMainActicity) getActivity()).hideMenu();

                break;
            case R.id.imgRight:
                if (currPage == amount - 1) {
                    return;
                }
                currPage = currPage + 1;

                if (currPage == amount - 1)
                    imgRight.setVisibility(View.GONE);

                imgLeft.setVisibility(View.VISIBLE);
                pagerRecognize.setCurrentItem(currPage);

                break;
            case R.id.llRecognize:
            case R.id.imgRecording:
                Utility.promptSpeechInput(getActivity(), Constant.REQ_CODE_SPEECH_INPUT, "vi");
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT:
//                if (resultCode == getActivity().RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                }
//                break;
//        }
//    }

    private void setInitData() {
//        lang = RecognizeActivity.this.getString(R.string.language);
//        audio = new AudioPlayer(getActivity());
        if (pref == null)
            pref = new Prefs(getActivity());
        pagerRecognize.setPageMargin(-60);
        pagerRecognize.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currPage = position;
                tvNumber.setText((currPage + 1) + "");
                tvAmount.setText(amount + "");

                pref.putIntValue(currPage, Constant.PREF_PAGE);
                if (currPage == 0)
                    imgLeft.setVisibility(View.GONE);
                else if (currPage == amount - 1)
                    imgRight.setVisibility(View.GONE);
                else {
                    imgLeft.setVisibility(View.VISIBLE);
                    imgRight.setVisibility(View.VISIBLE);
                }
                ((RecognizeMainActicity) getActivity()).hideMenu();
//                ULog.i(RecognizeActivity.class, "PageSelected Word:" + adapterPage.lstData.get(currPage).getVi());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        adapterPage = new RecognizePagerAdapter(getActivity(), amount);
        pagerRecognize.setAdapter(adapterPage);
        pagerRecognize.setCurrentItem(currPage);
        if (currPage == 0)
            imgLeft.setVisibility(View.GONE);
        else if (currPage == amount - 1)
            imgRight.setVisibility(View.GONE);
        else {
            imgLeft.setVisibility(View.VISIBLE);
            imgRight.setVisibility(View.VISIBLE);
        }
    }

    public void setCurrentPage(int currPage) {
        this.currPage = currPage;
        pagerRecognize.setCurrentItem(currPage);
    }
    public void setTextVoid(String text) {
        tvSearch.setText(text);
    }



}
