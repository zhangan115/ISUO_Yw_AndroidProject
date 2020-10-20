package com.isuo.yw2application.view.main.device.equipment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.client.android.CaptureActivity;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.create.CreateRepository;
import com.isuo.yw2application.utils.KeyboardChangeListener;
import com.isuo.yw2application.utils.PhotoUtils;
import com.isuo.yw2application.view.base.MvpFragment;
import com.isuo.yw2application.view.main.device.info.CreateEquipInfoActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.isuo.yw2application.widget.SpecialChatView;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.GlideUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */

public class CreateEquipFragment extends MvpFragment<CreateEquipContract.Presenter> implements CreateEquipContract.View, View.OnClickListener {

    //view
    private TextView tv_create_equip_type, tv_create_equip_name;
    private TextView tv_create_equip_num, tv_equip_grade, tv_create_room, tv_report, tv_equip_level;
    private EditText edit_content;
    private TextView currentTv;
    private LinearLayout layout_contain, ll_enter_layout;
    private ImageView photo_view, iv_change, iv_clean_edit;
    private ImageView iv_equipment_name, iv_equipment_num;
    private SpecialChatView mSpecialV; //特殊字符表
    private SwitchCompat switchCompat;
    //data
    private EquipmentBean equipmentBean;
    private String roomName;
    private long roomId;
    private int chooseNameType, chooseSpecialType;
    private boolean isKeyBoardShow, isAdd = true;
    private String equipmentPhotoUrl, equipmentLocal, equipmentTempPhoto;
    private File photoFile;
    private static final int ACTION_START_CAMERA = 100;
    private static final int ACTION_EQUIPMENT_ROOM = 101;
    private static final int ACTION_EQUIPMENT_TYPE = 102;
    private long typeId;
    private JSONObject jsonInfo;
    private int voltageLevel = -1, equipmentLevel = -1;

    public static CreateEquipFragment newInstance(@Nullable EquipmentBean equipment, long roomId, String roomName) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantStr.KEY_BUNDLE_OBJECT, equipment);
        args.putLong(ConstantStr.KEY_BUNDLE_LONG, roomId);
        args.putString(ConstantStr.KEY_BUNDLE_STR, roomName);
        CreateEquipFragment fragment = new CreateEquipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CreateEquipPresenter(CreateRepository.getRepository(), this);
        equipmentBean = getArguments().getParcelable(ConstantStr.KEY_BUNDLE_OBJECT);
        roomId = getArguments().getLong(ConstantStr.KEY_BUNDLE_LONG);
        roomName = getArguments().getString(ConstantStr.KEY_BUNDLE_STR);
        if (equipmentBean != null) {
            voltageLevel = equipmentBean.getVoltageLevel();
            typeId = equipmentBean.getEquipmentType().getEquipmentTypeId();
            isAdd = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_equipment, container, false);
        rootView.findViewById(R.id.ll_create_room).setOnClickListener(this);
        rootView.findViewById(R.id.ll_create_equip_type).setOnClickListener(this);
        rootView.findViewById(R.id.ll_create_equip_level).setOnClickListener(this);
        rootView.findViewById(R.id.ll_create_equip_name).setOnClickListener(this);
        rootView.findViewById(R.id.ll_create_equip_num).setOnClickListener(this);
        rootView.findViewById(R.id.ll_type).setOnClickListener(this);
        rootView.findViewById(R.id.ll_equip_grade).setOnClickListener(this);
        rootView.findViewById(R.id.tv_sure).setOnClickListener(this);
        photo_view = rootView.findViewById(R.id.photo_view);
        switchCompat = rootView.findViewById(R.id.switchCompat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chooseNameType = 1;
                } else {
                    chooseNameType = 0;
                }
            }
        });
        iv_change = rootView.findViewById(R.id.iv_change);
        iv_equipment_name = rootView.findViewById(R.id.iv_equipment_name);
        iv_equipment_num = rootView.findViewById(R.id.iv_equipment_num);
        iv_clean_edit = rootView.findViewById(R.id.iv_clean_edit);
        tv_equip_level = rootView.findViewById(R.id.tv_create_equip_level);
        tv_create_room = rootView.findViewById(R.id.tv_create_room);
        tv_create_equip_type = rootView.findViewById(R.id.tv_create_equip_type);
        tv_create_equip_name = rootView.findViewById(R.id.tv_create_equip_name);
        tv_create_equip_num = rootView.findViewById(R.id.tv_create_equip_num);
        tv_equip_grade = rootView.findViewById(R.id.tv_equip_grade);
        tv_report = rootView.findViewById(R.id.tv_report);
        edit_content = rootView.findViewById(R.id.edit_content);
        layout_contain = rootView.findViewById(R.id.layout_contain);
        ll_enter_layout = rootView.findViewById(R.id.ll_enter_layout);
        tv_report.setOnClickListener(this);
        photo_view.setOnClickListener(this);
        photo_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(equipmentPhotoUrl)) {
                    return false;
                }
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                new MaterialDialog.Builder(getActivity())
                                        .items(R.array.choose_condition_2)
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                //重新拍照
                                                if (position == 0) {//删除照片
                                                    cleanImage();
                                                } else {
                                                    photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                                    ActivityUtils.startCameraToPhoto(CreateEquipFragment.this, photoFile, ACTION_START_CAMERA);
                                                }
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
                                new AppSettingsDialog.Builder(getActivity())
                                        .setRationale(getString(R.string.need_save_setting))
                                        .setTitle(getString(R.string.request_permissions))
                                        .setPositiveButton(getString(R.string.sure))
                                        .setNegativeButton(getString(R.string.cancel))
                                        .build()
                                        .show();
                            }
                        });
                return true;
            }
        });
        iv_change.setOnClickListener(this);
        iv_clean_edit.setOnClickListener(this);
        initSpecialView(layout_contain);
        edit_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    enterStr();
                    return true;
                }
                return false;
            }
        });
        new KeyboardChangeListener(getActivity()).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                isKeyBoardShow = isShow;
                if (!isShow && !mSpecialV.isShow()) {
                    iv_equipment_name.setImageDrawable(findDrawById(R.drawable.list_arrow));
                    iv_equipment_num.setImageDrawable(findDrawById(R.drawable.list_arrow));
                    cleanEdit();
                    ll_enter_layout.setVisibility(View.GONE);
                }
                if (isShow) {
                    mSpecialV.hide();
                }
            }
        });
        initData();
        return rootView;
    }

    private void enterStr() {
        currentTv.setText(edit_content.getText().toString());
        cleanEdit();
        getApp().hideSoftKeyBoard(getActivity());
    }

    private void initData() {
        if (equipmentBean != null) {
            tv_report.setText("保存");
            tv_create_equip_name.setText(equipmentBean.getEquipmentName());
            if (!TextUtils.isEmpty(equipmentBean.getEquipmentSn())) {
                tv_create_equip_num.setText(equipmentBean.getEquipmentSn());
            }
            tv_create_equip_type.setText(equipmentBean.getEquipmentType().getEquipmentTypeName());
            tv_equip_grade.setText(Yw2Application.getInstance().getMapOption().get("12").get(String.valueOf(equipmentBean.getVoltageLevel())));
            if (!TextUtils.isEmpty(equipmentBean.getNameplatePicUrl())) {
                equipmentPhotoUrl = equipmentBean.getNameplatePicUrl();
                GlideUtils.ShowImage(getActivity(), equipmentPhotoUrl, photo_view, R.drawable.picture_default);
            }
        }
        if (!TextUtils.isEmpty(roomName) && roomId != 0) {
            tv_create_room.setText(roomName);
        }
    }

    private void initSpecialView(LinearLayout view) {
        mSpecialV = new SpecialChatView(getActivity());
        mSpecialV.setData();
        mSpecialV.setOnSelected(new SpecialChatView.ISelected() {
            @Override
            public void onSelected(String text) {
                if (edit_content != null) {
                    Editable content = edit_content.getText();
                    content.append(text);
                    edit_content.setText(content);
                    edit_content.setSelection(content.length());
                }
            }
        });
        view.addView(mSpecialV);
    }

    @Override
    public void setPresenter(CreateEquipContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_create_room:
                Intent intent1 = new Intent(getActivity(), CreateEquipInfoActivity.class);
                intent1.putExtra(ConstantStr.KEY_BUNDLE_INT, 0);
                startActivityForResult(intent1, ACTION_EQUIPMENT_ROOM);
                break;
            case R.id.ll_create_equip_type:
                Intent intent2 = new Intent(getActivity(), CreateEquipInfoActivity.class);
                intent2.putExtra(ConstantStr.KEY_BUNDLE_INT, 1);
                startActivityForResult(intent2, ACTION_EQUIPMENT_TYPE);
                break;
            case R.id.ll_create_equip_name:
                currentTv = tv_create_equip_name;
                cleanEdit();
                showEdit(currentTv.getText().toString());
                iv_equipment_name.setImageDrawable(findDrawById(R.drawable.edit_icon));
                iv_equipment_num.setImageDrawable(findDrawById(R.drawable.list_arrow));
                ll_enter_layout.setVisibility(View.VISIBLE);
                if (!isKeyBoardShow) {
                    getApp().showSoftKeyBoard();
                }
                break;
            case R.id.ll_create_equip_num:
                currentTv = tv_create_equip_num;
                cleanEdit();
                showEdit(currentTv.getText().toString());
                iv_equipment_name.setImageDrawable(findDrawById(R.drawable.list_arrow));
                iv_equipment_num.setImageDrawable(findDrawById(R.drawable.edit_icon));
                ll_enter_layout.setVisibility(View.VISIBLE);
                if (!isKeyBoardShow) {
                    getApp().showSoftKeyBoard();
                }
                break;
            case R.id.ll_type://双重命名
                break;
            case R.id.ll_equip_grade://电压等级
                List<String> list = new ArrayList<>();
                final List<Integer> integers = new ArrayList<>();
                final Map<String, String> map = Yw2Application.getInstance().getMapOption().get("12");
                for (String str : map.values()) {
                    list.add(str);
                }
                for (String str : map.keySet()) {
                    integers.add(Integer.valueOf(str));
                }
                new MaterialDialog.Builder(getActivity())
                        .items(list)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                voltageLevel = integers.get(position);
                                tv_equip_grade.setText(text);
                            }
                        })
                        .show();
                break;
            case R.id.ll_create_equip_level://设备等级
                List<String> levelList = new ArrayList<>();
                levelList.add("A类");
                levelList.add("B类");
                levelList.add("C类");
                new MaterialDialog.Builder(getActivity())
                        .items(levelList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                equipmentLevel = position + 1;
                                tv_equip_level.setText(text);
                            }
                        })
                        .show();
                break;
            case R.id.tv_report:
                if (checkData()) {
                    try {
                        jsonInfo = new JSONObject();
                        jsonInfo.put("roomId", roomId);
                        jsonInfo.put("equipmentTypeId", typeId);
                        jsonInfo.put("equipmentLevel", equipmentLevel);
                        jsonInfo.put("equipmentName", tv_create_equip_name.getText().toString());
                        if (!TextUtils.isEmpty(equipmentPhotoUrl)) {
                            jsonInfo.put("nameplatePicUrl", equipmentPhotoUrl);
                        }
                        jsonInfo.put("equipmentSn", tv_create_equip_num.getText().toString());
                        jsonInfo.put("isDoubleNaming", chooseNameType == 0);
                        jsonInfo.put("voltageLevel", voltageLevel);
                        if (mPresenter != null) {
                            if (equipmentBean != null) {
                                jsonInfo.put("equipmentId", equipmentBean.getEquipmentId());
                                mPresenter.editEquipment(jsonInfo);
                            } else {
                                mPresenter.uploadEquipment(jsonInfo);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.photo_view:
                if (TextUtils.isEmpty(equipmentLocal) && TextUtils.isEmpty(equipmentPhotoUrl)) {
                    SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            new CheckRequestPermissionListener() {
                                @Override
                                public void onPermissionOk(Permission permission) {
                                    photoFile = new File(Yw2Application.getInstance().imageCacheFile(), System.currentTimeMillis() + ".jpg");
                                    ActivityUtils.startCameraToPhoto(CreateEquipFragment.this, photoFile, ACTION_START_CAMERA);
                                }

                                @Override
                                public void onPermissionDenied(Permission permission) {
                                    new AppSettingsDialog.Builder(getActivity())
                                            .setRationale(getString(R.string.need_save_setting))
                                            .setTitle(getString(R.string.request_permissions))
                                            .setPositiveButton(getString(R.string.sure))
                                            .setNegativeButton(getString(R.string.cancel))
                                            .build()
                                            .show();
                                }
                            });
                } else {
                    String[] urls = null;
                    if (!TextUtils.isEmpty(equipmentLocal)) {
                        urls = new String[]{equipmentLocal};
                    } else if (!TextUtils.isEmpty(equipmentPhotoUrl)) {
                        urls = new String[]{equipmentPhotoUrl};
                    }
                    ViewPagePhotoActivity.startActivity(getActivity(), urls, 0);
                }
                break;
            case R.id.iv_change:
                chooseSpecialType = chooseSpecialType == 0 ? 1 : 0;
                if (chooseSpecialType == 1) {
                    iv_change.setImageDrawable(findDrawById(R.drawable.keyboard_icon));
                    getApp().hideSoftKeyBoard(getActivity());
                    mSpecialV.show();
                } else {
                    iv_change.setImageDrawable(findDrawById(R.drawable.ch_icon));
                    getApp().showSoftKeyBoard();
                    mSpecialV.hide();
                }
                break;
            case R.id.iv_clean_edit:
                cleanEdit();
                break;
            case R.id.tv_sure:
                enterStr();
                break;
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(tv_create_room.getText())) {
            showMessage("请选择区域");
            return false;
        }
        if (TextUtils.isEmpty(tv_create_equip_type.getText())) {
            showMessage("请选择设备类型");
            return false;
        }
        if (TextUtils.isEmpty(tv_create_equip_name.getText())) {
            showMessage("请输入设备名称");
            return false;
        }
        if (chooseNameType == 1 && TextUtils.isEmpty(tv_create_equip_num.getText())) {
            showMessage("请输入位号");
            return false;
        }
        if (voltageLevel == -1) {
            showMessage("请选择电压等级");
            return false;
        }
        if (equipmentLevel == -1) {
            showMessage("请选择设备等级");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_START_CAMERA && resultCode == Activity.RESULT_OK) {
            PhotoUtils.cropPhoto(getActivity(), photoFile, new PhotoUtils.PhotoListener() {
                @Override
                public void onSuccess(File file) {
                    if (mPresenter != null) {
                        equipmentTempPhoto = file.getAbsolutePath();
                        mPresenter.uploadImage("equipment", file.getAbsolutePath());
                    }
                }
            });
        } else if (requestCode == ACTION_EQUIPMENT_ROOM && resultCode == Activity.RESULT_OK) {
            roomId = data.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
            String roomName = data.getStringExtra(ConstantStr.KEY_BUNDLE_STR);
            tv_create_room.setText(roomName);
        } else if (requestCode == ACTION_EQUIPMENT_TYPE && resultCode == Activity.RESULT_OK) {
            typeId = data.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
            String typeName = data.getStringExtra(ConstantStr.KEY_BUNDLE_STR);
            tv_create_equip_type.setText(typeName);
        }
    }

    private void cleanEdit() {
        edit_content.setText("");
        edit_content.setSelection(0);
    }

    private void showEdit(String str) {
        edit_content.setText(str);
        edit_content.setSelection(str.length());
    }

    @Override
    public void showImage(String url) {
        equipmentLocal = equipmentTempPhoto;
        equipmentPhotoUrl = url;
        GlideUtils.ShowImage(getActivity(), equipmentLocal, photo_view, R.drawable.picture_default);
    }

    @Override
    public void showMessage(@Nullable String message) {
        if (!TextUtils.isEmpty(message)) {
            getApp().showToast(message);
        }
    }

    @Override
    public void showLoading() {
        showProgressDialog("上传中...");
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void uploadEquipmentSuccess() {
        getApp().showToast("上传成功");
        if (isAdd) {
            cleanImage();
        } else {
            getActivity().finish();
        }
    }

    private void cleanImage() {
        equipmentPhotoUrl = null;
        equipmentLocal = null;
        equipmentTempPhoto = null;
        photo_view.setImageDrawable(findDrawById(R.drawable.photo_button));
    }
}
