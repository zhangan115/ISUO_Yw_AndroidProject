package com.sito.evpro.inspection.view.energy;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.mode.bean.energy.EnergyBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.widget.EnergyDialog;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.ExpendRecycleView;

import java.util.ArrayList;
import java.util.List;

public class EnergyListActivity extends BaseActivity {
    //view
    private RelativeLayout mNoDataLayout;
    private ExpendRecycleView mExpendRecycleView;

    private TextView mStation;
    private TextView mEnginNum;
    private View mHeaderView;

    //data
    private List<EnergyBean> mEnergyBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_energy_list);
        setLayoutAndToolbar(R.layout.activity_energy_list, "能量隔离");
        initView();
        initData();
    }

    private void initData() {
        mEnergyBeen = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            EnergyBean bean = new EnergyBean();
            bean.setmId(i + "");
            mEnergyBeen.add(bean);
        }
        RVAdapter<EnergyBean> adapter = new RVAdapter<EnergyBean>(mExpendRecycleView, mEnergyBeen, R.layout.item_energy_list) {
            @Override
            public void showData(ViewHolder vHolder, EnergyBean data, int position) {
                TextView mId = (TextView) vHolder.getView(R.id.id_energy_item_id);
                mId.setText(data.getmId());
                ImageView mImg = (ImageView) vHolder.getView(R.id.id_energy_item_img);
            }
        };
        mExpendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new EnergyDialog(EnergyListActivity.this, mEnergyBeen.get(position).getmId()) {

                    @Override
                    public void insulate() {
                        //隔离
                    }
                }.show();
            }
        });

        mStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> staionList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    staionList.add(i + "号变电站");
                }
                new MaterialDialog.Builder(EnergyListActivity.this)
                        .items(staionList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mStation.setText(staionList.get(position));
                            }
                        })
                        .show();
            }
        });
    }

    private void initView() {
//        mStation = (TextView) findViewById(R.id.id_energy_station);
//        mEnginNum = (TextView) findViewById(R.id.id_energy_num);
//        mStation.setText("一号配电室");
//        mEnginNum.setText("总电机数：150台");
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.energy_header, null);
        mStation = (TextView) mHeaderView.findViewById(R.id.id_energy_station);
        mEnginNum = (TextView) mHeaderView.findViewById(R.id.id_energy_num);
        mStation.setText("一号配电室");
        mEnginNum.setText("总电机数：150台");

        mExpendRecycleView = (ExpendRecycleView) findViewById(R.id.recycleViewId);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.layout_no_data);
        mExpendRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));

        mExpendRecycleView.addHeaderView(mHeaderView);

    }
}
