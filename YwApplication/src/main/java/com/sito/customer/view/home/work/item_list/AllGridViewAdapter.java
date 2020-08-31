package com.sito.customer.view.home.work.item_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;
import com.iflytek.cloud.thirdparty.V;
import com.sito.customer.R;
import com.sito.customer.mode.bean.work.WorkItem;

import java.util.ArrayList;
import java.util.List;

/**
 * grid view adapter
 * Created by zhangan on 2018/3/26.
 */
public class AllGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WorkItem> mDatas = new ArrayList<>();
    private int layout;
    private IStateChange iStateChange;

    AllGridViewAdapter(Context context, List<WorkItem> workItems, int layout, IStateChange iStateChange) {
        this.context = context;
        this.mDatas.clear();
        this.mDatas.addAll(workItems);
        this.layout = layout;
        this.iStateChange = iStateChange;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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
        viewHolder.state.setTag(position);
        viewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iStateChange != null) {
                    int position = (int) v.getTag();
                    iStateChange.onStateChange(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView text;
        ImageView state;
        LinearLayout bg;
    }

    interface IStateChange {

        void onStateChange(int position);
    }

}
