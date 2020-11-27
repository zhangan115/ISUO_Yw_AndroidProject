package com.isuo.yw2application.view.contact.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.utils.ChooseDateDialog;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.alarm.fault.history.FaultHistoryActivity;
import com.isuo.yw2application.view.main.data.statistics.person.StatisticsPersonActivity;
import com.isuo.yw2application.view.main.task.inspection.user_list.InspectionTaskListActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.sito.library.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class UserDetailActivity extends BaseActivity implements UserDetailContract.View {

    private int userId;
    private UserDetailContract.Presenter mPresenter;
    private TextView[] texts = new TextView[10];
    private TextView[] chooseTx = new TextView[3];
    private EditText[] ets = new EditText[7];
    private TextView editInfoTv;
    private int type = 0;
    private int sex = -1; // 0 男 1 女

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_user_detail, "人员详情");
        new UserDetailPresenter(this, Yw2Application.getInstance().getRepositoryComponent().getRepository());
        userId = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        if (userId == -1) {
            finish();
            return;
        }
        texts[0] = findViewById(R.id.text1);
        ets[0] = findViewById(R.id.et1);

        texts[1] = findViewById(R.id.text2);

        texts[2] = findViewById(R.id.text3);
        ets[1] = findViewById(R.id.et3);

        texts[3] = findViewById(R.id.text4);
        ets[2] = findViewById(R.id.et4);

        texts[4] = findViewById(R.id.text5);
        ets[3] = findViewById(R.id.et5);

        texts[5] = findViewById(R.id.text6);
        ets[4] = findViewById(R.id.et6);

        texts[6] = findViewById(R.id.text7);

        texts[7] = findViewById(R.id.text8);
        ets[5] = findViewById(R.id.et8);

        texts[8] = findViewById(R.id.text9);
        ets[6] = findViewById(R.id.et9);

        texts[9] = findViewById(R.id.text10);

        chooseTx[0] = findViewById(R.id.choose1);
        chooseTx[0].setOnClickListener(this);
        chooseTx[1] = findViewById(R.id.choose2);
        chooseTx[1].setOnClickListener(this);
        chooseTx[2] = findViewById(R.id.choose3);
        chooseTx[2].setOnClickListener(this);
        editInfoTv = findViewById(R.id.editInfoTv);
        editInfoTv.setOnClickListener(this);

        findViewById(R.id.ll_1).setOnClickListener(this);
        findViewById(R.id.ll_2).setOnClickListener(this);
        findViewById(R.id.ll_3).setOnClickListener(this);
        mPresenter.subscribe();
        mPresenter.getUserInfo(userId);
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.choose1:
                calendar.set(1900, 0, 1);
                new ChooseDateDialog(this)
                        .setMinDate(calendar)
                        .setMaxDate(Calendar.getInstance())
                        .pickDay()
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                String text = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
                                texts[1].setText(text);
                                chooseTx[0].setText(text);
                            }
                        })
                        .show();
                break;
            case R.id.choose2:
                calendar.set(2000, 0, 1);
                new ChooseDateDialog(this)
                        .setMinDate(calendar)
                        .setMaxDate(Calendar.getInstance())
                        .pickDay()
                        .setResultListener(new ChooseDateDialog.OnDateChooseListener() {
                            @Override
                            public void onDate(Calendar calendar) {
                                String text = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
                                texts[6].setText(text);
                                chooseTx[1].setText(text);
                            }
                        })
                        .show();
                break;
            case R.id.choose3:
                new XPopup.Builder(this).asBottomList("选择性别", new String[]{"男", "女"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                sex = position;
                                chooseTx[2].setText(text);
                                texts[9].setText(text);
                            }
                        }).show();
                break;
            case R.id.editInfoTv:
                if (type == 0) {
                    type = 1;
                } else {
                    try {
                        type = 0;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId", this.userId);
                        if (!TextUtils.isEmpty(ets[0].getText().toString())) {
                            jsonObject.put("realName", ets[0].getText());
                        }
                        if (!TextUtils.isEmpty(ets[1].getText().toString())) {
                            jsonObject.put("age", ets[1].getText());
                        }
                        if (!TextUtils.isEmpty(ets[2].getText().toString())) {
                            jsonObject.put("height", ets[2].getText());
                        }
                        if (!TextUtils.isEmpty(ets[3].getText().toString())) {
                            jsonObject.put("post", ets[3].getText());
                        }
                        if (!TextUtils.isEmpty(ets[4].getText().toString())) {
                            jsonObject.put("ability", ets[4].getText());
                        }
                        if (!TextUtils.isEmpty(ets[5].getText().toString())) {
                            jsonObject.put("userCode", ets[5].getText());
                        }
                        if (!TextUtils.isEmpty(ets[6].getText().toString())) {
                            jsonObject.put("userPhone", ets[6].getText());
                        }
                        jsonObject.put("sex", this.sex);
                        jsonObject.put("birthDay", this.texts[1].getText().toString());
                        jsonObject.put("entryTime", this.texts[6].getText().toString());
                        mPresenter.saveUserInfo(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                editTypeChange();
                break;
            case R.id.ll_1:
                Intent intent = new Intent(this, StatisticsPersonActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, userId);
                startActivity(intent);
                break;
            case R.id.ll_2:
                Intent intent1 = new Intent(this, InspectionTaskListActivity.class);
                intent1.putExtra(ConstantStr.KEY_BUNDLE_INT, userId);
                startActivity(intent1);
                break;
            case R.id.ll_3:
                Intent intent2 = new Intent(this, FaultHistoryActivity.class);
                intent2.putExtra(ConstantStr.KEY_BUNDLE_INT, userId);
                startActivity(intent2);
                break;
        }
    }

    private void editTypeChange() {
        for (EditText et : ets) {
            et.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
            if (type == 0) {
                texts[0].setText(ets[0].getText());
                texts[2].setText(ets[1].getText());
                texts[3].setText(ets[2].getText());
                texts[4].setText(ets[3].getText());
                texts[5].setText(ets[4].getText());
                texts[7].setText(ets[5].getText());
                texts[8].setText(ets[6].getText());
            }
        }
        for (TextView tv : chooseTx) {
            tv.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        }
        for (TextView tv : texts) {
            tv.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        }
        if (type == 0) {
            editInfoTv.setText("编辑");
        } else {
            editInfoTv.setText("保存");
        }
    }

    @Override
    public void setPresenter(UserDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUserInfo(User user) {
        if (!TextUtils.isEmpty(user.getRealName())) {
            texts[0].setText(user.getRealName());
            ets[0].setText(user.getRealName());
        }
        if (!TextUtils.isEmpty(user.getBirthDay())) {
            texts[1].setText(user.getBirthDay());
            chooseTx[0].setText(user.getBirthDay());
        }
        sex = user.getSex();
        if (sex == 0) {
            texts[9].setText("男");
            chooseTx[2].setText(texts[9].getText());
        } else if (sex == 1) {
            texts[9].setText("女");
            chooseTx[2].setText(texts[9].getText());
        }
        int age = user.getAge();
        if (age != -1) {
            texts[2].setText(String.valueOf(age));
            ets[1].setText(String.valueOf(age));
        }
        if (!TextUtils.isEmpty(user.getHeight())) {
            texts[3].setText(user.getHeight());
            ets[2].setText(user.getHeight());
        }
        if (!TextUtils.isEmpty(user.getPost())) {
            texts[4].setText(user.getPost());
            ets[3].setText(user.getPost());
        }
        if (!TextUtils.isEmpty(user.getAbility())) {
            texts[5].setText(user.getAbility());
            ets[4].setText(user.getAbility());
        }
        if (!TextUtils.isEmpty(user.getEntryTime())) {
            texts[6].setText(user.getEntryTime());
            chooseTx[1].setText(user.getEntryTime());
        }
        if (!TextUtils.isEmpty(user.getUserCode())) {
            texts[7].setText(user.getUserCode());
            ets[5].setText(user.getUserCode());
        }
        if (!TextUtils.isEmpty(user.getUserPhone())) {
            texts[8].setText(user.getUserPhone());
            ets[6].setText(user.getUserPhone());
        }
    }

    @Override
    public void saveSuccess() {
        Yw2Application.getInstance().showToast("保存成功!");
        mPresenter.getUserInfo(this.userId);
    }
}
