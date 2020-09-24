package com.isuo.yw2application.view.main.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.discover.ValueAddedBean;
import com.isuo.yw2application.view.base.MvpFragmentV4;
import com.sito.library.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends MvpFragmentV4<DataContract.Presenter> implements DataContract.View, View.OnClickListener {

    private ConvenientBanner convenientBanner;

    public static DataFragment newInstance() {
        Bundle args = new Bundle();
        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DataPresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        convenientBanner = rootView.findViewById(R.id.convenientBanner);
        List<Integer> defaultValue = new ArrayList<>();
        defaultValue.add(R.drawable.banner);
        convenientBanner.setPages(new CBViewHolderCreator<DefaultHolderView>() {
            @Override
            public DefaultHolderView createHolder() {
                return new DefaultHolderView();
            }
        }, defaultValue).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3500);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void showValueAddedData(List<ValueAddedBean.Data> listBean) {
        convenientBanner.setVisibility(View.VISIBLE);
        convenientBanner.setPages(new CBViewHolderCreator<ImageHolderView>() {
            @Override
            public ImageHolderView createHolder() {
                return new ImageHolderView();
            }
        }, listBean).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    @Override
    public void noData() {

    }

    @Override
    public void noValueAdded() {
        convenientBanner.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(DataContract.Presenter presenter) {
        mPresenter = presenter;
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
}
