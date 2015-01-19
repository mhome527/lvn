package teach.vietnam.asia.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected abstract int getViewLayoutId();
    protected abstract void initView(View view);
    private View root;
    protected OnFragmentInteractionListener mListener;


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Class cls, int currPage);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root =  inflater.inflate(getViewLayoutId(), container, false);
        initView(root);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

//    @Override
//    public void onClick(View view) {
//
//    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getViewChild(int id) {
        return (V) root.findViewById(id);
    }

    public void setListenerView(int id) {
        View v = getViewChild(id);
        v.setOnClickListener(this);
    }

    public void startFragment(Class cls, int currPage){
        mListener.onFragmentInteraction(cls, currPage);
    }
}
