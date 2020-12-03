package com.isuo.yw2application.view.main.work;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
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
import com.isuo.yw2application.view.main.device.list.EquipListActivity;
import com.isuo.yw2application.view.main.equip.archives.EquipmentArchivesActivity;
import com.isuo.yw2application.view.main.work.all.PayGridViewAdapter;
import com.isuo.yw2application.view.main.work.all.WorkItemAllActivity;
import com.isuo.yw2application.view.main.work.all.WorkItemAllIntent;
import com.isuo.yw2application.view.main.work.all.widget.WorkItemGridView;
import com.isuo.yw2application.view.main.work.message.NewsListActivity;
import com.isuo.yw2application.view.main.work.safe_manager.SafeManagerActivity;
import com.isuo.yw2application.view.main.work.sos.SOSActivity;
import com.isuo.yw2application.widget.WorkItemLayout;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.GlideUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;

public class WorkFragment extends MvpFragmentV4<WorkContract.Presenter> implements WorkContract.View, View.OnClickListener {

    private ArrayList<WorkItem> showWorkItemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView userPhoto;
    private WorkItemGridView gridView;
    private GridViewAdapter gridViewAdapter;

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
        rootView.findViewById(R.id.leftImage).setOnClickListener(this);
        gridView = rootView.findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this.getActivity(), showWorkItemList, R.layout.work_item);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == -1) {
                    startActivityForResult(new Intent(getActivity(), WorkItemAllActivity.class), WORK_ALL_CODE);
                } else {
                    startActivity(WorkItemAllIntent.startWorkItem(getActivity(), showWorkItemList.get(position)));
                }
            }
        });
        rootView.findViewById(R.id.workMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.alarmMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.enterpriseMessageLayout).setOnClickListener(this);
        rootView.findViewById(R.id.abtMeMessageLayout).setOnClickListener(this);
        GlideUtils.ShowCircleImage(this.getActivity(), user.getPortraitUrl(), userPhoto, R.drawable.avater);
        return rootView;
    }

    private void userPhotoUpdate() {
        User user = Yw2Application.getInstance().getCurrentUser();
        if (userPhoto != null) {
            GlideUtils.ShowCircleImage(this.getActivity(), user.getPortraitUrl(), userPhoto, R.drawable.avater);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getNews();
        mPresenter.getWorkItem();
    }

    @Override
    public void showWorkItemList(List<WorkItem> workItems) {
        showWorkItemList.clear();
        showWorkItemList.addAll(workItems);
        if (getView() == null) {
            return;
        }
        gridViewAdapter.setData(showWorkItemList);
        WorkItem workItem9 = new WorkItem(9, "紧急电话", R.drawable.emergency_call);
        WorkItem workItem10 = new WorkItem(10, "安全制度管理", R.drawable.security);
        ((WorkItemLayout) getView().findViewById(R.id.workItem9)).setContent(workItem9);
        ((WorkItemLayout) getView().findViewById(R.id.workItem10)).setContent(workItem10);
        getView().findViewById(R.id.workItem9).setOnClickListener(this);
        getView().findViewById(R.id.workItem10).setOnClickListener(this);
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
            case R.id.rightImage:
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), SCANNER_CODE);
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
                                new AppSettingsDialog.Builder(getActivity())
                                        .setRationale(getString(R.string.need_camera_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
                break;
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
            case R.id.workItem9:
                startActivity(new Intent(getActivity(), SOSActivity.class));
                break;
            case R.id.workItem10:
                startActivity(new Intent(getActivity(), SafeManagerActivity.class));
                break;
        }
    }

    private final int WORK_ALL_CODE = 100;
    private final int SCANNER_CODE = 200;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORK_ALL_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (mPresenter != null) {
                mPresenter.getWorkItem();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_CODE) {
            if (data != null) {
                String result = data.getStringExtra(CaptureActivity.RESULT);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    long id = jsonObject.getLong("id");
                    if (TextUtils.equals("room", type)) {
                        Intent intent = new Intent(getActivity(), EquipListActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_3, true);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_INT_2, id);
                        startActivity(intent);
                    } else if (TextUtils.equals("equipment", type)) {
                        Intent intent = new Intent(getActivity(), EquipmentArchivesActivity.class);
                        intent.putExtra(ConstantStr.KEY_BUNDLE_STR, String.valueOf(id));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
