package com.sito.customer.view.home.discover;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.discover.StandBean;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.view.MvpFragmentV4;
import com.sito.customer.view.count.alarm.DealAlarmCountActivity;
import com.sito.customer.view.count.work.WorkCountActivity;
import com.sito.customer.view.home.discover.equiplist.EquipListActivity;
import com.sito.customer.view.home.discover.faultreport.FaultReportActivity;
import com.sito.customer.view.home.discover.faulttype.FaultTypeActivity;
import com.sito.customer.view.home.discover.staffcount.StaffCountActivity;
import com.sito.customer.view.home.discover.statistics.part.StatisticsPartActivity;
import com.sito.customer.view.home.discover.statistics.person.StatisticsPersonActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 */
public class DiscoverFragment extends MvpFragmentV4<DiscoverContract.Presenter> implements DiscoverContract.View, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayout mLlShowStand;
    private List<StandBean.ListBean> standBeen;
    private ConvenientBanner dataBanner;

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DiscoverPresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(rootView);
        initListener(rootView);
        mPresenter.getStandInfo();
        mPresenter.getValueAdded();
        return rootView;
    }

    private void initView(View rootView) {
        mLlShowStand = rootView.findViewById(R.id.ll_show_stand);
        mRecyclerView = rootView.findViewById(R.id.recycleViewId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        standBeen = new ArrayList<>();
        RVAdapter<StandBean.ListBean> adapter = new RVAdapter<StandBean.ListBean>(mRecyclerView, standBeen, R.layout.item_dis_stand) {
            @Override
            public void showData(ViewHolder vHolder, StandBean.ListBean data, int position) {
                TextView tvName = (TextView) vHolder.getView(R.id.id_stand_content);
                String str = (position + 1) + "." + data.getRegulationName();
                tvName.setText(str);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), StandInfoActivity.class);
                intent.putExtra(ConstantStr.KEY_TITLE, standBeen.get(position).getRegulationName());
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR, standBeen.get(position).getRegulationContent());
                startActivity(intent);
            }
        });

        dataBanner = rootView.findViewById(R.id.convenientBanner);
        List<Integer> defaultValue = new ArrayList<>();
        defaultValue.add(R.drawable.picture_default);
        dataBanner.setPages(new CBViewHolderCreator<DefaultHolderView>() {
            @Override
            public DefaultHolderView createHolder() {
                return new DefaultHolderView();
            }
        }, defaultValue).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    private void initListener(View view) {
        view.findViewById(R.id.id_dis_get).setOnClickListener(this);
        view.findViewById(R.id.id_dis_report).setOnClickListener(this);
        view.findViewById(R.id.id_dis_work).setOnClickListener(this);
        view.findViewById(R.id.id_dis_badtype).setOnClickListener(this);
        view.findViewById(R.id.id_dis_baddeal).setOnClickListener(this);
        view.findViewById(R.id.id_dis_equip).setOnClickListener(this);
        view.findViewById(R.id.llPart).setOnClickListener(this);
        view.findViewById(R.id.llPerson).setOnClickListener(this);
    }

    @Override
    public void setPresenter(DiscoverContract.Presenter presenter) {
        mPresenter = presenter;
    }


    public class ImageHolderView implements Holder<ValueAddedBean.Data> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, ValueAddedBean.Data data) {
            GlideUtils.ShowImage(context, data.getIconUrl(), imageView, R.drawable.picture_default);
        }
    }

    public class DefaultHolderView implements Holder<Integer> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageDrawable(findDrawById(data));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dataBanner.startTurning(3500);
    }

    @Override
    public void onPause() {
        super.onPause();
        dataBanner.stopTurning();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dis_get:
                //人员到岗
                startActivity(new Intent(getContext(), StaffCountActivity.class));
                break;
            case R.id.id_dis_report:
                //故障上报
                startActivity(new Intent(getContext(), FaultReportActivity.class));
                break;
            case R.id.id_dis_work:
                //工作量
                startActivity(new Intent(getContext(), WorkCountActivity.class));
                break;
            case R.id.id_dis_badtype:
                //故障类型
                startActivity(new Intent(getActivity(), FaultTypeActivity.class));
                break;
            case R.id.id_dis_baddeal:
                //故障处理
                startActivity(new Intent(getContext(), DealAlarmCountActivity.class));
                break;
            case R.id.id_dis_equip:
                //设备台账
                Intent intent = new Intent(getActivity(), EquipListActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, true);
                startActivity(intent);
                break;
            case R.id.llPart:
                startActivity(new Intent(getActivity(), StatisticsPartActivity.class));
                break;
            case R.id.llPerson:
                startActivity(new Intent(getActivity(), StatisticsPersonActivity.class));
                break;
        }
    }

    @Override
    public void showData(List<StandBean.ListBean> listBeen) {
        mLlShowStand.setVisibility(View.VISIBLE);
        standBeen.clear();
        standBeen.addAll(listBeen);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showValueAddedData(final List<ValueAddedBean.Data> listBean) {
        dataBanner.setPages(new CBViewHolderCreator<ImageHolderView>() {
            @Override
            public ImageHolderView createHolder() {
                return new ImageHolderView();
            }
        }, listBean).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        dataBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), StandInfoActivity.class);
                intent.putExtra(ConstantStr.KEY_TITLE, listBean.get(position).getValueAddedTitle());
                if (!TextUtils.isEmpty(listBean.get(position).getValueUrl())) {
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, listBean.get(position).getValueUrl());
                } else {
                    intent.putExtra(ConstantStr.KEY_BUNDLE_STR, listBean.get(position).getValueAddedContent());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void noData() {
        mLlShowStand.setVisibility(View.GONE);
    }

    @Override
    public void noValueAdded() {

    }
}
