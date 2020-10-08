package com.isuo.yw2application.view.main.task.inspection.work;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtilsV4;

import java.util.Locale;

/**
 * 巡检工作界面
 * Created by zhangan on 2018/3/20.
 */

public class InspectionRoomActivity extends BaseActivity {

    IFindRoomListener findRoomListener;
    @Nullable
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage ndefMessage;
    private String taskStartType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.inspection_room_activity, "任务详情");
        long taskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        //获取开始状态
        if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                && Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
            for (int i = 0; i < Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("startType")) {
                    taskStartType = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(taskStartType)) {
            taskStartType = ConstantStr.START_TYPE_0;
        }
        if (!TextUtils.isEmpty(taskStartType) && taskStartType.equals(ConstantStr.START_TYPE_2)) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            //拦截系统级的NFC扫描，例如扫描蓝牙
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            ndefMessage = new NdefMessage(new NdefRecord[]{Utils.newTextRecord("",
                    Locale.ENGLISH, true)});
            if (nfcAdapter == null) {
                Yw2Application.getInstance().showToast("当前设备不支持NFC");
            } else if (!nfcAdapter.isEnabled()) {
                new MaterialDialog.Builder(this)
                        .content("请打开NFC功能")
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        })
                        .show();
            }
        }
        InspectionRoomFragment fragment = (InspectionRoomFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
        if (fragment == null) {
            fragment = InspectionRoomFragment.newInstance(taskId);
            ActivityUtilsV4.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frame);
        }
        processAdapterAction(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            //隐式启动
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            nfcAdapter.enableForegroundNdefPush(this, ndefMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }

    //获取系统隐式启动的
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        processAdapterAction(intent);
    }

    public void processAdapterAction(Intent intent) {
        // 当系统检测到tag中含有NDEF格式的数据时，且系统中有activity声明可以接受包含NDEF数据的Intent的时候，系统会优先发出这个action的intent。
        // 得到是否检测到ACTION_NDEF_DISCOVERED触发 序号1
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            return;
        }
        // 当没有任何一个activity声明自己可以响应ACTION_NDEF_DISCOVERED时，系统会尝试发出TECH的intent.即便你的tag中所包含的数据是NDEF的，但是如果这个数据的MIME
        // type或URI不能和任何一个activity所声明的想吻合，系统也一样会尝试发出tech格式的intent，而不是NDEF.
        // 得到是否检测到ACTION_TECH_DISCOVERED触发 序号2
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            return;
        }
        // 当系统发现前两个intent在系统中无人会接受的时候，就只好发这个默认的TAG类型的
        // 得到是否检测到ACTION_TAG_DISCOVERED触发 序号3
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
        }
    }

    @Nullable
    private String read(Tag tag) throws Exception {
        if (tag != null) {
            //解析Tag获取到NDEF实例
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }
            //打开连接
            ndef.connect();
            //获取NDEF消息
            NdefMessage message = ndef.getNdefMessage();
            if (message != null) {
                //将消息转换成字节数组
                byte[] data = message.toByteArray();
                //将字节数组转换成字符串
                String str = new String(data, "UTF-8");
                //关闭连接
                ndef.close();
                if (str.length() > 7) {
                    str = str.substring(7);
                }
                return str;
            }
        } else {
            Yw2Application.getInstance().showToast("设备与nfc卡连接断开，请重新连接...");
        }
        return null;
    }

    private void processIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String roomId = "";
        try {
            roomId = read(tagFromIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findRoom(roomId);
    }

    public void setFindRoomListener(IFindRoomListener findRoomListener) {
        this.findRoomListener = findRoomListener;
    }

    /**
     * 查找配电室
     *
     * @param s 参数
     */
    private void findRoom(String s) {
        if (findRoomListener == null) return;
        if (!findRoomListener.findRoom(s)) {
            Yw2Application.getInstance().showToast("没有找到配电室");
        }
    }
}
