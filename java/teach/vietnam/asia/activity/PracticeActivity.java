package teach.vietnam.asia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Utility;

public class PracticeActivity extends BaseActivity implements OnClickListener {

	@Override
	protected int getViewLayoutId() {
		return R.layout.practice_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setInitData();

        Utility.setScreenNameGA("PracticeActivity");

    }

    @Override
    protected void reloadData() {

    }

    @Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btnFruit:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 1);
			startActivity(i);
			break;
		case R.id.btnVegetables:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 2);
			startActivity(i);
			break;
		case R.id.btnFood:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 3);
			startActivity(i);
			break;
		case R.id.btnAnimal:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 4);
			startActivity(i);
			break;
		case R.id.btnFurniture:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 5);
			startActivity(i);
			break;
		case R.id.btnOther:
			i = new Intent(PracticeActivity.this, PracticeDetailActivity.class);
			i.putExtra(Constant.INTENT_KIND, 12);
			startActivity(i);
			break;
		}
		super.onClick(v);

	}

	private void setInitData() {

		setListenerView(R.id.btnFruit, this);
		setListenerView(R.id.btnVegetables, this);
		setListenerView(R.id.btnFood, this);
		setListenerView(R.id.btnAnimal, this);
		setListenerView(R.id.btnFurniture, this);
		setListenerView(R.id.btnOther, this);

	}

}
