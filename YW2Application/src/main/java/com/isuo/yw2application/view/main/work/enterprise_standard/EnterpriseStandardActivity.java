package com.isuo.yw2application.view.main.work.enterprise_standard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class EnterpriseStandardActivity extends BaseActivity implements EnterpriseStandardContact.View {

    EnterpriseStandardContact.Presenter mPresenter;
    private final List<StandBean.ListBean> standBeen = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_enterprise_standard, "查看规范");
        new EnterpriseStandardPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        RecycleRefreshLoadLayout refreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        refreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        refreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.getEnterpriseStandardList();
                }
            }
        });
        this.mRecyclerView = findViewById(R.id.recycleViewId);
        RVAdapter<StandBean.ListBean> adapter = new RVAdapter<StandBean.ListBean>(mRecyclerView, standBeen, R.layout.item_dis_stand) {
            @Override
            public void showData(ViewHolder vHolder, StandBean.ListBean data, int position) {
                TextView tvName = (TextView) vHolder.getView(R.id.id_stand_content);
                String str = (position + 1) + "." + data.getRegulationName();
                tvName.setText(str);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(EnterpriseStandardActivity.this, StandInfoActivity.class);
                intent.putExtra(ConstantStr.KEY_TITLE, standBeen.get(position).getRegulationName());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, standBeen.get(position).getRegulationContent());
                startActivity(intent);
            }
        });
        if (mPresenter != null) {
            mPresenter.getEnterpriseStandardList();
        }
    }


    @Override
    public void setPresenter(EnterpriseStandardContact.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showData(List<StandBean.ListBean> listBeen) {
        standBeen.clear();
        standBeen.addAll(listBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {

    }
}
