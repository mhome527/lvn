package teach.vietnam.asia.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import teach.vietnam.asia.activity.PhrasesActivity;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.SearchWordsActivity;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class PhrasesAdapter extends BaseAdapter implements SectionIndexer {

    private PhrasesActivity activity;
    private List<tblViet> listData;
    private List<tblViet> listData2;
    private LayoutInflater layoutInflater;
    private String lang = "";
    private String[] alpha;
    public AudioPlayer audio;

    @SuppressLint("DefaultLocale")
    public PhrasesAdapter(PhrasesActivity activity, List<tblViet> listData) {
        int i = 0;
        audio = new AudioPlayer(activity);

        ULog.i(PhrasesAdapter.class, "PracticeAdapter locale:" + Locale.getDefault().getLanguage());
        this.activity = activity;
        this.listData = listData;
        listData2 = new ArrayList<tblViet>();
        listData2.addAll(listData);
        lang = activity.getString(R.string.language);
        layoutInflater = LayoutInflater.from(activity);

        alpha = null;
        alpha = new String[listData.size()];
        if (lang.equals("ja")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getJa().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else if (lang.equals("ko")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getKo().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getEn().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
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

    private void resetAlphaSearch() {
        int i = 0;
        alpha = null;
        alpha = new String[listData.size()];
        if (lang.equals("ja")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getJa().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else if (lang.equals("ko")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getKo().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getEn().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        }
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup viewGroup) {
        String phrases;
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.phrases_item, null);

            holder.llWord = (RelativeLayout) view.findViewById(R.id.llWord);
            holder.tvViet = (TextView) view.findViewById(R.id.tvViet);
            holder.tvOther = (TextView) view.findViewById(R.id.tvOther);
            holder.btnSpeak = (Button) view.findViewById(R.id.btnSpeak);
            holder.imgSearch = (ImageView) view.findViewById(R.id.imgSearch);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // if (Locale.getDefault().getLanguage().equals("ja"))
        // holder.tvViet.setText(listData.get(position).getJa());
        // if (Locale.getDefault().getLanguage().equals("ko"))
        // holder.tvViet.setText(listData.get(position).getKo());
        // else
        // holder.tvViet.setText(listData.get(position).getEn());
//        ULog.i(PhrasesAdapter.class, "getview word:" + listData.get(position).getVi());
        phrases = String.format(listData.get(position).getVi(), "<u><font size=\"3\" color=\"blue\">"
                + listData.get(position).getDefault_word() + " </font></u>");
        holder.tvViet.setText(Html.fromHtml(phrases));
        if (lang.equals("ja"))
            holder.tvOther.setText(Html.fromHtml(listData.get(position).getJa()));
        else if (lang.equals("ko"))
            holder.tvOther.setText(Html.fromHtml(listData.get(position).getKo()));
        else
            holder.tvOther.setText(Html.fromHtml(listData.get(position).getEn()));

        if (listData.get(position).getDefault_word() != null && !listData.get(position).getDefault_word().trim().equals("")) {
            holder.llWord.setTag(listData.get(position).getDefault_word());
            holder.llWord.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ULog.i(PhrasesAdapter.class, "click tag:" + v.getTag());
                    if (activity.isClick)
                        return;
                    activity.isClick = true;
                    Intent i = new Intent(activity, SearchWordsActivity.class);
                    i.putExtra(Constant.INTENT_POSITION, position);
                    activity.startActivityForResult(i, PhrasesActivity.REQUEST_CODE_SEARCH);
                }
            });
            holder.imgSearch.setVisibility(View.VISIBLE);
        } else {
            holder.imgSearch.setVisibility(View.GONE);
            holder.llWord.setOnClickListener(null);
        }

        holder.btnSpeak.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String word;
                word = String.format(listData.get(position).getVi(), listData.get(position).getDefault_word());
                ULog.i(PhrasesAdapter.class, "onClick word:" + word);
                if (Constant.isPro)
                    audio.speakWord(word.toLowerCase().replaceAll("\\?", "").replaceAll("\\.", "").replaceAll("!", "").replaceAll(",", "").replaceAll("<u>", "").replaceAll("</u>", ""));
                else
                    Utility.installPremiumApp(activity);
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView tvViet;
        TextView tvOther;
        Button btnSpeak;
        ImageView imgSearch;
        RelativeLayout llWord;
    }

    // ///filter
    @SuppressLint("DefaultLocale")
    public void filter(String charText) {
        String word1, word2, wordVN;
        long number;
        tblViet tmp;

        try {
            charText = charText.toLowerCase(Locale.getDefault()).trim();
            ULog.i(PhrasesAdapter.this, "key: " + charText);
            listData.clear();
            if (charText.length() == 0) {
                ULog.i(PhrasesAdapter.class, "add all word");
                listData.addAll(listData2);
            } else {
                for (tblViet vi : listData2) {
                    wordVN = vi.getVi();
                    if (wordVN.contains(charText) || charText.contains(wordVN)) {
                        listData.add(vi);
                    } else if (lang.toLowerCase().equals("ja")) {
                        word1 = vi.getJa();
                        word2 = vi.getJa2();

                        if (word1.contains(charText) || charText.contains(word1)) {
                            listData.add(vi);
                        } else if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
                            listData.add(vi);
                        }
                    } else if (lang.toLowerCase().equals("ko")) {
                        word1 = vi.getKo().toLowerCase();
                        if (word1.contains(charText) || charText.contains(word1)) {
                            listData.add(vi);
                        }
                    } else {
                        word1 = vi.getEn().toLowerCase();
                        if (word1.contains(charText) || charText.contains(word1)) {
                            listData.add(vi);
                        }
                    }
                }
            }

            if (listData.size() == 0) {
                if (!charText.equals("")) {
                    number = Utility.convertToLong(charText);
                    if (number > -1) {
                        tmp = new tblViet();
                        tmp.setVi(NumberToWord.getWordFromNumber(number));
                        tmp.setEn(charText);
                        tmp.setJa(charText);
                        tmp.setJa2(charText);
                        tmp.setKo(charText);
                        listData.add(tmp);
                    }

                }
            } else
                resetAlphaSearch();

            notifyDataSetChanged();
        } catch (Exception e) {
            ULog.e(PhrasesAdapter.this, "filter error:" + e.getMessage());
        }
    }

    @Override
    public int getPositionForSection(int section) {
        return section;
    }

    @Override
    public int getSectionForPosition(int arg0) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return alpha;
    }

    public void setSlowly(boolean b) {
        audio.isSlowly = b;
    }
}