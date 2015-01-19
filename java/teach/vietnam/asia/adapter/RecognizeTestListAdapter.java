package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.tblRecognize;
import teach.vietnam.asia.utils.ULog;

@SuppressLint("DefaultLocale")
public class RecognizeTestListAdapter extends BaseAdapter {

    private Context context;
    private List<tblRecognize> listData;
    private LayoutInflater layoutInflater;
    private String lang = "";
    private String ans = "";

    public interface RecognizeTest {
        public String getCurrWord();
    }

    private RecognizeTest recognizeTest;

    public RecognizeTestListAdapter(Context context, List<tblRecognize> listData, RecognizeTest recognizeTest) {
        this.context = context;
//        this.listData = listData;
        this.listData = cloneData(listData);
        this.recognizeTest =  recognizeTest;
        RandData();
//        ULog.i(RecognizeTestListAdapter.class, "construct ans:" + ans);
        try {
            layoutInflater = LayoutInflater.from(context);
            lang = context.getString(R.string.language);

        } catch (Exception e) {
            ULog.e(SearchAllAdapter.class, "SearchAllAdapter Error: " + e.getMessage());
        }

    }

    private List<tblRecognize> cloneData(List<tblRecognize> listData){
        List<tblRecognize> lstTmp = new ArrayList<tblRecognize>();
        for(tblRecognize entry : listData){
            tblRecognize tmp = new tblRecognize();
            tmp.setVn(entry.getVn());
            tmp.setEx(entry.getEx());
            tmp.setEn(entry.getEn());
            tmp.setJa(entry.getJa());
            tmp.setKo(entry.getKo());
            tmp.setGroup_id(entry.getGroup_id());
            tmp.setWord_id(entry.getWord_id());
            lstTmp.add(tmp);
        }
        return lstTmp;
    }

    // public LearnAdapter(Context context) {
    // mContext = context;
    // }

    public int getCount() {
        return listData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.recognize_test_item, null);
            holder.tvWord = (TextView) view.findViewById(R.id.tvWord);
            holder.tvEx = (TextView) view.findViewById(R.id.tvEx);
            holder.tvOther = (TextView) view.findViewById(R.id.tvOther);

            holder.imgCheck = (ImageView) view.findViewById(R.id.imgCheck);
            holder.rlWord = (RelativeLayout) view.findViewById(R.id.rlWord);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.rlWord.setTag(listData.get(position).getVn());
        holder.rlWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag;
                tag = (String) holder.rlWord.getTag();
                ULog.i(RecognizePagerTestAdapter.class, "Word compare: " + tag + "; currWord:" + recognizeTest.getCurrWord());
                if (tag.equals(recognizeTest.getCurrWord())) {
                    holder.imgCheck.setBackgroundResource(R.drawable.o);
                    holder.tvWord.setTypeface(null, Typeface.BOLD);
                } else
                    holder.imgCheck.setBackgroundResource(R.drawable.x);
            }
        });

        holder.imgCheck.setBackgroundResource(R.drawable.uncheck_bg);
        if (listData.get(position).getEx() != null)
            holder.tvEx.setText(listData.get(position).getEx() + "");
        if (lang.equals("ja")) {
            if (listData.get(position).getJa() != null && !listData.get(position).getJa().equals("")) {
//                holder.tvEx.setText(listData.get(position).getEx());
                holder.tvOther.setText(": " + listData.get(position).getJa());
            }
        } else if (lang.equals("ko")) {
            if (listData.get(position).getKo() != null && !listData.get(position).getKo().equals("")) {
//                holder.tvEx.setText(listData.get(position).getEx());
                holder.tvOther.setText(": " + listData.get(position).getKo());
            }
        } else if (lang.equals("en")) {
            if (listData.get(position).getEn() != null && !listData.get(position).getEn().equals("")) {
//                holder.tvEx.setText(listData.get(position).getEx());
                holder.tvOther.setText(": " + listData.get(position).getEn());
            }
        }

        holder.tvWord.setText(listData.get(position).getVn());
        holder.tvEx.setText(listData.get(position).getEx());
        return view;
    }


    public class ViewHolder {
        TextView tvWord;
        TextView tvOther;
        TextView tvEx;
        ImageView imgCheck;
        RelativeLayout rlWord;
    }

    private void RandData() {
        int number1;
        int number2;
        tblRecognize entryTmp;
        Random ran;
        for(int i = 0; i < listData.size() ; i++) {
            ran = new Random();
            number1 = ran.nextInt(listData.size() - 1);

            ran = new Random();
            number2 = ran.nextInt(listData.size() - 1);

            entryTmp = listData.get(number2);
            listData.set(number2, listData.get(number1));
            listData.set(number1, entryTmp);

        }
    }

}