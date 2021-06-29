package com.isuo.yw2application.view.main.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.work.WorkItem;

import java.util.ArrayList;
import java.util.List;

/**
 * grid view adapter
 * Created by zhangan on 2018/3/26.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WorkItem> mDatas = new ArrayList<>();
    private int layout;

    GridViewAdapter(Context context, List<WorkItem> workItems, int layout) {
        this.context = context;
        this.mDatas.clear();
        this.mDatas.addAll(workItems);
        this.layout = layout;
    }

    public void setData(List<WorkItem> dataList) {
        this.mDatas.clear();
        this.mDatas.addAll(dataList);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public WorkItem getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.text = convertView.findViewById(R.id.tvWorkName);
            viewHolder.icon = convertView.findViewById(R.id.ivWorkIcon);
            viewHolder.state = convertView.findViewById(R.id.ivState);
            viewHolder.bg = convertView.findViewById(R.id.llEditBg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setImageDrawable(context.getResources().getDrawable(mDatas.get(position).getIcon()));
        viewHolder.text.setText(mDatas.get(position).getName());
        viewHolder.bg.setVisibility(View.GONE);
        viewHolder.state.setVisibility(View.GONE);
        viewHolder.state.setTag(position);
        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
        ImageView state;
        LinearLayout bg;
    }

}
