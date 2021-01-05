package com.sito.customer.view.home.work.inspection.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.work.InspectionDataBean;
import com.sito.customer.view.MvpFragment;
import com.sito.library.widget.PinnedHeaderExpandableListView;

/**
 * 巡检数据
 * Created by zhangan on 2017/9/27.
 */

public class InspectionDataFragment extends MvpFragment<InspectionDataContract.Presenter> implements InspectionDataContract.View {

    private Long id;
    private RelativeLayout mNoDataLayout;
    private PinnedHeaderExpandableListView mListView;
    private InspectionDataContract.Presenter mPresenter;

    public static InspectionDataFragment newInstance(Long id) {
        Bundle args = new Bundle();
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, id);
        InspectionDataFragment fragment = new InspectionDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new InspectionDataPresenter(CustomerApp.getInstance().getWorkRepositoryComponent().getRepository(), this);
        this.id = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inspection_data, container, false);
        mListView = rootView.findViewById(R.id.pinnedView);
        mNoDataLayout = rootView.findViewById(R.id.layout_no_data);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getInspectionData(id);
    }

    @Override
    public void setPresenter(InspectionDataContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void showInspectionData(InspectionDataBean dataBean) {
        mListView.setAdapter(new DataAdapter(getActivity(), dataBean.getRoomList()));
    }

    @Override
    public void noData() {
        mNoDataLayout.setVisibility(View.VISIBLE);
    }
}
