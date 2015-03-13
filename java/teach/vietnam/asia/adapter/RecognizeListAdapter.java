package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import teach.vietnam.asia.R;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

@SuppressLint("DefaultLocale")
public class RecognizeListAdapter extends BaseAdapter {

    private Context context;
    private List listData;
    private LayoutInflater layoutInflater;
    private String lang = "";
    private AudioPlayer audio;
    private int currPage;

    public RecognizeListAdapter(Context context, List listData, int currPage) {
        this.context = context;
        this.listData = listData;
        this.currPage = currPage;
        try {
            layoutInflater = LayoutInflater.from(context);
            lang = context.getString(R.string.language);
            audio = new AudioPlayer(context);

        } catch (Exception e) {
            ULog.e(SearchAllAdapter.class, "SearchAllAdapter Error: " + e.getMessage());
        }

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
        String ex, ot;
        if (view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.recognize_item, null);
            holder.tvWord = (TextView) view.findViewById(R.id.tvWord);
            holder.tvEx = (TextView) view.findViewById(R.id.tvEx);
//            holder.tvOther = (TextView) view.findViewById(R.id.tvOther);

            holder.btnSpeak = (Button) view.findViewById(R.id.btnSpeak);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ex = Utility.getREC_Ex(listData.get(position), lang);
        if (ex != null && !ex.equals("")) {
            holder.tvEx.setText(ex);
//        holder.tvOther.setText("");

        }
        holder.tvWord.setText(Utility.getREC_VN(listData.get(position), lang));

        ot = Utility.getREC_Ot(listData.get(position), lang);
        if (ot != null && !ot.equals(""))
            holder.tvEx.setText(ex + ": " + ot);

        holder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                audio.speakWord(listData.get(position).getVn());
                if (Constant.isPro || currPage < 10)
                    audio.speakWord(Utility.getREC_VN(listData.get(position), lang));
                else
                    Utility.installPremiumApp(context);
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvWord;
//        TextView tvOther;
        TextView tvEx;
        Button btnSpeak;
    }

}