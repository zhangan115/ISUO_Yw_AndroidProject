package com.isuo.yw2application.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.sito.library.utils.DataUtil;

/**
 * room list item
 * Created by zhangan on 2017/8/14.
 */

public class RoomListLayout extends LinearLayout implements View.OnClickListener {

    private ImageView iv_state, iv_end;
    private TextView tv_equip_count, tv_equip_time, tv_equip_name;
    private LinearLayout startTaskLayout, finishTaskLayout;
    private Context context;
    private OnStartListener onStartListener;
    private OnFinishListener onFinishListener;
    private RoomListBean roomListBean;

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
        startTaskLayout = findViewById(R.id.ll_start_task);
        startTaskLayout.setOnClickListener(this);
        finishTaskLayout = findViewById(R.id.ll_finish_task);
        finishTaskLayout.setOnClickListener(this);
        iv_state = findViewById(R.id.iv_start);
        iv_end = findViewById(R.id.iv_end);
        tv_equip_count = findViewById(R.id.tv_equip_count);
        tv_equip_time = findViewById(R.id.tv_equip_time);
        tv_equip_name = findViewById(R.id.tv_equip_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_start_task:
                int startPosition = (int) v.getTag(R.id.tag_position);
                roomListBean = (RoomListBean) v.getTag(R.id.tag_object);
                if (onStartListener != null) {
                    onStartListener.onStart(roomListBean, startPosition);
                }
                break;
            case R.id.ll_finish_task:
                int finishPosition = (int) v.getTag(R.id.tag_position);
                roomListBean = (RoomListBean) v.getTag(R.id.tag_object);
                if (onFinishListener != null) {
                    onFinishListener.onFinish(roomListBean, finishPosition);
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
        tv_equip_name.setText(data.getRoomDb().getRoomName());
        long startTime = data.getRoomDb().getStartTime();
        long finishTime = data.getRoomDb().getEndTime();
        startTaskLayout.setTag(R.id.tag_position, position);
        startTaskLayout.setTag(R.id.tag_object, data);
        finishTaskLayout.setTag(R.id.tag_position, position);
        finishTaskLayout.setTag(R.id.tag_object, data);
        if (data.getRoomDb().getTaskState() == ConstantInt.ROOM_STATE_1) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_check));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_room_finish));
            tv_equip_time.setText(R.string.zero_time);
        } else if (data.getRoomDb().getTaskState() == ConstantInt.ROOM_STATE_2) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_doing));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_state_room_finish));
            if (startTime != 0) {
                tv_equip_time.setText(DataUtil.timeFormat((System.currentTimeMillis() - startTime - 28800 * 1000), "HH:mm:ss"));
            } else {
                tv_equip_time.setText(R.string.zero_time);
            }
        } else if (data.getRoomDb().getTaskState() == ConstantInt.ROOM_STATE_3) {
            iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.work_start_button_gray_normal));
            iv_end.setImageDrawable(context.getResources().getDrawable(R.drawable.work_finish_button));
            if (startTime == 0 || finishTime == 0) {
                tv_equip_time.setText(R.string.zero_time);
            } else {
                tv_equip_time.setText(DataUtil.timeFormat((finishTime - startTime - 28800 * 1000), "HH:mm:ss"));
            }
        }
        String str = data.getRoomDb().getCheckCount() + "/" + data.getTaskEquipment().size();
        tv_equip_count.setText(str);

    }

    public void timer() {
        if (roomListBean != null && roomListBean.getRoomDb().getStartTime() != 0
                && roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2) {
            tv_equip_time.setText(DataUtil.timeFormat((System.currentTimeMillis()
                    - roomListBean.getRoomDb().getStartTime() - 28800 * 1000), "HH:mm:ss"));
        }
    }

    public interface OnStartListener {
        void onStart(RoomListBean data, int position);
    }

    public interface OnFinishListener {
        void onFinish(RoomListBean data, int position);
    }
}
