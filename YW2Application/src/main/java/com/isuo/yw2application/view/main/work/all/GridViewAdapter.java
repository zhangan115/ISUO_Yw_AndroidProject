package com.isuo.yw2application.view.main.work.all;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;
import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.work.WorkItem;

import java.util.ArrayList;
import java.util.List;

/**
 * grid view adapter
 * Created by zhangan on 2018/3/26.
 */
public class GridViewAdapter extends BaseAdapter implements OnItemMovedListener {
    private Context context;
    private ArrayList<WorkItem> mDatas = new ArrayList<>();
    private int layout;
    private IShowWorkStateChange showWorkStateChange;

    GridViewAdapter(Context context, List<WorkItem> workItems, int layout, IShowWorkStateChange showWorkStateChange) {
        this.context = context;
        this.mDatas.clear();
        this.mDatas.addAll(workItems);
        this.layout = layout;
        this.showWorkStateChange = showWorkStateChange;
    }

    public void setData(List<WorkItem> dataList) {
        this.mDatas.clear();
        this.mDatas.addAll(dataList);
        notifyDataSetChanged();
    }

    void setInEditMode() {
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
        return position;
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
            viewHolder.add_bg = convertView.findViewById(R.id.llAddBg);
            viewHolder.llWorkItem = convertView.findViewById(R.id.llWorkItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mDatas.get(position).getId() == -1) {
            if (getCount() < 8 && mDatas.get(position).isEdit()) {
                viewHolder.add_bg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.add_bg.setVisibility(View.INVISIBLE);
            }
            viewHolder.llWorkItem.setVisibility(View.GONE);
            viewHolder.bg.setVisibility(View.GONE);
            viewHolder.state.setVisibility(View.GONE);
        } else {
            viewHolder.llWorkItem.setVisibility(View.VISIBLE);
            viewHolder.add_bg.setVisibility(View.GONE);
            viewHolder.icon.setImageDrawable(context.getResources().getDrawable(mDatas.get(position).getIcon()));
            viewHolder.text.setText(mDatas.get(position).getName());
            if (mDatas.get(position).isAdd()) {
                viewHolder.state.setImageDrawable(context.getResources().getDrawable(R.drawable.reduce_icon));
            } else {
                viewHolder.state.setImageDrawable(context.getResources().getDrawable(R.drawable.add_icon));
            }
            if (mDatas.get(position).isEdit()) {
                viewHolder.state.setVisibility(View.VISIBLE);
                viewHolder.bg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bg.setVisibility(View.GONE);
                viewHolder.state.setVisibility(View.GONE);
            }
        }
        viewHolder.state.setTag(position);
        viewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (showWorkStateChange != null) {
                    showWorkStateChange.onShowWorkStateChange(position);
                }
            }
        });
        return convertView;
    }

    @Override
    public void onItemMoved(int from, int to) {
        WorkItem s = mDatas.remove(from);
        mDatas.add(to, s);
        if (showWorkStateChange != null) {
            showWorkStateChange.onItemMoved(from, to);
        }
    }

    @Override
    public boolean isFixed(int position) {
        return false;
    }

    class ViewHolder {
        ImageView icon;
        TextView text;
        ImageView state;
        LinearLayout bg;
        LinearLayout add_bg;
        LinearLayout llWorkItem;
    }

    interface IShowWorkStateChange {

        void onShowWorkStateChange(int position);

        void onItemMoved(int from, int to);
    }

}
