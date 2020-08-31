package com.sito.evpro.inspection.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.db.UserInfo;
import com.sito.evpro.inspection.utils.commadapter.CommonAdapter;
import com.sito.evpro.inspection.utils.commadapter.ViewHolder;
import com.sito.evpro.inspection.view.MvpFragment;
import com.sito.evpro.inspection.view.home.HomeActivity;
import com.sito.evpro.inspection.view.regist.RegisterActivity;
import com.sito.evpro.inspection.view.setting.test.TestActivity;

import java.util.List;

/**
 * 登陆界面
 * Created by zhangan on 2017-06-22.
 */

public class LoginFragment extends MvpFragment<LoginContract.Presenter> implements LoginContract.View, View.OnClickListener {

    private AppCompatMultiAutoCompleteTextView mNameEt;
    private EditText mPassEt;
    private ImageView mChoose;
    private List<UserInfo> userInfos;
    private ListPopupWindow popupWindow;
    private LinearLayout mLoginLL;
    private View mPopView;
    private ListView mUserList;
    private TextView mLogin;
    private boolean isShowPop;
    private FrameLayout mLoginBg;

    public static LoginFragment newInstance(boolean needLogin) {
        Bundle args = new Bundle();
        args.putBoolean(ConstantStr.KEY_BUNDLE_BOOLEAN, needLogin);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmg_login, container, false);
        mNameEt = (AppCompatMultiAutoCompleteTextView) rootView.findViewById(R.id.edit_username);
        mPassEt = (EditText) rootView.findViewById(R.id.edit_password);
        mChoose = (ImageView) rootView.findViewById(R.id.img_choose);
        mLoginLL = (LinearLayout) rootView.findViewById(R.id.id_ll_user);
        mPopView = rootView.findViewById(R.id.id_pop_view);
        mUserList = (ListView) rootView.findViewById(R.id.id_user_list);
        mLogin = (TextView) rootView.findViewById(R.id.tv_login);
        mLoginBg = (FrameLayout) rootView.findViewById(R.id.id_login_bg);
        rootView.findViewById(R.id.tv_test).setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mChoose.setOnClickListener(this);
        mNameEt.setOnClickListener(this);
        rootView.findViewById(R.id.tv_forget_password).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isShowPop) {
                    isShowPop = false;
                    mLoginLL.setVisibility(View.GONE);
                    mPassEt.setVisibility(View.VISIBLE);
//                    mPopView.setVisibility(View.VISIBLE);
                    mLogin.setVisibility(View.VISIBLE);
//                    mChoose.setBackground(findDrawById(R.drawable.list_narrow_under_normal));
                    mChoose.setImageDrawable(findDrawById(R.drawable.list_narrow_under_normal));
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfos = InspectionApp.getInstance().getDaoSession().getUserInfoDao().loadAll();
        if (userInfos != null && userInfos.size() > 0) {
            mChoose.setVisibility(View.VISIBLE);
        } else {
            mChoose.setVisibility(View.INVISIBLE);
        }
        mNameEt.setSelection(mNameEt.getText().toString().length());//将光标一道最后
        mPassEt.setText("");
        inItPopWindow();
    }

    @Override
    public void loginSuccess() {
        startHomeActivity();
    }

    @Override
    public void loginFail() {

    }

    @Override
    public void loginAutoFinish() {
        startHomeActivity();
    }

    @Override
    public void loginNeed() {

    }

    @Override
    public void loginLoading() {
        showProgressDialog("登陆中...");
    }

    @Override
    public void loginHideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showHistoryUser(@NonNull List<User> userList) {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                checkUserInfo();
                break;
            case R.id.edit_username:
                if (isShowPop) {
                    isShowPop = false;
                    mLoginLL.setVisibility(View.GONE);
                    mPassEt.setVisibility(View.VISIBLE);
//                    mPopView.setVisibility(View.VISIBLE);
                    mLogin.setVisibility(View.VISIBLE);
//                    mChoose.setBackground(findDrawById(R.drawable.list_narrow_under_normal));
                    mChoose.setImageDrawable(findDrawById(R.drawable.list_narrow_under_normal));
                }
                break;
            case R.id.img_choose:
                if (!isShowPop) {
                    if (userInfos != null && userInfos.size() > 0) {
                        mLoginLL.setVisibility(View.VISIBLE);
                        mPassEt.setVisibility(View.GONE);
//                        mPopView.setVisibility(View.GONE);
                        mLogin.setVisibility(View.GONE);
                    }
                    mChoose.setImageDrawable(findDrawById(R.drawable.list_narrow_top_normal));
                    isShowPop = true;
                } else {
                    isShowPop = false;
                    mLoginLL.setVisibility(View.GONE);
                    mPassEt.setVisibility(View.VISIBLE);
//                    mPopView.setVisibility(View.VISIBLE);
                    mLogin.setVisibility(View.VISIBLE);
//                    mChoose.setBackground(findDrawById(R.drawable.list_narrow_under_normal));
                    mChoose.setImageDrawable(findDrawById(R.drawable.list_narrow_under_normal));
                }
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.tv_test:
                startTestTimes++;
                if (startTestTimes > 6) {
                    startActivity(new Intent(getActivity(), TestActivity.class));
                    startTestTimes = 0;
                }
                break;
        }
    }

    int startTestTimes;

    private void checkUserInfo() {
        String userName = mNameEt.getEditableText().toString().trim();
        String userPass = mPassEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            InspectionApp.getInstance().showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(userPass)) {
            InspectionApp.getInstance().showToast("请输入密码");
            return;
        }
        if (mPresenter != null) {
            mPresenter.login(userName, userPass);
        }

    }

    private void startHomeActivity() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
        getActivity().finish();
    }

    private void inItPopWindow() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.login_pop, null);

        //自适配长、框设置
//        popupWindow = new ListPopupWindow(getActivity());
//        popupWindow.setBackgroundDrawable(findDrawById(R.drawable.bg_login_enter));
//        popupWindow.setOutsideTouchable(true);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        // 实例化一个ColorDrawable颜色为半透明
////        ColorDrawable dw = new ColorDrawable(0000000000);
////        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
////        popupWindow.setBackgroundDrawable(dw);
//        // 刷新状态
//        popupWindow.update();
//        popupWindow.showAsDropDown(mNameEt);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        PopAdpter adpter = new PopAdpter(getActivity(), userInfos, R.layout.item_pop_user);
//        popupWindow.setAdapter(adpter);
//        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setAnchorView(mPopView);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
//        popupWindow.setModal(true);//设置是否是模式
//        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mNameEt.setText(userInfos.get(position).getUserName());
//                mPassEt.setText(userInfos.get(position).getPassward());
//                popupWindow.dismiss();
//            }
//        });
        mUserList.setAdapter(adpter);
        int totalHeight = 0;
        int height = adpter.getCount();
        if (height > 4) {
            height = 4;
        }
        for (int i = 0; i < height; i++) {//最大显示4个的高度
            View listItem = adpter.getView(i, null, mUserList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mUserList.getLayoutParams();

        params.height = totalHeight
                + (mUserList.getDividerHeight() * (mUserList.getCount() - 1));
        mUserList.setLayoutParams(params);

        mUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isShowPop = false;
                mNameEt.setText(userInfos.get(position).getUserName());
                // mPassEt.setText(userInfos.get(position).getPassward());
                mLoginLL.setVisibility(View.GONE);
                mPassEt.setVisibility(View.VISIBLE);
//                mPopView.setVisibility(View.VISIBLE);
                mLogin.setVisibility(View.VISIBLE);
                mChoose.setImageDrawable(findDrawById(R.drawable.list_narrow_under_normal));
                mNameEt.setSelection(mNameEt.getText().toString().length());//将光标一道最后
            }
        });
//        ListView listView = (ListView) view.findViewById(R.id.id_pop_listview);
//        listView.setAdapter(adpter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mNameEt.setText(userInfos.get(position).getUserName());
//                mPassEt.setText(userInfos.get(position).getPassward());
//                popupWindow.dismiss();
//            }
//        });
    }

    class PopAdpter extends CommonAdapter<UserInfo> {

        public PopAdpter(Context context, List<UserInfo> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void conVert(ViewHolder viewHolder, UserInfo userInfo) {
            viewHolder.setTvText(R.id.id_pop_username, userInfo.getUserName());
        }
    }
}
