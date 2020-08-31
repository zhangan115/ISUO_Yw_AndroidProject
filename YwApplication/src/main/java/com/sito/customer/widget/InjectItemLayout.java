package com.sito.customer.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.customer.R;
import com.sito.customer.mode.inject.bean.InjectOilBean;
import com.sito.library.utils.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * inject item
 * Created by zhangan on 2018/4/13.
 */

public class InjectItemLayout extends LinearLayout implements View.OnClickListener {

    private TextView tvValue;
    private EditText etValue;
    private Calendar mCreateCalender;
    private InjectOilBean bean;
    private Context context;

    public InjectItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.inject_item_layout, this);
        tvValue = findViewById(R.id.tvValue);
        etValue = findViewById(R.id.etValue);
        mCreateCalender = Calendar.getInstance(Locale.CHINA);
    }

    public void setData(InjectOilBean bean) {
        this.bean = bean;
        TextView textView = findViewById(R.id.tvName);
        textView.setText(bean.getItemName());
        switch (bean.getItemType()) {
            case 1:
                tvValue.setVisibility(GONE);
                etValue.setVisibility(VISIBLE);
                etValue.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                etValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
                etValue.addTextChangedListener(new MyTextWatcher());
                break;
            case 2:
            case 3:
                tvValue.setVisibility(VISIBLE);
                etValue.setVisibility(GONE);
                setOnClickListener(this);
                break;
            case 4:
                tvValue.setVisibility(GONE);
                etValue.setVisibility(VISIBLE);
                etValue.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                etValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                etValue.addTextChangedListener(new MyTextWatcher());
                break;
            case 5:
                tvValue.setVisibility(VISIBLE);
                etValue.setVisibility(GONE);
                setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (bean.getItemType() == 5) {
            new DatePickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                    mCreateCalender.set(year, month, dayOfMonth);
                    String time = getDataStr(mCreateCalender);
                    bean.setValue(time);
                    tvValue.setText(time);
                }
            }, mCreateCalender.get(Calendar.YEAR), mCreateCalender.get(Calendar.MONTH)
                    , mCreateCalender.get(Calendar.DAY_OF_MONTH))
                    .show();
        } else if (bean.getItemType() == 2 || bean.getItemType() == 3) {
            if (bean.getOptionList() == null || bean.getOptionList().size() == 0) {
                return;
            }
            List<String> list = new ArrayList<>();
            for (int i = 0; i < bean.getOptionList().size(); i++) {
                list.add(bean.getOptionList().get(i).getOptionContent());
            }
            new MaterialDialog.Builder(this.context).items(list)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            bean.setValue(String.valueOf(bean.getOptionList().get(position).getOptionId()));
                            tvValue.setText(text);
                        }
                    }).show();
        }
    }

    private String getDataStr(Calendar calendar) {
        return DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
    }


    private class MyTextWatcher implements TextWatcher {

        MyTextWatcher() {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                return;
            }
            if (bean.getItemType() == 1) {
                return;
            }
            String older = s.subSequence(0, start).toString();
            String enter = s.subSequence(start, s.length()).toString();
            boolean isWatcher = enter.matches("[0-9.-]+");
            boolean hasPoint = older.contains(".") && enter.contains(".");
            boolean startPoint = s.toString().startsWith(".");
            boolean hasMinus;
            hasMinus = older.contains("-") && enter.contains("-")
                    || older.length() > 0 && enter.contains("-");
            if (startPoint) {
                etValue.setText("");
                if (etValue.getText().length() == 0)
                    return;
                return;
            }
            if (!isWatcher || hasPoint || hasMinus) {
                etValue.setText(older);
                if (etValue.getText().length() == 0) {
                    return;
                }
                etValue.setSelection(older.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (bean != null) {
                bean.setValue(editable.toString());
            }
        }
    }
}
