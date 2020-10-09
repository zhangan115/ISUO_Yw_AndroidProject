package com.isuo.yw2application.view.main.work;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.BroadcastAction;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.isuo.yw2application.view.main.MainActivity;
import com.isuo.yw2application.view.main.work.all.WorkItemListActivity;
import com.isuo.yw2application.view.main.work.message.NewsListActivity;
import com.isuo.yw2application.view.main.work.sos.SOSActivity;
import com.isuo.yw2application.widget.WorkItemLayout;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.GlideUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class WorkFragment extends MvpFragmentV4<WorkContract.Presenter> implements WorkContract.View, View.OnClickListener {

    private ArrayList<WorkItem> workItemList;
    private ArrayList<WorkItem> showWorkItemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView userPhoto;

    public interface DrawClickCallBack {
        void onCallBack();
    }

    private DrawClickCallBack callBack;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            userPhotoUpdate();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            callBack = (DrawClickCallBack) context;
        }
    }

    public static WorkFragment newInstance() {
        Bundle args = new Bundle();
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workItemList = new ArrayList<>();
        showWorkItemList = new ArrayList<>();
        new WorkPresenter(Yw2Application.getInstance().getWorkRepositoryComponent().getRepository()
                , Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        getActivity().registerReceiver(receiver, new IntentFilter(BroadcastAction.USER_PHOTO_UPDATE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(findColorById(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getWorkItem();
                mPresenter.getNews();
            }
        });
        User user = Yw2Application.getInstance().getCurrentUser();
        TextView userNameTv = rootView.findViewById(R.id.userNameTv);
        userNameTv.setText(String.format("欢迎，%s", user.getRealName()));
        rootView.findViewById(R.id.rightImage).setOnClickListener(this);
        userPhoto = rootView.findViewById(R.id.leftImage);
        userPhoto.setOnClickListener(this);
        rootView.findViewById(R.id.workMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.alarmMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.enterpriseMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.abtMeMessageLayout).setOnClickListener(this);
        GlideUtils.ShowCircleImage(this.getActivity(), user.getPortraitUrl(), userPhoto, R.drawable.mine_head_default);
        return rootView;
    }

    private void userPhotoUpdate() {
        User user = Yw2Application.getInstance().getCurrentUser();
        if (userPhoto != null) {
            GlideUtils.ShowCircleImage(this.getActivity(), user.getPortraitUrl(), userPhoto, R.drawable.mine_head_default);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getWorkItem();
        mPresenter.getNews();
    }

    @Override
    public void showWorkItemList(List<WorkItem> workItems) {
        showWorkItemList.clear();
        showWorkItemList.addAll(workItems);
        workItemList.clear();
        workItemList.addAll(workItems);
        if (workItems.size() == 8 && getView() != null) {
            ((WorkItemLayout) getView().findViewById(R.id.workItem1)).setContent(workItems.get(0));
            ((WorkItemLayout) getView().findViewById(R.id.workItem2)).setContent(workItems.get(1));
            ((WorkItemLayout) getView().findViewById(R.id.workItem3)).setContent(workItems.get(2));
            ((WorkItemLayout) getView().findViewById(R.id.workItem4)).setContent(workItems.get(3));
            ((WorkItemLayout) getView().findViewById(R.id.workItem5)).setContent(workItems.get(4));
            ((WorkItemLayout) getView().findViewById(R.id.workItem6)).setContent(workItems.get(5));
            ((WorkItemLayout) getView().findViewById(R.id.workItem7)).setContent(workItems.get(6));
            ((WorkItemLayout) getView().findViewById(R.id.workItem8)).setContent(workItems.get(7));
            WorkItem workItem9 = new WorkItem(9, "紧急电话", R.drawable.emergency_call);
            WorkItem workItem10 = new WorkItem(10, "安全制度管理", R.drawable.security);
            ((WorkItemLayout) getView().findViewById(R.id.workItem9)).setContent(workItem9);
            ((WorkItemLayout) getView().findViewById(R.id.workItem10)).setContent(workItem10);
            getView().findViewById(R.id.workItem8).setOnClickListener(this);
            getView().findViewById(R.id.workItem9).setOnClickListener(this);
        }
    }

    @Override
    public void showWorkNews(MessageListBean bean) {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.workContentTv)).setText(bean
                    .getTitle());
            ((TextView) getView().findViewById(R.id.workContentTimeTv)).setText(getTimeStr(bean.getCreateTime()));
        }
    }

    @Override
    public void showAlarmNews(MessageListBean bean) {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.faultContentTv)).setText(bean
                    .getTitle());
            ((TextView) getView().findViewById(R.id.faultContentTimeTv)).setText(getTimeStr(bean.getCreateTime()));
        }
    }

    @Override
    public void showEnterpriseNews(MessageListBean bean) {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.enterpriseContentTv)).setText(bean
                    .getTitle());
            ((TextView) getView().findViewById(R.id.enterpriseContentTimeTv)).setText(getTimeStr(bean.getCreateTime()));
        }
    }

    @Override
    public void showMyNews(MessageListBean bean) {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.abtMeContentTv)).setText(bean
                    .getTitle());
            ((TextView) getView().findViewById(R.id.abtMeContentTimeTv)).setText(getTimeStr(bean.getCreateTime()));
        }
    }

    @Override
    public void requestFinish() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftImage:
                if (callBack != null) {
                    callBack.onCallBack();
                }
                break;
            case R.id.workMessageLayout:
            case R.id.alarmMessageLayout:
            case R.id.enterpriseMessageLayout:
            case R.id.abtMeMessageLayout:
                int type = Integer.parseInt((String) view.getTag());
                Intent intent = new Intent(getActivity(), NewsListActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, type);
                startActivity(intent);
                break;
            case R.id.workItem8:
                Intent workItemListInt = new Intent(getActivity(), WorkItemListActivity.class);
                workItemListInt.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, showWorkItemList);
                workItemListInt.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST_1, workItemList);
                startActivityForResult(workItemListInt, WORK_ITEM_CODE);
                break;
            case R.id.workItem9:
                startActivity(new Intent(getActivity(), SOSActivity.class));
                break;
            case R.id.workItem10:

                break;
        }
    }

    private int WORK_ITEM_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORK_ITEM_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<WorkItem> workItems = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            showWorkItemList(workItems);
//            if (workItems != null) {
//                mPresenter.saveWorkItem(workItems);
//            }
        }
    }

    private String getTimeStr(long time) {
        try {
            if (Utils.IsToday(DataUtil.timeFormat(time, null))) {
                return (DataUtil.timeFormat(time, "HH:mm"));
            } else if (Utils.IsYesterday(DataUtil.timeFormat(time, null))) {
                return ("昨天" + DataUtil.timeFormat(time, "HH:mm"));
            } else {
                return (DataUtil.timeFormat(time, "MM.dd HH:mm"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
