package com.isuo.yw2application.view.main.work.tool.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.tool.borrow.BorrowToolsActivity;
import com.isuo.yw2application.view.main.work.tool.return_tools.ReturnToolsActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.GlideUtils;

/**
 * 工具详情
 * Created by zhangan on 2018/4/4.
 */

public class ToolsDetailActivity extends BaseActivity implements ToolsDetailContract.View {

    private ToolsDetailContract.Presenter mPresenter;
    private Tools tools;
    private boolean isCustomer;
    private LinearLayout llCustomer, llBorrow;
    private static final int REQUEST_BORROW = 100;
    private static final int REQUEST_RETURN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.tools_detail_activity, "详情");
        new ToolsDetailPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        llCustomer = findViewById(R.id.llCustomer);
        llBorrow = findViewById(R.id.llBorrow);
        tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        isCustomer = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        showTools(tools);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvToolsBorrow:
                Intent intent = new Intent(ToolsDetailActivity.this, BorrowToolsActivity.class);
                intent.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, tools);
                startActivityForResult(intent, REQUEST_BORROW);
                break;
            case R.id.tvToolsReturn:
                Intent intentReturn = new Intent(ToolsDetailActivity.this, ReturnToolsActivity.class);
                intentReturn.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, tools);
                startActivityForResult(intentReturn, REQUEST_RETURN);
                break;
            case R.id.tvToolsCustomerAsk:
                new MaterialDialog.Builder(this)
                        .content("是否确认发送催还消息?")
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mPresenter.askReturn(tools.getToolsLog().getLogId());
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void showTools(final Tools tools) {
        llBorrow.setVisibility(View.GONE);
        ImageView ivToolsPhoto = findViewById(R.id.ivToolsPhoto);
        ivToolsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagePhotoActivity.startActivity(ToolsDetailActivity.this, new String[]{tools.getToolPic()}, 0);
            }
        });
        GlideUtils.ShowImage(this, tools.getToolPic(), ivToolsPhoto, R.drawable.picture_default);
        TextView tvToolsName = findViewById(R.id.tvToolsName);
        TextView tvToolsState = findViewById(R.id.tvToolsState);
        tvToolsName.setText(tools.getToolName());
        if (isCustomer) {
            llCustomer.setVisibility(View.VISIBLE);
        } else {
            llCustomer.setVisibility(View.GONE);
        }
        TextView tvToolsCustomerLocal = findViewById(R.id.tvToolsCustomerLocal);
        TextView tvToolsBorrow = findViewById(R.id.tvToolsBorrow);
        TextView tvToolsIn = findViewById(R.id.tvToolsIn);
        TextView tvToolsReturn = findViewById(R.id.tvToolsReturn);
        TextView tvToolsCustomerAsk = findViewById(R.id.tvToolsCustomerAsk);

        TextView tvToolsUse = findViewById(R.id.tvToolsUse);
        TextView tvToolsUseTime = findViewById(R.id.tvToolsUseTime);
        TextView tvToolsReturnTime = findViewById(R.id.tvToolsReturnTime);

        tvToolsBorrow.setOnClickListener(this);
        tvToolsReturn.setOnClickListener(this);
        tvToolsCustomerAsk.setOnClickListener(this);

        switch (tools.getToolStatus()) {
            case "1":
                tvToolsState.setText("状态:正常");
                tvToolsState.setTextColor(findColorById(R.color.colorTextGreen));
                if (isCustomer) {
                    if (tools.getIsUse().equals("0")) {
                        tvToolsCustomerLocal.setText("在库");
                        tvToolsCustomerLocal.setTextColor(findColorById(R.color.colorTextBlue));
                        tvToolsBorrow.setVisibility(View.VISIBLE);
                        tvToolsIn.setVisibility(View.GONE);
                        tvToolsReturn.setVisibility(View.GONE);
                        tvToolsCustomerAsk.setVisibility(View.GONE);
                    } else {
                        tvToolsCustomerLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                        tvToolsCustomerLocal.setText(String.format("外借  借用人:%s", tools.getUseUser()));
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.GONE);
                        tvToolsReturn.setVisibility(View.VISIBLE);
                        if (tools.getToolsLog() != null && tools.getToolsLog().getPreReturnTime() < System.currentTimeMillis()) {
                            tvToolsCustomerAsk.setVisibility(View.VISIBLE);
                            llBorrow.setVisibility(View.VISIBLE);
                            tvToolsUse.setText("用途:" + tools.getToolsLog().getUse());
                            tvToolsUseTime.setText("借用时间:" + DataUtil.timeFormat(tools.getToolsLog().getUseTime(), "yyyy-MM-dd HH:mm"));
                            tvToolsReturnTime.setText("预计归还时间:" + DataUtil.timeFormat(tools.getToolsLog().getPreReturnTime(), "yyyy-MM-dd HH:mm"));
                        } else {
                            tvToolsCustomerAsk.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case "2":
                tvToolsState.setText("状态:遗失");
                tvToolsState.setTextColor(findColorById(R.color.colorTextYellow));
                if (isCustomer) {
                    if (tools.getIsUse().equals("0")) {
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.VISIBLE);
                        tvToolsReturn.setVisibility(View.GONE);
                        tvToolsCustomerAsk.setVisibility(View.GONE);
                    } else {
                        tvToolsCustomerLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                        tvToolsCustomerLocal.setText(String.format("外借  借用人:%s", tools.getUseUser()));
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.GONE);
                        tvToolsReturn.setVisibility(View.GONE);
                        tvToolsCustomerAsk.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                tvToolsState.setText("状态:损坏");
                tvToolsState.setTextColor(findColorById(R.color.colorTextRed));
                if (isCustomer) {
                    if (tools.getIsUse().equals("0")) {
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.VISIBLE);
                        tvToolsReturn.setVisibility(View.GONE);
                        tvToolsCustomerAsk.setVisibility(View.GONE);
                    } else {
                        tvToolsCustomerLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                        tvToolsCustomerLocal.setText(String.format("外借  借用人:%s", tools.getUseUser()));
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.GONE);
                        tvToolsReturn.setVisibility(View.GONE);
                        tvToolsCustomerAsk.setVisibility(View.GONE);
                    }
                }
                break;
        }
        TextView tvToolsLocal = findViewById(R.id.tvToolsLocal);
        if (isCustomer) {
            tvToolsLocal.setVisibility(View.GONE);
        } else {
            if (tools.getIsUse().equals("0")) {
                tvToolsLocal.setTextColor(findColorById(R.color.colorTextBlue));
                tvToolsLocal.setText("在库");
            } else {
                tvToolsLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                tvToolsLocal.setText(String.format("借用人:%s", tools.getUseUser()));
            }
        }
        TextView tvToolsNum = findViewById(R.id.tvToolsNum);
        tvToolsNum.setText(String.format("设备编号:%s", tools.getToolNumber()));
        TextView tvToolsDes = findViewById(R.id.tvToolsDes);
        tvToolsDes.setText(tools.getToolDesc());
        TextView tvToolsBuyTime = findViewById(R.id.tvToolsBuyTime);
        TextView tvToolsM = findViewById(R.id.tvToolsM);
        TextView tvToolsCount = findViewById(R.id.tvToolsCount);
        TextView tvToolsUnitPrice = findViewById(R.id.tvToolsUnitPrice);
        if (tools.getBuyTime() != 0) {
            tvToolsBuyTime.setText("购买时间:");
        } else {
            tvToolsBuyTime.setText("购买时间:" + tools.getBuyTime());
        }
        if (TextUtils.isEmpty(tools.getManufacturer())) {
            tvToolsM.setText("生产厂家:");
        } else {
            tvToolsM.setText("生产厂家:" + tools.getManufacturer());
        }
        tvToolsCount.setText("数量:" + tools.getToolCount() + "个");
        tvToolsUnitPrice.setText("单价:" + tools.getUnitPrice() + "元");
    }

    @Override
    public void getToolsError() {

    }

    @Override
    public void askSuccess() {
        Yw2Application.getInstance().showToast("发送成功");
    }

    @Override
    public void askError() {

    }

    @Override
    public void setPresenter(ToolsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private boolean stateChange;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RETURN && Activity.RESULT_OK == resultCode) {
            tools.setIsUse("0");
            showTools(tools);
            //归还
            stateChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (stateChange) {
            setResult(Activity.RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    public void toolBarClick() {
        if (stateChange) {
            setResult(Activity.RESULT_OK);
        }
        super.toolBarClick();
    }
}
