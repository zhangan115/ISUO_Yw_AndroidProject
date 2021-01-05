package com.isuo.yw2application.view.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.view.base.MvpFragment;
import com.sito.library.widget.PinnedHeaderExpandableListView1;

import java.util.List;

/**
 * 联系人
 * Created by zhangan on 2017/9/5.
 */

public class ContactFragment extends MvpFragment<ContactContract.Presenter> implements ContactContract.View {

    private PinnedHeaderExpandableListView1 mListView;
    private ContactListAdapter mContactAdapter;
    private RelativeLayout noDataLayout;

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ContactPresenter(Yw2Application.getInstance().getFaultRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_employee, container, false);
        mListView = rootView.findViewById(R.id.expandableListView);
        mContactAdapter = new ContactListAdapter(getActivity(), mListView
                , R.layout.item_employee_group, R.layout.item_employee_child_call);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getEmployee();
        }
    }

    @Override
    public void showData(List<DepartmentBean> list) {
        mContactAdapter.setData(list);
        mListView.setAdapter(mContactAdapter);
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
//        showEvLoading();
    }

    @Override
    public void hideLoading() {
//        hideEvLoading();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
