package com.sito.customer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页统计
 * Created by zhangan on 2018/3/27.
 */

public class CountWorkLayout extends LinearLayout implements View.OnClickListener {

    public CountWorkLayout(Context context) {
        super(context);
        init(context);
    }

    ItemClickListener clickListener;

    private void init(Context context) {
        inflate(context, R.layout.layout_count_work, this);
    }

    @SuppressLint("SetTextI18n")
    public void setData(CountWorkBean countWorkBean, ItemClickListener clickListener) {
        this.clickListener = clickListener;
        TextView countName = findViewById(R.id.tvCountName);
        countName.setText(countWorkBean.getTitle());
        List<CountTypeTv> countTypeTvs = new ArrayList<>();
        TextView countType1 = findViewById(R.id.tvCountType1);
        TextView typeCount1 = findViewById(R.id.tvTypeCount1);
        TextView typeAll1 = findViewById(R.id.tvTypeAll1);
        countTypeTvs.add(new CountTypeTv(countType1, typeCount1, typeAll1));
        TextView countType2 = findViewById(R.id.tvCountType2);
        TextView typeCount2 = findViewById(R.id.tvTypeCount2);
        TextView typeAll2 = findViewById(R.id.tvTypeAll2);
        countTypeTvs.add(new CountTypeTv(countType2, typeCount2, typeAll2));
        TextView countType3 = findViewById(R.id.tvCountType3);
        TextView typeCount3 = findViewById(R.id.tvTypeCount3);
        TextView typeAll3 = findViewById(R.id.tvTypeAll3);
        countTypeTvs.add(new CountTypeTv(countType3, typeCount3, typeAll3));
        TextView countType4 = findViewById(R.id.tvCountType4);
        TextView typeCount4 = findViewById(R.id.tvTypeCount4);
        TextView typeAll4 = findViewById(R.id.tvTypeAll4);
        countTypeTvs.add(new CountTypeTv(countType4, typeCount4, typeAll4));
        for (int i = 0; i < countWorkBean.getCountTypeBeans().size(); i++) {
            countTypeTvs.get(i).getType().setText(countWorkBean.getCountTypeBeans().get(i).getName());
            countTypeTvs.get(i).getTypeCount().setText(String.valueOf(countWorkBean.getCountTypeBeans().get(i).getFinishCount()));
            if (countWorkBean.getCountTypeBeans().get(i).getAllCount() != -1) {
                countTypeTvs.get(i).getTypeAll().setVisibility(VISIBLE);
                countTypeTvs.get(i).getTypeAll().setText("/" + countWorkBean.getCountTypeBeans().get(i).getAllCount());
            } else {
                countTypeTvs.get(i).getTypeAll().setVisibility(GONE);
            }
        }
        findViewById(R.id.ll_1).setTag(String.valueOf(countWorkBean.getCountTypeBeans().get(0).getType()));
        findViewById(R.id.ll_2).setTag(String.valueOf(countWorkBean.getCountTypeBeans().get(1).getType()));
        findViewById(R.id.ll_3).setTag(String.valueOf(countWorkBean.getCountTypeBeans().get(2).getType()));
        findViewById(R.id.ll_1).setOnClickListener(this);
        findViewById(R.id.ll_2).setOnClickListener(this);
        findViewById(R.id.ll_3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            String type = (String) v.getTag();
            clickListener.onItem(type);
        }
    }

    public interface ItemClickListener {

        void onItem(String type);
    }

    public static class CountWorkBean {

        private String title;
        private int type;
        private List<CountTypeBean> countTypeBeans;

        public CountWorkBean() {
        }

        public CountWorkBean(String title, int type, List<CountTypeBean> countTypeBeans) {
            this.title = title;
            this.type = type;
            this.countTypeBeans = countTypeBeans;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<CountTypeBean> getCountTypeBeans() {
            return countTypeBeans;
        }

        public void setCountTypeBeans(List<CountTypeBean> countTypeBeans) {
            this.countTypeBeans = countTypeBeans;
        }

        public static class CountTypeBean {
            public CountTypeBean() {
            }

            public CountTypeBean(int type, String name, int finishCount, int allCount) {
                this.name = name;
                this.type = type;
                this.finishCount = finishCount;
                this.allCount = allCount;
            }

            private int type;
            private String name;
            private int finishCount;
            private int allCount;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getFinishCount() {
                return finishCount;
            }

            public void setFinishCount(int finishCount) {
                this.finishCount = finishCount;
            }

            public int getAllCount() {
                return allCount;
            }

            public void setAllCount(int allCount) {
                this.allCount = allCount;
            }
        }
    }

    private static class CountTypeTv {
        private TextView type;
        private TextView typeCount;
        private TextView typeAll;

        public CountTypeTv() {
        }

        public CountTypeTv(TextView type, TextView typeCount, TextView typeAll) {
            this.type = type;
            this.typeCount = typeCount;
            this.typeAll = typeAll;
        }

        public TextView getType() {
            return type;
        }

        public void setType(TextView type) {
            this.type = type;
        }

        public TextView getTypeCount() {
            return typeCount;
        }

        public void setTypeCount(TextView typeCount) {
            this.typeCount = typeCount;
        }

        public TextView getTypeAll() {
            return typeAll;
        }

        public void setTypeAll(TextView typeAll) {
            this.typeAll = typeAll;
        }
    }

}
