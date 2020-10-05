package com.isuo.yw2application.view.main.adduser;

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

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.view.base.MvpFragment;
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
    private RelativeLayout noDataLayout;
    private boolean isRepair, isChooseOne;
    private String[] chooseUserIds;

    public static EmployeeFragment newInstance(@Nullable ArrayList<EmployeeBean> employeeBeen
            , boolean isRepair, boolean isChooseOne, String[] userId) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantStr.KEY_BUNDLE_LIST, employeeBeen);
        args.putBoolean(ConstantStr.KEY_BUNDLE_BOOLEAN, isRepair);
        args.putBoolean(ConstantStr.KEY_BUNDLE_BOOLEAN_1, isChooseOne);
        args.putStringArray(ConstantStr.KEY_BUNDLE_LIST_1, userId);
        EmployeeFragment fragment = new EmployeeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new EmployeePresenter(Yw2Application.getInstance().getFaultRepositoryComponent().getRepository(), this);
        setHasOptionsMenu(true);
        mChooseEmployeeBeen = new ArrayList<>();
        ArrayList<EmployeeBean> employeeBeen = getArguments().getParcelableArrayList(ConstantStr.KEY_BUNDLE_LIST);
        isRepair = getArguments().getBoolean(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        isChooseOne = getArguments().getBoolean(ConstantStr.KEY_BUNDLE_BOOLEAN_1, false);
        chooseUserIds = getArguments().getStringArray(ConstantStr.KEY_BUNDLE_LIST_1);
        if (employeeBeen != null) {
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
        mEmployeeListAdapter = new EmployeeListAdapter(getActivity()
                , R.layout.item_employee_group
                , R.layout.item_employee_child
                , isChooseOne);
        noDataLayout = rootView.findViewById(R.id.layout_no_data);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getEmployee(isRepair);
        }
    }

    @Override
    public void setPresenter(EmployeeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(List<DepartmentBean> list) {
        mEmployeeListAdapter.setData(list);
        if (chooseUserIds != null) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getUserList().size(); j++) {
                    for (String chooseUserId : chooseUserIds) {
                        if (chooseUserId.equals(String.valueOf(list.get(i).getUserList().get(j).getUser().getUserId()))) {
                            list.get(i).getUserList().get(j).setSelect(true);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getUserList().size(); j++) {
                    for (int k = 0; k < mChooseEmployeeBeen.size(); k++) {
                        if (mChooseEmployeeBeen.get(k).getUser().getUserId() == list.get(i).getUserList().get(j).getUser().getUserId()) {
                            list.get(i).getUserList().get(j).setSelect(true);
                        }
                    }
                }
            }
        }
        mEmployeeListAdapter.setCanChooseMySelf(chooseUserIds != null);
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
