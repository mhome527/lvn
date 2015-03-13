package teach.vietnam.asia.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;
import java.util.Random;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.LearnWordsActivity;
import teach.vietnam.asia.activity.RecognizeMainActicity;
import teach.vietnam.asia.adapter.RecognizePagerTestAdapter;
import teach.vietnam.asia.adapter.RecognizeTestListAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import teach.vietnam.asia.view.MainMenuLayout;


public class TestRecognizeFragment extends BaseFragment implements RecognizeTestListAdapter.RecognizeTest {
    private static final String ARG_PAGER = "arg_pager";

    public int currPage = 0;
    public int arrW[];
    //    private DaoMaster daoMaster;
//    private tblRecognizeDao dao;
    private List dataRecognize;
    private ViewPager pagerRecognize;
    private RecognizePagerTestAdapter adapterPage;
    //    private DaoMaster daoMaster;
    private AudioPlayer audio;
    private int kind = 1;
    private MainMenuLayout mainLayout;
    private ImageButton imgLeft;
    private ImageButton imgRight;
    private int amount = 3;
    private int currAns = 0;
    private String lang;
//    private String currWord ="";


//    private ProgressDialog progress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment LearnRecoginzeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestRecognizeFragment newInstance(int param1) {
        TestRecognizeFragment fragment = new TestRecognizeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currPage = getArguments().getInt(ARG_PAGER);
        }
    }


    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_test_recognize;
    }

    @Override
    protected void initView(View view) {
        setListenerView(R.id.btnLearn);
        setListenerView(R.id.imgSpeak);
        imgLeft = getViewChild(R.id.imgLeft);
        imgRight = getViewChild(R.id.imgRight);
        setListenerView(R.id.imgLeft);
        setListenerView(R.id.imgRight);
        pagerRecognize = getViewChild(R.id.pagerRecognize);


        /////GA
//        Utility.setScreenNameGA(TestRecognizeFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        lang = getActivity().getString(R.string.language);
        setInitData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnLearn:
                    startFragment(LearnRecoginzeFragment.class, currPage);
                    break;
                case R.id.imgSpeak:
//                    audio.speakWord(currWord);
                    if (Constant.isPro || currPage < 10)
                        audio.speakWord(Utility.getREC_VN(dataRecognize.get(arrW[currAns]), lang));
                    else
                        Utility.installPremiumApp(getActivity());
                    break;
                case R.id.imgLeft:
                    if (currAns <= 0) {
                        currAns = 0;
                        return;
                    }
                    currAns = currAns - 1;

                    if (currAns == 0)
                        imgLeft.setVisibility(View.GONE);

                    imgRight.setVisibility(View.VISIBLE);
                    pagerRecognize.setCurrentItem(currAns);
                    ((RecognizeMainActicity) getActivity()).hideMenu();

                    break;
                case R.id.imgRight:
                    if (currAns >= amount - 1) {
                        currAns = amount - 1;
                        return;
                    }
                    currAns = currAns + 1;

                    if (currAns == amount - 1)
                        imgRight.setVisibility(View.GONE);

                    imgLeft.setVisibility(View.VISIBLE);
                    pagerRecognize.setCurrentItem(currAns);

                    break;
            }
        } catch (Exception e) {
            ULog.e(TestRecognizeFragment.class, "Click error:" + e.getMessage());
        }
    }


    private void setInitData() {
        ULog.i(TestRecognizeFragment.class, "setInitData kind:" + kind);
        audio = new AudioPlayer(getActivity());
        pagerRecognize.setPageMargin(-50);
        pagerRecognize.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currAns = position;
//                adapterPage.notifyDataSetChanged();
                if (currAns == 0)
                    imgLeft.setVisibility(View.GONE);
                else if (currAns == amount - 1)
                    imgRight.setVisibility(View.GONE);
                else {
                    imgLeft.setVisibility(View.VISIBLE);
                    imgRight.setVisibility(View.VISIBLE);
                }
//                setCurrentWord(currAns);
                ((RecognizeMainActicity) getActivity()).hideMenu();

//                ULog.i(TestRecognizeFragment.class, "arr:" + arrW[0] + "," + arrW[1] + "," + arrW[2] + "," + arrW[3]);
//                ULog.i(TestRecognizeFragment.class, "PageSelected currPage:" + currAns + "; " +
//                        Utility.getREC_VN(dataRecognize.get(arrW[currAns]), lang) + " ||| " +
//                        Utility.getREC_VN(dataRecognize.get(0), lang) +
//                        Utility.getREC_VN(dataRecognize.get(1), lang) +
//                        Utility.getREC_VN(dataRecognize.get(2), lang) +
//                        Utility.getREC_VN(dataRecognize.get(3), lang));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        loadData();
        setArrData();

        adapterPage = new RecognizePagerTestAdapter(getActivity(), dataRecognize, this);
        pagerRecognize.setAdapter(adapterPage);
//        amount = adapterPage.amount;
//        setCurrentWord(currAns);
        if (currAns == 0)
            imgLeft.setVisibility(View.GONE);
        else if (currAns == amount - 1)
            imgRight.setVisibility(View.GONE);
        else {
            imgLeft.setVisibility(View.VISIBLE);
            imgRight.setVisibility(View.VISIBLE);
        }

    }

    private void setArrData() {
        arrW = new int[amount];
        for (int i = 0; i < amount; i++) {
            arrW[i] = i;
        }

        for (int i = 0; i < amount - 1; i++) {

            Random ran = new Random();
            Random ran2 = new Random();
            int value = ran.nextInt(amount);
            int value2 = ran2.nextInt(amount);
            int tmp = arrW[value];
            arrW[value] = arrW[value2];
            arrW[value2] = tmp;
        }
//        ULog.i(TestRecognizeFragment.class, "arr:" + arrW[0] + "," + arrW[1] + "," + arrW[2] + "," + arrW[3]);
    }

//    public void setCurrentWord(int currAns) {
//        currWord = dataRecognize.get(arrW[currAns]).getVn();
//        ULog.i(RecognizePagerTestAdapter.class, "setCurrentWord page:" + currAns + "; word:" + currWord + " a0:" + arrW[0] + "; aN:" + arrW[currAns]);
//    }

    private void loadData() {
        QueryBuilder qb;
        AbstractDao dao;
        try {
//            daoMaster = ((MyApplication) getActivity().getApplication()).daoMaster;
//            dao = daoMaster.newSession().getTblRecognizeDao();

            dao = Utility.getRecDao(getActivity(), lang);
            qb = dao.queryBuilder();
            qb.where(Utility.getREC_GroupID(lang).eq(currPage + 1));

            ULog.i(this, "===loadData data db:" + qb.list().size());
            dataRecognize = qb.list();
            amount = qb.list().size();
        } catch (Exception e) {
            ULog.e(LearnWordsActivity.class, "load data error:" + e.getMessage());
        }
    }

    @Override
    public String getCurrWord() {
//        ULog.i(RecognizePagerTestAdapter.class, "getCurrWord word:" + dataRecognize.get(arrW[currAns]).getVn());
        return Utility.getREC_VN(dataRecognize.get(arrW[currAns]), lang);
    }

}
