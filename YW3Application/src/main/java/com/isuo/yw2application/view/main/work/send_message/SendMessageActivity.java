package com.isuo.yw2application.view.main.work.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.view.main.adduser.EmployeeActivity;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.option.OptionBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.widget.FileItemLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import filepicker.ui.FilePickerActivity;


/**
 * 发布新的企业通知
 * Created by zhangan on 2018/3/6.
 */

public class SendMessageActivity extends BaseActivity implements SendMessageContract.View {

    private EditText titleEt, contentEt;
    private TextView tvMessageType, tvUserList;
    private LinearLayout llFileList;
    private SendMessageContract.Presenter mPresenter;
    private ArrayList<EmployeeBean> chooseEmployeeBeen;
    private String userIds;
    private List<String> fileList;
    private int messageType = -1;
    private final static int REQUEST_CODE_USER = 2;
    private final static int REQUEST_CODE_FILE = 3;
    private List<OptionBean.ItemListBean> typeList;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_send_message, "发布通知");
        new SendMessagePresenter(Yw2Application.getInstance().getRepositoryComponent().getRepository(), this);
        initView();
        initData();
    }

    private void initView() {
        titleEt = findViewById(R.id.et_title);
        contentEt = findViewById(R.id.et_content);
        tvMessageType = findViewById(R.id.tvMessageType);
        tvUserList = findViewById(R.id.tvUserList);
        llFileList = findViewById(R.id.llFileList);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.llType).setOnClickListener(this);
        findViewById(R.id.llUser).setOnClickListener(this);
        findViewById(R.id.llFile).setOnClickListener(this);
    }

    private void initData() {
        chooseEmployeeBeen = new ArrayList<>();
        fileList = new ArrayList<>();
        List<OptionBean> list = Yw2Application.getInstance().getOptionInfo();
        if (list != null && list.size() > 0) {
            typeList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOptionId() == ConstantInt.SEND_MESSAGE) {
                    typeList.addAll(list.get(i).getItemList());
                }
            }
        }
        if (typeList.size() > 0) {
            messageType = typeList.get(0).getItemId();
            tvMessageType.setText(typeList.get(0).getItemName());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llType:
                List<String> types = new ArrayList<>();
                for (int i = 0; i < typeList.size(); i++) {
                    types.add(typeList.get(i).getItemName());
                }
                new MaterialDialog.Builder(this)
                        .items(types)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                messageType = typeList.get(position).getItemId();
                                tvMessageType.setText(text);
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.llUser:
                Intent intentUser = new Intent(this, EmployeeActivity.class);
                intentUser.putParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST, chooseEmployeeBeen);
                startActivityForResult(intentUser, REQUEST_CODE_USER);
                break;
            case R.id.llFile:
                Intent intent = new Intent(this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.ARG_FILTER, Pattern.compile(".*\\.*"));
                intent.putExtra(FilePickerActivity.ARG_CLOSEABLE, false);
                intent.putExtra(FilePickerActivity.ARG_TITLE, "选择文件");
                startActivityForResult(intent, REQUEST_CODE_FILE);
                break;
            case R.id.btn_send:

                if (TextUtils.isEmpty(titleEt.getText().toString())) {
                    Yw2Application.getInstance().showToast("请输入标题");
                    return;
                }
                if (TextUtils.isEmpty(contentEt.getText().toString())) {
                    Yw2Application.getInstance().showToast("请输入内容");
                    return;
                }
                if (messageType == -1) {
                    Yw2Application.getInstance().showToast("请在后台配置消息类型");
                    return;
                }
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("messageTitle", titleEt.getText().toString());
                    jsonObject.put("messageContent", contentEt.getText().toString());
                    jsonObject.put("messageContentType", messageType);
                    jsonObject.put("messageType", 1);
                    jsonObject.put("toUserIds", userIds);
                    if (fileList.size() > 0) {
                        mPresenter.uploadFile(fileList);
                        return;
                    }

                    mPresenter.sendMessage(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void sendSuccess() {
        finish();
        Yw2Application.getInstance().showToast("发送成功!");
    }

    @Override
    public void showMessage(String message) {
        Yw2Application.getInstance().showToast(message);
    }

    @Override
    public void uploadFileSuccess(String fileUrl) {
        try {
            jsonObject.put("appendicesUrl", fileUrl);
            mPresenter.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFileFail() {

    }

    @Override
    public void showUploadProgress() {
        showProgressDialog("上传中...");
    }

    @Override
    public void hideUploadProgress() {
        hideProgressDialog();
    }

    @Override
    public void setPresenter(SendMessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK) {
            chooseEmployeeBeen = data.getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
            if (chooseEmployeeBeen != null && chooseEmployeeBeen.size() > 0) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sbName = new StringBuilder();
                for (int i = 0; i < chooseEmployeeBeen.size(); i++) {
                    sb.append(chooseEmployeeBeen.get(i).getUser().getUserId());
                    sbName.append(chooseEmployeeBeen.get(i).getUser().getRealName());
                    if (i != chooseEmployeeBeen.size() - 1) {
                        sb.append(",");
                        sbName.append(",");
                    }
                    userIds = sb.toString();
                    tvUserList.setText(sbName);
                }
            } else {
                tvUserList.setText("全部");
                userIds = null;
            }
        } else if (requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (new File(filePath).isFile()) {
                if (new File(filePath).length() > 20 * 1024 * 1024) {
                    Yw2Application.getInstance().showToast("请选择20M以下的文件");
                    return;
                }
                fileList.clear();
                fileList.add(filePath);
                refreshFileList();
            }
        }
    }

    private void refreshFileList() {
        llFileList.removeAllViews();
        for (int i = 0; i < fileList.size(); i++) {
            FileItemLayout layout = new FileItemLayout(this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setTag(i);
            layout.setData(new File(fileList.get(i)).getName(), i, fileClick);
            llFileList.addView(layout,i);
        }
    }

    private View.OnClickListener fileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (position < fileList.size()) {
                fileList.remove(position);
            }
            refreshFileList();
        }
    };
}
