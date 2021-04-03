package com.isuo.yw2application.view.main.work.tool.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.tools.ToolsRepository;
import com.isuo.yw2application.mode.tools.bean.ToolDetailBean;
import com.isuo.yw2application.mode.tools.bean.ToolLogListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.main.work.tool.borrow.BorrowToolsActivity;
import com.isuo.yw2application.view.main.work.tool.return_tools.ReturnToolsActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.sito.library.adapter.VRVAdapter;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExpendRecycleView;
import com.sito.library.widget.RecycleRefreshLoadLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具详情
 * Created by zhangan on 2018/4/4.
 */

public class ToolsDetailActivity extends BaseActivity implements ToolsDetailContract.View {

    private ToolsDetailContract.Presenter mPresenter;
    private Tools tools;
    private boolean isCustomer;
    private LinearLayout llCustomer;
    private ExpendRecycleView recyclerView;
    private RecycleRefreshLoadLayout refreshLoadLayout;
    private static final int REQUEST_BORROW = 100;
    private static final int REQUEST_RETURN = 101;
    private boolean isRefresh;
    private List<ToolDetailBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.tools_detail_activity, "详情");
        new ToolsDetailPresenter(ToolsRepository.getRepository(this), this);
        mPresenter.subscribe();
        llCustomer = findViewById(R.id.llCustomer);
        tools = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        isCustomer = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        recyclerView = findViewById(R.id.recyclerView);
        @SuppressLint("InflateParams") View loadFooterView = LayoutInflater.from(this).inflate(R.layout.view_load_more_inspection, null);
        refreshLoadLayout = findViewById(R.id.refreshLoadLayout);
        refreshLoadLayout.setViewFooter(loadFooterView);
        refreshLoadLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLoadLayout.setNoMoreData(false);
                recyclerView.setVisibility(View.VISIBLE);
                isRefresh = true;
                mPresenter.getToolBrowList(tools.getToolId());
            }
        });
        refreshLoadLayout.setOnLoadListener(new RecycleRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoadMore() {
                if (!isRefresh && dataList.size() > 0) {
                    mPresenter.getToolBrowListMore(tools.getToolId(), dataList.get(dataList.size() - 1).getBean().getLogId());
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        VRVAdapter<ToolDetailBean> adapter = new VRVAdapter<ToolDetailBean>
                (recyclerView, this.dataList, new int[]{R.layout.item_tool_detail_header, R.layout.item_tool_log_list}) {

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 0;
                }
                return 1;
            }

            @Override
            public void showData(ViewHolder vHolder, final ToolDetailBean data, int position, int type) {
                if (type == 1) {
                    TextView tvToolsUse = (TextView) vHolder.getView(R.id.tvToolsUse);
                    TextView tvToolsUseTime = (TextView) vHolder.getView(R.id.tvToolsUseTime);
                    TextView tvToolsReturnTime = (TextView) vHolder.getView(R.id.tvToolsReturnTime);
                    tvToolsUse.setText(MessageFormat.format("用途:{0}", data.getBean().getUse()));
                    tvToolsUseTime.setText(MessageFormat.format("借用时间:{0}", DataUtil.timeFormat(data.getBean().getUseTime(), "yyyy-MM-dd HH:mm")));
                    if (data.getBean().getReturnTime() > 0) {
                        tvToolsReturnTime.setText(MessageFormat.format("归还时间:{0}", DataUtil.timeFormat(data.getBean().getReturnTime(), "yyyy-MM-dd HH:mm")));
                    } else {
                        tvToolsReturnTime.setText(MessageFormat.format("预计归还时间:{0}", DataUtil.timeFormat(data.getBean().getPreReturnTime(), "yyyy-MM-dd HH:mm")));
                    }
                    LinearLayout layoutNote = (LinearLayout) vHolder.getView(R.id.ll_note);
                    LinearLayout layoutPhoto = (LinearLayout) vHolder.getView(R.id.ll_photo);

                    TextView tvNote = (TextView) vHolder.getView(R.id.tv_content);
                    ImageView ivToolsPhoto = (ImageView) vHolder.getView(R.id.ivToolsPhoto);
                    if (TextUtils.isEmpty(data.getBean().getView())) {
                        layoutNote.setVisibility(View.GONE);
                    } else {
                        layoutNote.setVisibility(View.VISIBLE);
                        tvNote.setText(data.getBean().getView());
                    }
                    if (TextUtils.isEmpty(data.getBean().getPicUrl())) {
                        layoutPhoto.setVisibility(View.GONE);
                    } else {
                        layoutPhoto.setVisibility(View.VISIBLE);
                        GlideUtils.ShowImage(ToolsDetailActivity.this, data.getBean().getPicUrl(), ivToolsPhoto, R.drawable.img_default);
                        ivToolsPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(data.getBean().getPicUrl())) {
                                    ViewPagePhotoActivity.startActivity(ToolsDetailActivity.this, new String[]{data.getBean().getPicUrl()}, 0);
                                }
                            }
                        });
                    }
                    LinearLayout layoutTakeNote = (LinearLayout) vHolder.getView(R.id.ll_take_note);
                    LinearLayout layoutTakePhoto = (LinearLayout) vHolder.getView(R.id.ll_take_photo);
                    TextView tvTakeNote = (TextView) vHolder.getView(R.id.tv_take_content);
                    ImageView ivTakeToolsPhoto = (ImageView) vHolder.getView(R.id.ivTakeToolsPhoto);
                    if (TextUtils.isEmpty(data.getBean().getTakeView())) {
                        layoutTakeNote.setVisibility(View.GONE);
                    } else {
                        layoutTakeNote.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(data.getBean().getTakeView()))
                            tvTakeNote.setText(data.getBean().getTakeView());
                    }
                    if (TextUtils.isEmpty(data.getBean().getTakePicUrl())) {
                        layoutTakePhoto.setVisibility(View.GONE);
                    } else {
                        layoutTakePhoto.setVisibility(View.VISIBLE);
                        GlideUtils.ShowImage(ToolsDetailActivity.this, data.getBean().getTakePicUrl(), ivTakeToolsPhoto, R.drawable.img_default);
                        ivTakeToolsPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(data.getBean().getTakePicUrl())) {
                                    ViewPagePhotoActivity.startActivity(ToolsDetailActivity.this, new String[]{data.getBean().getTakePicUrl()}, 0);
                                }
                            }
                        });
                    }

                } else {
                    setTools(vHolder, data.getTools());
                }
            }
        };
        recyclerView.setAdapter(adapter);
        showTools(tools);
        mPresenter.getToolBrowList(tools.getToolId());
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
        if (this.dataList.size() > 0) {
            ToolDetailBean bean = this.dataList.get(0);
            bean.setTools(tools);
        } else {
            ToolDetailBean bean = new ToolDetailBean();
            bean.setTools(tools);
            this.dataList.add(bean);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setTools(VRVAdapter.ViewHolder viewHolder, final Tools tools) {
        LinearLayout llBorrow = (LinearLayout) viewHolder.getView(R.id.llBorrow);
        ImageView ivToolsPhoto = (ImageView) viewHolder.getView(R.id.ivToolsPhoto);
        ivToolsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagePhotoActivity.startActivity(ToolsDetailActivity.this, new String[]{tools.getToolPic()}, 0);
            }
        });
        GlideUtils.ShowImage(this, tools.getToolPic(), ivToolsPhoto, R.drawable.picture_default);
        TextView tvToolsName = (TextView) viewHolder.getView(R.id.tvToolsName);
        TextView tvToolsState = (TextView) viewHolder.getView(R.id.tvToolsState);
        tvToolsName.setText(tools.getToolName());
        if (isCustomer) {
            llCustomer.setVisibility(View.VISIBLE);
        } else {
            llCustomer.setVisibility(View.GONE);
        }
        TextView tvToolsCustomerLocal = ToolsDetailActivity.this.findViewById(R.id.tvToolsCustomerLocal);
        TextView tvToolsBorrow = ToolsDetailActivity.this.findViewById(R.id.tvToolsBorrow);
        TextView tvToolsIn = ToolsDetailActivity.this.findViewById(R.id.tvToolsIn);
        TextView tvToolsReturn = ToolsDetailActivity.this.findViewById(R.id.tvToolsReturn);
        TextView tvToolsCustomerAsk = ToolsDetailActivity.this.findViewById(R.id.tvToolsCustomerAsk);

        TextView tvToolsUse = (TextView) viewHolder.getView(R.id.tvToolsUse);
        TextView tvToolsUseTime = (TextView) viewHolder.getView(R.id.tvToolsUseTime);
        TextView tvToolsReturnTime = (TextView) viewHolder.getView(R.id.tvToolsReturnTime);

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
                        llBorrow.setVisibility(View.GONE);
                    } else {
                        tvToolsCustomerLocal.setTextColor(findColorById(R.color.colorBtnYellowNormal));
                        tvToolsCustomerLocal.setText(String.format("外借  借用人:%s", tools.getUseUser()));
                        tvToolsBorrow.setVisibility(View.GONE);
                        tvToolsIn.setVisibility(View.GONE);
                        tvToolsReturn.setVisibility(View.VISIBLE);
                        if (tools.getToolsLog() != null && tools.getToolsLog().getPreReturnTime() < System.currentTimeMillis()) {
                            tvToolsCustomerAsk.setVisibility(View.VISIBLE);
                            llBorrow.setVisibility(View.GONE);
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
        TextView tvToolsLocal = (TextView) viewHolder.getView(R.id.tvToolsLocal);
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
        TextView tvToolsNum = (TextView) viewHolder.getView(R.id.tvToolsNum);
        tvToolsNum.setText(String.format("设备编号:%s", tools.getToolNumber()));
        TextView tvToolsDes = (TextView) viewHolder.getView(R.id.tvToolsDes);
        if (!TextUtils.isEmpty(tools.getToolDesc())) {
            tvToolsDes.setVisibility(View.VISIBLE);
            tvToolsDes.setText(tools.getToolDesc());
        } else {
            tvToolsDes.setVisibility(View.GONE);
        }
        TextView tvToolsBuyTime = (TextView) viewHolder.getView(R.id.tvToolsBuyTime);
        TextView tvToolsM = (TextView) viewHolder.getView(R.id.tvToolsM);
        TextView tvToolsCount = (TextView) viewHolder.getView(R.id.tvToolsCount);
        TextView tvToolsUnitPrice = (TextView) viewHolder.getView(R.id.tvToolsUnitPrice);
        if (tools.getBuyTime() != 0) {
            tvToolsBuyTime.setText("购买时间:");
        } else {
            tvToolsBuyTime.setText(MessageFormat.format("购买时间:{0}", tools.getBuyTime()));
        }
        if (TextUtils.isEmpty(tools.getManufacturer())) {
            tvToolsM.setText("生产厂家:");
        } else {
            tvToolsM.setText(MessageFormat.format("生产厂家:{0}", tools.getManufacturer()));
        }
        tvToolsCount.setText(MessageFormat.format("数量:{0}个", tools.getToolCount()));
        tvToolsUnitPrice.setText(MessageFormat.format("单价:{0}元", tools.getUnitPrice()));
        TextView text1 = (TextView) viewHolder.getView(R.id.text1);
        if (dataList.size() == 1) {
            text1.setVisibility(View.GONE);
        } else {
            text1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noToolBrowLog() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void noMoreData() {
        refreshLoadLayout.setNoMoreData(true);
    }

    @Override
    public void refreshFinish() {
        isRefresh = false;
        refreshLoadLayout.setRefreshing(false);
    }

    @Override
    public void showToolBrowList(List<ToolLogListBean.BrowLogBean> list) {
        this.dataList.clear();
        ToolDetailBean bean = new ToolDetailBean();
        bean.setTools(this.tools);
        this.dataList.add(bean);
        for (int i = 0; i < list.size(); i++) {
            ToolDetailBean bean1 = new ToolDetailBean();
            bean1.setBean(list.get(i));
            this.dataList.add(bean1);
        }
        this.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showToolBrowListMore(List<ToolLogListBean.BrowLogBean> list) {
        for (int i = 0; i < list.size(); i++) {
            ToolDetailBean bean1 = new ToolDetailBean();
            bean1.setBean(list.get(i));
            this.dataList.add(bean1);
        }
        if (list.size() < ConstantInt.PAGE_SIZE) {
            this.refreshLoadLayout.setNoMoreData(true);
        } else {
            this.refreshLoadLayout.loadFinish();
        }
        this.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void getToolsError() {
        recyclerView.getAdapter().notifyDataSetChanged();
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
        } else if (requestCode == REQUEST_BORROW && Activity.RESULT_OK == resultCode) {
            if (data != null) {
                String useName = data.getStringExtra(ConstantStr.KEY_BUNDLE_STR);
                tools.setUseUser(useName);
            }
            tools.setIsUse("1");
            showTools(tools);
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
