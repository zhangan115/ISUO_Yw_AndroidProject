package com.sito.customer.view.equip.time.detail;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.equip.EquipRecordDetail;
import com.sito.customer.mode.bean.equip.TimeLineBean;
import com.sito.customer.view.MvpFragment;
import com.sito.customer.view.photo.ViewPagePhotoActivity;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * * 检测详情,大修详情,实验详情
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentRecordDetailFragment extends MvpFragment<EquipmentRecordDetailContract.Presenter> implements EquipmentRecordDetailContract.View, EasyPermissions.PermissionCallbacks {

    private TimeLineBean timeLineBean;
    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EvPro/";

    public static EquipmentRecordDetailFragment newInstance(TimeLineBean timeLineBean) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantStr.KEY_BUNDLE_OBJECT, timeLineBean);
        EquipmentRecordDetailFragment fragment = new EquipmentRecordDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RecordDetailPresenter(CustomerApp.getInstance().getEquipmentRepositoryComponent().getRepository(), this);
        timeLineBean = getArguments().getParcelable(ConstantStr.KEY_BUNDLE_OBJECT);
        checkPermission();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmg_equip_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.getRecordDetail(timeLineBean.getEquipmentRecordId());
        }
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
    public void showData(EquipRecordDetail equipRecordDetail) {
        if (getView() == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.tv_name)).setText(equipRecordDetail.getStaffName());
        ((TextView) getView().findViewById(R.id.tv_time)).setText(DataUtil.timeFormat(timeLineBean.getCreateTime(), null));
        ((TextView) getView().findViewById(R.id.tv_equip_name)).setText(equipRecordDetail.getRecordName());
        TextView contentTitleTv = (TextView) getView().findViewById(R.id.tv_record_title);
        if (timeLineBean.getType() == ConstantInt.TYPE_REPAIR) {
            contentTitleTv.setText("大修内容");
        } else if (timeLineBean.getType() == ConstantInt.TYPE_CHECK) {
            contentTitleTv.setText("检测内容");
        } else if (timeLineBean.getType() == ConstantInt.TYPE_EXPERIMENT) {
            contentTitleTv.setText("实验内容简要");
        }
        ((TextView) getView().findViewById(R.id.record_content)).setText(equipRecordDetail.getRecordContent());
        LinearLayout photo_content = (LinearLayout) getView().findViewById(R.id.photo_content);
        final String[] images = new String[equipRecordDetail.getRecordImages().size()];
        for (int i = 0; i < equipRecordDetail.getRecordImages().size(); i++) {
            images[i] = equipRecordDetail.getRecordImages().get(i).getFileUrl();
        }
        for (int i = 0; i < equipRecordDetail.getRecordImages().size(); i++) {
            photo_content.addView(getImageView(i, images));
        }
        LinearLayout file_content = (LinearLayout) getView().findViewById(R.id.file_content);
        for (int i = 0; i < equipRecordDetail.getRecordAppendices().size(); i++) {
            file_content.addView(getTextView(equipRecordDetail.getRecordAppendices().get(i).getFileUrl()
                    , equipRecordDetail.getRecordAppendices().get(i).getFileName()));
        }
    }

    @Override
    public void downLoadSuccess(String filePath) {
        new MaterialDialog.Builder(getActivity())
                .content("文件已经下载成功，请在EvPro文件夹中查看!")
                .positiveText("确定").show();
    }

    @Override
    public void showDownLoadProgress() {
        showProgressDialog("下载中...");
    }

    @Override
    public void hideDownProgress() {
        hideProgressDialog();
    }

    @Override
    public void downLoadFail() {
        getApp().showToast("下载失败");
    }

    @Override
    public void setPresenter(EquipmentRecordDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private ImageView getImageView(int position, String[] url) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setTag(R.id.tag_task, url);
        imageView.setTag(R.id.tag_position, position);
        imageView.setOnClickListener(onClickListener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getActivity(), 60), DisplayUtil.dip2px(getActivity(), 60));
        params.setMargins(10, 0, 0, 0);//4个参数按顺序分别是左上右下
        imageView.setLayoutParams(params);
        GlideUtils.ShowImage(this, url[position], imageView, R.drawable.picture_default);
        return imageView;
    }

    private View getTextView(String url, String fileName) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_file_text, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_file_name);
        textView.setText(fileName);
        textView.setTag(R.id.tag_object, url);
        textView.setTag(R.id.tag_object_1, fileName);
        textView.setOnClickListener(onFileClick);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] urls = (String[]) v.getTag(R.id.tag_task);
            int position = (int) v.getTag(R.id.tag_position);
            ViewPagePhotoActivity.startActivity(getActivity(), urls, position);
        }
    };

    private View.OnClickListener onFileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!EasyPermissions.hasPermissions(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AppSettingsDialog.Builder(getActivity())
                        .setTitle(getString(R.string.request_permissions))
                        .setRationale(getString(R.string.need_save_setting))
                        .setPositiveButton(getString(R.string.sure))
                        .setNegativeButton(getString(R.string.cancel))
                        .setRequestCode(REQUEST_EXTERNAL)
                        .build()
                        .show();
                return;
            }
            String url = (String) v.getTag(R.id.tag_object);
            String fileName = (String) v.getTag(R.id.tag_object_1);
            String filePath = DOWNLOAD_PATH + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                new MaterialDialog.Builder(getActivity())
                        .content("文件已经存在，请在EvPro文件夹中查看!")
                        .positiveText("确定").show();
            } else {
                if (mPresenter != null) {
                    mPresenter.downLoadFile(url, DOWNLOAD_PATH, fileName);
                }
            }
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_EXTERNAL) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setRationale(getString(R.string.need_save_setting))
                        .setTitle(getString(R.string.request_permissions))
                        .setPositiveButton(getString(R.string.sure))
                        .setNegativeButton(getString(R.string.cancel))
                        .setRequestCode(REQUEST_EXTERNAL)
                        .build()
                        .show();
            }
        }
    }

    public static final int REQUEST_EXTERNAL = 10;//内存卡权限

    @AfterPermissionGranted(REQUEST_EXTERNAL)
    public void checkPermission() {
        if (!EasyPermissions.hasPermissions(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "当前app 需要请求存储权限以及麦克风权限",
                    REQUEST_EXTERNAL, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
