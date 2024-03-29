package com.sito.evpro.inspection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.SPHelper;

/**
 * Created by zhangan on 2017/8/14.
 */

public class RoomListLayout extends LinearLayout implements View.OnClickListener {

    private ImageView iv_state, iv_end;
    private TextView tv_equip_count, tv_equip_time, tv_equip_name;
    private LinearLayout startTaskLayout, finishTaskLayout;
    private Context context;
    private OnStartListener onStartListener;
    private OnFinishListener onFinishListener;
    private int mPosition;
    private RoomListBean roomListBean;
    private long startTime, finishTime;

    public RoomListLayout(Context context) {
        super(context);
        init(context);
    }

    public RoomListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.item_inspection_detail, this);
        startTaskLayout = (LinearLayout) findViewById(R.id.ll_start_task);
        startTaskLayout.setOnClickListener(this);
        finishTaskLayout = (LinearLayout) findViewById(R.id.ll_finish_task);
        finishTaskLayout.setOnClickListener(this);
        iv_state = (ImageView) findViewById(R.id.iv_start);
        iv_end = (ImageView) findViewById(R.id.iv_end);
        tv_equip_count = (TextView) findViewById(R.id.tv_equip_count);
        tv_equip_time = (TextView) findViewById(R.id.tv_equip_time);
        tv_equip_name = (TextView) findViewById(R.id.tv_equip_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_start_task:
                mPosition = (int) v.getTag(R.id.tag_position);
                roomListBean = (RoomListBean) v.getTag(R.id.tag_object);
                if (onStartListener != null) {
                    onStartListener.onStart(roomListBean, mPosition);
                }
                break;
            case R.id.ll_finish_task:
                mPosition = (int) v.getTag(R.id.tag_position);
                roomListBean = (RoomListBean) v.getTag(R.id.tag_object);
                if (onFinishListener != null) {
                    onFinishListener.onFinish(roomListBean, mPosition);
                }
                break;
        }
    }

    public void setListener(OnStartListener onStartListener, OnFinishListener onFinishListener) {
        this.onStartListener = onStartListener;
        this.onFinishListener = onFinishListener;
    }

    public void setRoomBean(RoomListBean data, int position) {
        roomListBean = data;
        tv_equip_name.setText(data.getRoom().getRoomName());
        startTime = SPHelper.readLong(context
                , ConstantStr.USER_DATA, InspectionApp.getInstance().getCurrentUser().getUserId()
                        + "_" + data.getRoomDb().getTaskId()
                        + "_" + data.getRoomDb().getRoomId() + ConstantStr.ROOM_START_TIME);
        finishTime = SPHelper.readLong(context
                , ConstantStr.USER_DATA, InspectionApp.getInstance().getCurrentUser().getUserId()
                        + "_" + data.getRoomDb().getTaskId()
                        + "_" + data.getRoomDb().getRoomId() + ConstantStr.ROOM_FINISH_TIME);
        startTaskLayout.setTag(R.id.tag_position, position);
        startTaskLayout.setTag(R.id.tag_object, data);
        finishTaskLayout.setTag(R.id.tag_position, position);
        finishTaskLayout.setTag(R.id.tag_object, data);
        if (data.getTaskRoomState() == ConstantInt.ROOM_STATE_1) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_check));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_room_finish));
            tv_equip_time.setText("00:00:00");
        } else if (data.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_doing));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_room_finish));
            if (startTime != 0) {
                tv_equip_time.setText(DataUtil.timeFormat((System.currentTimeMillis() - startTime - 28800 * 1000), "HH:mm:ss"));
            } else {
                tv_equip_time.setText("00:00:00");
            }
        } else if (data.getTaskRoomState() == ConstantInt.ROOM_STATE_3) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.work_start_button_gray_normal));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.work_finish_button));
            if (startTime == 0 || finishTime == 0) {
                tv_equip_time.setText("00:00:00");
            } else {
                tv_equip_time.setText(DataUtil.timeFormat((finishTime - startTime - 28800 * 1000), "HH:mm:ss"));
            }
        }
        String TAT = InspectionApp.getInstance().getCurrentUser().getUserId() +
                "_" + data.getRoomDb().getTaskId() + "_" + data.getRoomDb().getRoomId();
        int count = SPHelper.readInt(context, ConstantStr.USER_DATA, TAT, 0);
        tv_equip_count.setText(count + "/" + data.getTaskEquipment().size());

    }

    public void timer() {
        if (startTime != 0 && roomListBean != null && roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
            tv_equip_time.setText(DataUtil.timeFormat((System.currentTimeMillis() - startTime - 28800 * 1000), "HH:mm:ss"));
        }
    }

    public interface OnStartListener {
        void onStart(RoomListBean data, int position);
    }

    public interface OnFinishListener {
        void onFinish(RoomListBean data, int position);
    }
}
