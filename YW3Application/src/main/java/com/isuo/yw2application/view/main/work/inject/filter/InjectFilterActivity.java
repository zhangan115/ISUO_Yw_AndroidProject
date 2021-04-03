package com.isuo.yw2application.view.main.work.inject.filter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.inject.InjectOilRepository;
import com.isuo.yw2application.mode.inject.bean.InjectRoomBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.inject.InjectActivity;
import com.isuo.yw2application.widget.treeadapter.Node;
import com.isuo.yw2application.widget.treeadapter.TreeAdapter;
import com.isuo.yw2application.widget.treeadapter.TreeItemClickListener;

import java.util.List;

/**
 * filter inject equipment
 * Created by zhangan on 2018/4/9.
 */

public class InjectFilterActivity extends BaseActivity implements InjectFilterContract.View {

    private RecyclerView recyclerView;
    private RelativeLayout noDataLayout;

    private InjectFilterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.inject_filter_acitivity, "筛选");
        new InjectFilterPresenter(InjectOilRepository.getRepository(this), this);
        recyclerView = findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noDataLayout = findViewById(R.id.layout_no_data);
        mPresenter.requestRoomList();
    }

    @Override
    public void showRoomList(List<InjectRoomBean> roomBeans) {
        noDataLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        final TreeAdapter<InjectRoomBean> adapter = new TreeAdapter<>(this, roomBeans);
        adapter.setListener(new TreeItemClickListener() {
            @Override
            public void OnClick(Node node) {
                Intent intent = new Intent(InjectFilterActivity.this, InjectActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, node.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void noData() {
        recyclerView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(InjectFilterContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
