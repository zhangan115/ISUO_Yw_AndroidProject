package com.sito.evpro.inspection.view.employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加人员
 * Created by zhangan on 2017-06-26.
 */

public class EmployeeFragment extends MvpFragment<EmployeeContract.Presenter> implements EmployeeContract.View {

    private PinnedHeaderExpandableListView mListView;
    private EmployeeListAdapter mEmployeeListAdapter;
    private List<EmployeeBean> mChooseEmployeeBeen;
    private String[] userIds = null;
    private boolean isChooseUserSelf = true;
    private RelativeLayout noDataLayout;

    public static EmployeeFragment newInstance(@Nullable ArrayList<EmployeeBean> employeeBeen) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
        EmployeeFragment fragment = new EmployeeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static EmployeeFragment newInstance(@Nullable String[] employeeBeen) {
        Bundle args = new Bundle();
        args.putStringArray(ConstantStr.KEY_BUNDLE_LIST_1, employeeBeen);
        EmployeeFragment fragment = new EmployeeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mChooseEmployeeBeen = new ArrayList<>();
        ArrayList<EmployeeBean> employeeBeen = getArguments().getParcelableArrayList(ConstantStr.KEY_BUNDLE_LIST);
        userIds = getArguments().getStringArray(ConstantStr.KEY_BUNDLE_LIST_1);
        if (employeeBeen != null) {
            isChooseUserSelf = false;
            mChooseEmployeeBeen.addAll(employeeBeen);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sure, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sure) {
            Intent intent = new Intent();
            if (mEmployeeListAdapter != null && mEmployeeListAdapter.getSelectEmployee() != null) {
                intent.putExtra(ConstantStr.KEY_BUNDLE_LIST, mEmployeeListAdapter.getSelectEmployee());
                getActivity().setResult(Activity.RESULT_OK, intent);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_employee, container, false);
        mListView = rootView.findViewById(R.id.expandableListView);
        mEmployeeListAdapter = new EmployeeListAdapter(getActivity(), mListView, R.layout.item_employee_group
                , R.layout.item_employee_child, isChooseUserSelf);
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
    public void setPresenter(EmployeeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(List<DepartmentBean> list) {
        mEmployeeListAdapter.setData(list);
        if (userIds == null) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getUserList().size(); j++) {
                    for (int k = 0; k < mChooseEmployeeBeen.size(); k++) {
                        if (mChooseEmployeeBeen.get(k).getUser().getUserId() == list.get(i).getUserList().get(j).getUser().getUserId()) {
                            list.get(i).getUserList().get(j).setSelect(true);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getUserList().size(); j++) {
                    for (String userId : userIds) {
                        if (Long.valueOf(userId) == list.get(i).getUserList().get(j).getUser().getUserId()) {
                            list.get(i).getUserList().get(j).setSelect(true);
                        }
                    }
                }
            }
        }
        mListView.setAdapter(mEmployeeListAdapter);
    }

    @Override
    public void noData() {
        noDataLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        showEvLoading();
    }

    @Override
    public void hideLoading() {
        hideEvLoading();
    }
}
