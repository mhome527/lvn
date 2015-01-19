package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.PracticeDetailEntity;
import teach.vietnam.asia.entity.tblRecognize;
import teach.vietnam.asia.utils.ULog;

public class RecognizePagerTestAdapter extends PagerAdapter  {

    private Activity activity;
    public ArrayList<PracticeDetailEntity> lstExceriese;

    //    private int num;
    private List<tblRecognize> dataRecognize;
//    public String currWord = "";
//    public int amount = 3;
//    private int arrW[];
    private RecognizeTestListAdapter.RecognizeTest recognizeTest;

    public RecognizePagerTestAdapter(Activity activity, List<tblRecognize> dataRecognize, RecognizeTestListAdapter.RecognizeTest recognizeTest) {
        lstExceriese = new ArrayList<PracticeDetailEntity>();
//        this.num = num + 1;
        this.activity = activity;
//        this.currWord = currWord;
        this.dataRecognize = dataRecognize;
        this.recognizeTest = recognizeTest;
//        ULog.i(RecognizePagerTestAdapter.class, "() curr:" + currWord);
    }

    @Override
    public int getCount() {
        return dataRecognize.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @SuppressLint("InflateParams")
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.recognize_test_list, null);

        ListView lstRecognize = (ListView) view.findViewById(R.id.lstRecognize);
//        lstRecognize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                if (position == arrW[position]) {
//                    ULog.i(RecognizePagerTestAdapter.class, "Item Correct oooooooo");
//                }
//            }
//        });

        if (dataRecognize != null || dataRecognize.size() > 0) {
            RecognizeTestListAdapter adapter = new RecognizeTestListAdapter(activity, dataRecognize, recognizeTest);
            lstRecognize.setAdapter(adapter);
//            currWord = dataRecognize.get(arrW[position]).getVn();
//            ULog.i(RecognizePagerTestAdapter.class, "Speak word: " + currWord + "; pos:" + position + "; ans:" + arrW[position] + "; arr0:" + arrW[0]);
        } else {
            ULog.e(RecognizePagerTestAdapter.class, "load data recognize error pos:" + position);
        }
        view.setTag(position);
        ((ViewPager) collection).addView(view, 0);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

//    @Override
//    public String getCurrWord() {
//        ULog.i(RecognizePagerTestAdapter.class, "getCurrWord word:" + currWord);
//        return currWord;
//    }




}
