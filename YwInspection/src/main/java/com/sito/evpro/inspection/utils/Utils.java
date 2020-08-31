package com.sito.evpro.inspection.utils;

import android.content.Context;
import android.nfc.NdefRecord;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sito.evpro.inspection.R;

import java.nio.charset.Charset;
import java.util.Locale;


/**
 * 作者：Yangzb on 2017/6/30 13:36
 * 邮箱：yangzongbin@si-top.com
 */
public class Utils {

    public static void showToast(Context context, String content, int duration, int gravity) {
        LayoutInflater mLaoutInflater = (LayoutInflater) (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View layout = mLaoutInflater.inflate(R.layout.my_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText(content);
        Toast toast = new Toast(context);
        if (gravity != -1) {
            toast.setGravity(gravity, 0, 0);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, dip2px(context, 70));
        }
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static NdefRecord newTextRecord(String text, Locale locale,
                                           boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(
                Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
                .forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], data);
    }
}
