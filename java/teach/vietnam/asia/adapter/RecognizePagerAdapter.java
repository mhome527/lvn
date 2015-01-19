package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.LearnWordsActivity;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.PracticeDetailEntity;
import teach.vietnam.asia.entity.tblRecognize;
import teach.vietnam.asia.entity.tblRecognizeDao;
import teach.vietnam.asia.utils.ULog;

public class RecognizePagerAdapter extends PagerAdapter {

    private Activity activity;
    public ArrayList<PracticeDetailEntity> lstExceriese;
    private DaoMaster daoMaster;
    private tblRecognizeDao dao;
    private int num;
    private int arrW[];


    public RecognizePagerAdapter(Activity activity, int num) {
        lstExceriese = new ArrayList<PracticeDetailEntity>();
        this.num = num;
        arrW = new int[num];
        this.activity = activity;
        // Log.i("HomeAdapter", "HomePagerAdapter...");
    }

    @Override
    public int getCount() {
        return num;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @SuppressLint("InflateParams")
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.recognize_list, null);

        ListView lstRecognize = (ListView) view.findViewById(R.id.lstRecognize);

        new LoadData(lstRecognize, position).execute();
        view.setTag(position);
        ((ViewPager) collection).addView(view, 0);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    ///// load data
    private class LoadData extends AsyncTask<Void, Void, Void> {

        private int pos = 0;
        private ListView lstRecognize;
        private List<tblRecognize> dataRecognize;

        public LoadData(ListView lstRecognize , int pos) {
            this.pos = pos;
            this.lstRecognize = lstRecognize;
        }

        @Override
        protected Void doInBackground(Void... params) {
            QueryBuilder<tblRecognize> qb;
            String lang;
            try {
                daoMaster = ((MyApplication) activity.getApplication()).daoMaster;
                dao = daoMaster.newSession().getTblRecognizeDao();
                qb = dao.queryBuilder();
//                lang = activity.getString(R.string.language);


                qb.where(tblRecognizeDao.Properties.Group_id.eq(pos + 1));
//                if (lang.equals("ja")) {
//                    qb.where(tblRecognizeDao.Properties.Group_id.eq(pos));
//                } else if (lang.equals("ko")) {
//                    qb.where(tblRecognizeDao.Properties.Group_id.eq(pos));
//                } else {
//                    qb.where(tblRecognizeDao.Properties.Group_id.eq(pos));
//                }


                ULog.i(this, "===data db:" + qb.list().size());
                dataRecognize = qb.list();
            } catch (Exception e) {
                ULog.e(LearnWordsActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecognizeListAdapter adapter = new RecognizeListAdapter(activity, dataRecognize, pos);
            lstRecognize.setAdapter(adapter);
        }
    }


}
