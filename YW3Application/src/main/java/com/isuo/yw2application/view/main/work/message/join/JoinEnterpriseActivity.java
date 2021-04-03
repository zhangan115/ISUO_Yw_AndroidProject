package com.isuo.yw2application.view.main.work.message.join;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class JoinEnterpriseActivity extends BaseActivity implements JoinEnterpriseContract.View {

    private JoinEnterpriseContract.Presenter mPresenter;
    private List<User> dataList;
    private RecyclerView mRecyclerView;
    private RelativeLayout layout_no_data;
    private RecycleRefreshLoadLayout refreshLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_enterprise_standard, "人员申请");
        dataList = new ArrayList<>();
        new JoinEnterprisePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        layout_no_data = findViewById(R.id.layout_no_data);
        refreshLoadLayout = findViewById(R.id.refreshLoadLayoutId);
        refreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        refreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    layout_no_data.setVisibility(View.GONE);
                    mPresenter.getPlayJoinList();
                }
            }
        });
        this.mRecyclerView = findViewById(R.id.recycleViewId);
        RVAdapter<User> adapter = new RVAdapter<User>(mRecyclerView, dataList, R.layout.item_join_enterprise) {
            @Override
            public void showData(RVAdapter.ViewHolder vHolder, final User data, int position) {
                TextView tvContent = (TextView) vHolder.getView(R.id.contentTv);
                TextView tvTime = (TextView) vHolder.getView(R.id.joinTimeTv);
                tvContent.setText(MessageFormat.format("{0}申请加入公司(手机号码:{1})", data.getRealName(), data.getUserPhone()));
                tvTime.setText(MessageFormat.format("申请时间:{0}", DataUtil.timeFormat(data.getCreateTime(), null)));
                Button btn = (Button) vHolder.getView(R.id.joinBtn);
                btn.setTag(position);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int position = (int) view.getTag();
                        new MaterialDialog.Builder(JoinEnterpriseActivity.this)
                                .content("是否同意" + data.getRealName() + "(手机号码:" + data.getUserPhone() + ")加入公司?")
                                .negativeText(R.string.cancel)
                                .positiveText(R.string.sure)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        mPresenter.agreeJoin(dataList.get(position).getUserId());
                                    }
                                }).show();
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
        if (mPresenter != null) {
            mPresenter.getPlayJoinList();
        }
    }

    @Override
    public void showPlayJoinList(List<User> list) {
        refreshLoadLayout.setRefreshing(false);
        layout_no_data.setVisibility(View.GONE);
        dataList.clear();
        dataList.addAll(list);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        refreshLoadLayout.setRefreshing(false);
        layout_no_data.setVisibility(View.VISIBLE);
    }

    @Override
    public void agreeSuccess() {
        if (mPresenter != null) {
            layout_no_data.setVisibility(View.GONE);
            mPresenter.getPlayJoinList();
        }
    }

    @Override
    public void setPresenter(JoinEnterpriseContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
