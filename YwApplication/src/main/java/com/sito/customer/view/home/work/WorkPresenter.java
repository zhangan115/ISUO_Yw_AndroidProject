package com.sito.customer.view.home.work;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.mode.bean.work.WorkState;
import com.sito.customer.mode.work.WorkDataSource;
import com.sito.customer.widget.CountWorkLayout;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 工作
 * Created by Administrator on 2017/6/28.
 */
class WorkPresenter implements WorkContract.Presenter {
    private WorkDataSource mRepository;
    private WorkContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    WorkPresenter(WorkDataSource repository, WorkContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getWorkItem() {
        mRepository.getWorkItems(new WorkDataSource.WorkItemCallBack() {
            @Override
            public void showWorkItem(List<WorkItem> workItems) {
                mView.showWorkItemList(workItems);
            }

            @Override
            public void showAllWorkItem(List<WorkItem> workItems) {
                mView.showAllWorkItemList(workItems);
            }
        });
    }

    @Override
    public void getWorkCount() {
        subscription.add(mRepository.getWorkState(new IObjectCallBack<WorkState>() {
            @Override
            public void onSuccess(@NonNull WorkState workState) {
                List<CountWorkLayout.CountWorkBean> countWorkBeans = new ArrayList<>();
                CountWorkLayout.CountWorkBean bean1 = new CountWorkLayout.CountWorkBean();
                bean1.setTitle("组织固定任务");
                bean1.setType(1);
                List<CountWorkLayout.CountWorkBean.CountTypeBean> typeBeans = new ArrayList<>();
                typeBeans.add(new CountWorkLayout.CountWorkBean.CountTypeBean(1, "本月", workState.getMonthFinishCount()
                        , workState.getMonthAllCount()));
                typeBeans.add(new CountWorkLayout.CountWorkBean.CountTypeBean(2, "本周", workState.getWeekFinishCount(),
                        workState.getWeekAllCount()));
                typeBeans.add(new CountWorkLayout.CountWorkBean.CountTypeBean(3, "今日", workState.getDayFinishCount(),
                        workState.getDayAllCount()));
                bean1.setCountTypeBeans(typeBeans);
                countWorkBeans.add(bean1);
                CountWorkLayout.CountWorkBean bean2 = new CountWorkLayout.CountWorkBean();
                bean2.setTitle("今日发生");
                bean2.setType(2);
                List<CountWorkLayout.CountWorkBean.CountTypeBean> typeBeans2 = new ArrayList<>();
                typeBeans2.add(new CountWorkLayout.CountWorkBean.CountTypeBean(4, "待办", workState.getTodoFinishCount(),
                        workState.getTodoAllCount()));
                typeBeans2.add(new CountWorkLayout.CountWorkBean.CountTypeBean(5, "故障", workState.getFaultFinishCount(),
                        workState.getFaultAllCount()));
                typeBeans2.add(new CountWorkLayout.CountWorkBean.CountTypeBean(6, "专项工作", workState.getIncrFinishCount(),
                        workState.getIncrAllCount()));
                bean2.setCountTypeBeans(typeBeans2);
                countWorkBeans.add(bean2);

                CountWorkLayout.CountWorkBean bean3 = new CountWorkLayout.CountWorkBean();
                bean3.setTitle("遗留问题");
                bean3.setType(3);
                List<CountWorkLayout.CountWorkBean.CountTypeBean> typeBeans3 = new ArrayList<>();
                typeBeans3.add(new CountWorkLayout.CountWorkBean.CountTypeBean(7, "故障", workState.getRelicFaultCount(), -1));
                typeBeans3.add(new CountWorkLayout.CountWorkBean.CountTypeBean(8, "关注设备", workState.getRelicFocusEquipCount(), -1));
                typeBeans3.add(new CountWorkLayout.CountWorkBean.CountTypeBean(9, "专项工作", workState.getRelicIncrCount(), -1));
                bean3.setCountTypeBeans(typeBeans3);
                countWorkBeans.add(bean3);
                mView.showWorkCount(countWorkBeans);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void saveWorkItem(ArrayList<WorkItem> workItems) {
        mRepository.saveWorkItems(workItems);
    }
}
