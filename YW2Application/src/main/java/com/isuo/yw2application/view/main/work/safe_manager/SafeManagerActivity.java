package com.isuo.yw2application.view.main.work.safe_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.data.StandInfoActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.List;

public class SafeManagerActivity extends BaseActivity implements SafeManagerContract.View {

    private SafeManagerContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private List<StandBean.ListBean> standBeen;
    private RelativeLayout layout_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        standBeen = new ArrayList<>();
        setLayoutAndToolbar(R.layout.activity_enterprise_standard,"安全管理制度");
        new SafeManagerPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(),this);
        layout_no_data = findViewById(R.id.layout_no_data);
        RecycleRefreshLoadLayout refreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        refreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        refreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.getSafeManagerList();
                }
            }
        });
        this.mRecyclerView = findViewById(R.id.recycleViewId);
        RVAdapter<StandBean.ListBean> adapter = new RVAdapter<StandBean.ListBean>(mRecyclerView, standBeen, R.layout.item_dis_stand) {
            @Override
            public void showData(ViewHolder vHolder, StandBean.ListBean data, int position) {
                TextView tvName = (TextView) vHolder.getView(R.id.id_stand_content);
                String str = (position + 1) + ". " + data.getRegulationName();
                tvName.setText(str);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SafeManagerActivity.this, StandInfoActivity.class);
                intent.putExtra(ConstantStr.KEY_TITLE, standBeen.get(position).getRegulationName());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, standBeen.get(position).getRegulationContent());
                startActivity(intent);
            }
        });
        mPresenter.getSafeManagerList();
    }

    @Override
    public void setPresenter(SafeManagerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showData(List<StandBean.ListBean> listBeen) {
        layout_no_data.setVisibility(View.GONE);
        standBeen.clear();
        standBeen.addAll(listBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        layout_no_data.setVisibility(View.VISIBLE);
    }
}

