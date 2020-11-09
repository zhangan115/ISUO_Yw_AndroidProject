package com.isuo.yw2application.view.main.work.message;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.db.NewsBean;
import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.utils.NewsUtils;
import com.isuo.yw2application.view.base.LazyLoadFragmentV4;
import com.isuo.yw2application.view.main.alarm.detail.AlarmDetailActivity;
import com.isuo.yw2application.view.main.task.increment.WorkIncrementActivity;
import com.isuo.yw2application.view.main.task.increment.detail.IncrementDetailActivity;
import com.isuo.yw2application.view.main.task.inspection.WorkInspectionActivity;
import com.isuo.yw2application.view.main.task.inspection.detial.InspectDetailActivity;
import com.isuo.yw2application.view.main.task.overhaul.WorkOverhaulActivity;
import com.isuo.yw2application.view.main.task.overhaul.detail.OverhaulDetailActivity;
import com.isuo.yw2application.view.main.work.enterprise.EnterpriseActivity;
import com.isuo.yw2application.view.main.work.inject.InjectActivity;
import com.isuo.yw2application.view.main.work.safe.NewsSafeActivity;
import com.isuo.yw2application.widget.MessageItemLayout;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.adapter.VRVAdapter;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示消息内容
 * Created by zhangan on 2017/10/16.
 */

public class NewsListFragment extends LazyLoadFragmentV4<NewsListContract.Presenter> implements NewsListContract.View
        , SwipeRefreshLayout.OnRefreshListener, RecycleRefreshLoadLayout.OnLoadListener {

    private int type;
    private boolean isRefresh;
    private Map<String, String> requestMap;
    private List<MessageListBean> messageListBeans;
    private ExpendRecycleView expendRecycleView;
    private RelativeLayout noDataLayout;
    private ReceiverNewMessage receiverNewMessage;
    private RecycleRefreshLoadLayout recycleRefreshLoadLayout;

    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ConstantStr.KEY_BUNDLE_INT, type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NewsListPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        type = getArguments().getInt(ConstantStr.KEY_BUNDLE_INT);
        messageListBeans = new ArrayList<>();
        receiverNewMessage = new ReceiverNewMessage();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.MESSAGE_UN_READ_STATE);
        filter.addAction(BroadcastAction.MESSAGE_NEW_MESSAGE);
        getActivity().registerReceiver(receiverNewMessage, filter);
        requestMap = new HashMap<>();
        requestMap.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        requestMap.put("messageType", String.valueOf(type + 1));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_news_list, container, false);
        recycleRefreshLoadLayout = rootView.findViewById(R.id.refreshLoadLayoutId);
        recycleRefreshLoadLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        recycleRefreshLoadLayout.setOnRefreshListener(this);
        recycleRefreshLoadLayout.setOnLoadListener(this);
        @SuppressLint("InflateParams")
        View loadFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more_inspection, null);
        recycleRefreshLoadLayout.setViewFooter(loadFooterView);
        expendRecycleView = rootView.findViewById(R.id.recycleViewId);
        expendRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        expendRecycleView.setNestedScrollingEnabled(false);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        final RVAdapter<MessageListBean> adapter = new RVAdapter<MessageListBean>(expendRecycleView, messageListBeans, R.layout.item_news_list) {
            @Override
            public void showData(ViewHolder vHolder, MessageListBean data, int position) {
                TextView title = (TextView) vHolder.getView(R.id.id_news_title);
                LinearLayout itemLayout = (LinearLayout) vHolder.getView(R.id.llItems);
                title.setText(data.getTitle());
                itemLayout.removeAllViews();
                for (int i = 0; i < data.getMessageItemList().size(); i++) {
                    MessageItemLayout layout = new MessageItemLayout(getActivity());
                    layout.setData(data.getMessageItemList().get(i).getSmallType(), data.getMessageItemList().get(i).getContent()
                            , data.getMessageItemList().get(i).getCreateTime());
                    itemLayout.addView(layout);
                }
            }
        };
        VRVAdapter<MessageListBean> rvAdapter = new VRVAdapter<MessageListBean>(expendRecycleView,messageListBeans,new int[]{R.layout.item_news_list}) {

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public void showData(ViewHolder vHolder, MessageListBean data, int position, int type) {

            }
        };
        expendRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (messageListBeans.get(position).getMessageItemList().size() > 0) {
                    for (int i = 0; i < messageListBeans.get(position).getMessageItemList().size(); i++) {
                        int type = messageListBeans.get(position).getMessageItemList().get(i).getSmallType();
                        if (type == 401 || type == 402) {
                            Intent toSafe = new Intent(getActivity(), NewsSafeActivity.class);
                            if (messageListBeans.get(position).getMessageItemList().size() == 1) {
                                toSafe.putExtra(ConstantStr.KEY_BUNDLE_LONG, messageListBeans.get(position).getMessageId());
                                startActivity(toSafe);
                            }
                            break;
                        } else if (type == 101 || type == 102 || type == 103 || type == 104) {
                            Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, NewsListFragment.this.type == 3);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(messageListBeans.get(position).getTaskId()));
                            startActivity(intent);
                            break;
                        } else if (type == 201 || type == 202 || type == 203) {
                            Intent intent = new Intent(getActivity(), InspectDetailActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, messageListBeans.get(position).getTaskId());
                            startActivity(intent);
                            break;
                        } else if (type == 204 || type == 205 || type == 206) {
                            Intent intent = new Intent(getActivity(), OverhaulDetailActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, messageListBeans.get(position).getTaskId());
                            startActivity(intent);
                            break;
                        } else if (type == 207 || type == 208 || type == 209) {
                            Intent intent = new Intent(getActivity(), IncrementDetailActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, messageListBeans.get(position).getTaskId());
                            startActivity(intent);
                            break;
                        } else if (type == 601) {
                            new MaterialDialog.Builder(getActivity())
                                    .content("是否确定借用该工具")
                                    .negativeText("不借了")
                                    .positiveText("确定借用")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            if (mPresenter != null) {
                                                mPresenter.borrowSure(messageListBeans.get(position).getTaskId(), 1);
                                            }
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            if (mPresenter != null) {
                                                mPresenter.borrowSure(messageListBeans.get(position).getTaskId(), 0);
                                            }
                                        }
                                    })
                                    .show();
                            break;
                        } else if (type == 602) {
                            break;
                        } else if (type == 701) {
                            startActivity(new Intent(getActivity(), InjectActivity.class));
                            break;
                        } else if (type == 301) {
                            Intent intent = new Intent(getActivity(), EnterpriseActivity.class);
                            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, Long.valueOf(messageListBeans.get(position).getTaskId()));
                            startActivity(intent);
                        } else if (type == 501) {
                            startActivity(new Intent(getActivity(), WorkInspectionActivity.class));
                        } else if (type == 502) {
                            startActivity(new Intent(getActivity(), WorkOverhaulActivity.class));
                        } else if (type == 503) {
                            startActivity(new Intent(getActivity(), WorkIncrementActivity.class));
                        }
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void setPresenter(NewsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessage(List<NewsBean> list) {
        noDataLayout.setVisibility(View.GONE);
        expendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void noData() {
        expendRecycleView.getAdapter().notifyDataSetChanged();
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        recycleRefreshLoadLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isRefresh = false;
        recycleRefreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void noMoreData() {
        recycleRefreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void loadMoreFinish() {
        recycleRefreshLoadLayout.loadFinish();
    }

    @Override
    public void showMessageList(List<MessageListBean> list) {
        noDataLayout.setVisibility(View.GONE);
        messageListBeans.clear();
        messageListBeans.addAll(list);
        expendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMessageListMore(List<MessageListBean> list) {
        messageListBeans.addAll(list);
        expendRecycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void borrowSureSuccess() {
        Yw2Application.getInstance().showToast("操作成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (receiverNewMessage != null) {
                getActivity().unregisterReceiver(receiverNewMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestData() {
        if (mPresenter != null) {
            mPresenter.getMessageList(requestMap);
        }
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            isRefresh = true;
            if (requestMap.containsKey("lastId")) {
                requestMap.remove("lastId");
            }
            recycleRefreshLoadLayout.setNoMoreData(false);
            mPresenter.getMessageList(requestMap);
        }
    }

    @Override
    public void onLoadMore() {
        if (messageListBeans.size() == 0 || isRefresh || mPresenter == null) {
            return;
        }
        requestMap.put("lastId", String.valueOf(messageListBeans.get(messageListBeans.size() - 1).getMessageId()));
        mPresenter.getMessageListMore(requestMap);
    }

    private class ReceiverNewMessage extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), BroadcastAction.MESSAGE_NEW_MESSAGE)) {
                NewsBean newsBean = intent.getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
                if (newsBean != null) {
                    if (NewsUtils.getNewsIntent(newsBean) == type) {
                        onRefresh();
                    }
                }
            }
        }
    }
}
