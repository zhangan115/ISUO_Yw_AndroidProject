package com.sito.evpro.inspection.view.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * Created by zhangan on 2017/9/5.
 */

public class ContactFragment extends MvpFragment<ContactContract.Presenter> implements ContactContract.View {

    private PinnedHeaderExpandableListView mListView;
    private ContactListAdapter mContactAdapter;
    private RelativeLayout noDataLayout;

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_employee, container, false);
        mListView = (PinnedHeaderExpandableListView) rootView.findViewById(R.id.expandableListView);
        mContactAdapter = new ContactListAdapter(getActivity(), mListView, R.layout.item_employee_group, R.layout.item_employee_child_call);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_data);
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
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
