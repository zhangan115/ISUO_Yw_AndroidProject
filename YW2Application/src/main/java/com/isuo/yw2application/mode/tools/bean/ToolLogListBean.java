package com.isuo.yw2application.mode.tools.bean;

import com.isuo.yw2application.mode.bean.User;

import java.util.List;

public class ToolLogListBean {

    private List<BrowLogBean> list;

    public List<BrowLogBean> getList() {
        return list;
    }

    public void setList(List<BrowLogBean> list) {
        this.list = list;
    }

    public static class BrowLogBean {
        private long logId;
        private int outStatus;
        private long preReturnTime;
        private long useTime;
        private long returnTime;
        private String use;
        private String view;
        private String picUrl;
        private User useUser;

        public long getLogId() {
            return logId;
        }

        public void setLogId(long logId) {
            this.logId = logId;
        }

        public int getOutStatus() {
            return outStatus;
        }

        public void setOutStatus(int outStatus) {
            this.outStatus = outStatus;
        }

        public long getPreReturnTime() {
            return preReturnTime;
        }

        public void setPreReturnTime(long preReturnTime) {
            this.preReturnTime = preReturnTime;
        }

        public long getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(long returnTime) {
            this.returnTime = returnTime;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public User getUseUser() {
            return useUser;
        }

        public long getUseTime() {
            return useTime;
        }

        public void setUseTime(long useTime) {
            this.useTime = useTime;
        }

        public void setUseUser(User useUser) {
            this.useUser = useUser;
        }
    }
}


