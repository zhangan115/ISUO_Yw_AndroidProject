package com.sito.customer.widget.treeadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sito.customer.R;

import java.util.List;


public class TreeAdapter<T extends RvTree> extends RecyclerView.Adapter<TreeAdapter.TreeViewHolder> {
    private static final int PADDING = 30;
    private Context mContext;
    private List<Node<T>> mNodes;
    private TreeItemClickListener mListener;

    /**
     * 要在{@link #setNodes(List)}之后才会刷新数据
     */
    public TreeAdapter(Context context) {
        mContext = context;
    }

    public TreeAdapter(Context context, List<T> data) {
        mContext = context;
        setNodes(data);
    }

    public void setListener(TreeItemClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 获取指定下标节点的源数据
     *
     * @param index 指定的下标
     */
    public T getItem(int index) {
        return mNodes.get(index).getItem();
    }

    /**
     * 获取指定id的源数据
     *
     * @param id 指定的id
     */
    public T getItemById(int id) {
        for (Node<T> node : mNodes) {
            if (node.getId() == id) {
                return node.getItem();
            }
        }
        return null;
    }

    /**
     * 需要传入数据才会刷新treeview，默认所有节点都会折叠，只保留根节点
     */
    public void setNodes(List<T> data) {
        List<Node<T>> allNodes = TreeHelper.getSortedNode(data, 0);
        mNodes = TreeHelper.<T>filterVisibleNode(allNodes);
    }

    /**
     * 在给定下标的节点处添加子节点，并展开该节点
     * 注意执行该方法是强制将数据挂在指定节点下
     * 如子节点的实际父节点并非指定节点，则在下一次调用{@link #setNodes(List)}时会重新调整
     *
     * @param index 将数据添加到该下标的节点处
     * @param data  要添加的数据
     */
    public void addChildrenByIndex(int index, List<T> data) {
        Node<T> parent = mNodes.get(index);
        List<Node<T>> childNodes = TreeHelper.getSortedNode(data, 0);
        List<Node<T>> children = parent.getChildren();
        parent.setExpand(true);
        for (Node<T> node : childNodes) {
            node.setParent(parent);
            children.add(node);
        }
        // 展开节点
        int count = addChildNodes(parent, index + 1);
        notifyItemChanged(index);
        notifyItemRangeInserted(index + 1, count);
    }

    /**
     * 向指定id节点下添加子节点，并展开该节点
     * 注意执行该方法是强制将数据挂在指定节点下
     * 如子节点的实际父节点并非指定节点，则在下一次调用{@link #setNodes(List)}时会重新调整
     *
     * @param id   作为父节点的id
     * @param data 要添加的数据
     */
    public void addChildrenById(int id, List<T> data) {
        int index = findNode(id);
        if (index != -1) {
            addChildrenByIndex(index, data);
        }
    }

    /**
     * 根据id查找指定节点
     *
     * @param id 需要查找的节点id
     * @return 查找成功的下标，若不存在该节点，返回-1
     */
    public int findNode(int id) {
        int size = mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = mNodes.get(i);
            if (node.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public TreeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.rv_item_tree, parent, false);
        return new TreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TreeAdapter.TreeViewHolder holder, int position) {
        Node node = mNodes.get(position);
//        holder.itemView.setPadding(node.getLevel() * PADDING, 3, 3, 3);
        holder.setControl(node);
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }

    class TreeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView detail;

        private TreeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rv_item_tree_title);
            detail = itemView.findViewById(R.id.tv_item_tree_detail);
            itemView.setOnClickListener(this);
            itemView.setTag(System.currentTimeMillis());
        }

        private void setControl(Node node) {
            title.setText(node.getName());
            title.setPadding(node.getLevel() * PADDING, 3, 3, 3);
            if (node.isLeaf()) {
                detail.setVisibility(View.INVISIBLE);
            } else {
                detail.setVisibility(View.VISIBLE);
            }
            if (node.isExpand()) {
                detail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_down));
            } else {
                detail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_up));
            }

        }

        @Override
        public void onClick(View view) {
            Node<T> node = mNodes.get(getLayoutPosition());
            if (node != null && !node.isLeaf()) {
                long lastClickTime = (long) itemView.getTag();
                // 避免过快点击
                if (System.currentTimeMillis() - lastClickTime < 200) {
                    return;
                }
                itemView.setTag(System.currentTimeMillis());
                boolean isExpand = node.isExpand();
                node.setExpand(!isExpand);
                if (!isExpand) {
                    detail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_down));
                    notifyItemRangeInserted(getLayoutPosition() + 1, addChildNodes(node, getLayoutPosition() + 1));
                } else {
                    detail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_up));
                    notifyItemRangeRemoved(getLayoutPosition() + 1, removeChildNodes(node));
                }
            } else {
                if (mListener != null) {
                    mListener.OnClick(node);
                }
            }
        }
    }

    private int addChildNodes(Node<T> n, int startIndex) {
        List<Node<T>> childList = n.getChildren();
        int addChildCount = 0;
        for (Node<T> node : childList) {
            mNodes.add(startIndex + addChildCount++, node);
            // 递归展开其下要展开的子元素
            if (node.isExpand()) {
                addChildCount += addChildNodes(node, startIndex + addChildCount);
            }
        }
        return addChildCount;
    }

    // 折叠父节点时删除其下所有子元素
    private int removeChildNodes(Node<T> n) {
        if (n.isLeaf()) {
            return 0;
        }

        List<Node<T>> childList = n.getChildren();
        int removeChildCount = childList.size();
        mNodes.removeAll(childList);
        for (Node<T> node : childList) {
            if (node.isExpand()) {
                removeChildCount += removeChildNodes(node);
            }
        }
        return removeChildCount;
    }
}