package teach.vietnam.asia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.Constant;

public class GrammarActivity extends BaseActivity {

    private ListView lstGrammar;
    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_grammar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lstGrammar = getViewChild(R.id.lstGrammar);
        initData();
    }

    private void initData(){
//        String [] arrData = getResources().getStringArray(R.array.arr_grammar);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arr_grammar, android.R.layout.simple_list_item_1);
        lstGrammar.setAdapter(adapter);
        lstGrammar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(GrammarActivity.this, GrammarDetailActivity.class);
                i.putExtra(Constant.INTENT_POSITION, position);
                GrammarActivity.this.startActivity(i);
            }
        });
    }

    @Override
    protected void reloadData() {

    }

}
