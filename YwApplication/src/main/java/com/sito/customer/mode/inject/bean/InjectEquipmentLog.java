package com.sito.customer.mode.inject.bean;

import com.sito.customer.mode.bean.User;

import java.util.List;

/**
 * Created by zhangan on 2018/4/13.
 */

public class InjectEquipmentLog {

    private int firstResult;
    private int pageNumber;
    private int totalCount;

    private List<ItemList> list;

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ItemList> getList() {
        return list;
    }

    public void setList(List<ItemList> list) {
        this.list = list;
    }

    public static class ItemList {
        private long createTime;
        private long id;
        private long nextTime;
        private User user;
        private int beforeOrBack;
        private List<InjectionOilDataList> injectionOilDataList;

        public int getBeforeOrBack() {
            return beforeOrBack;
        }

        public void setBeforeOrBack(int beforeOrBack) {
            this.beforeOrBack = beforeOrBack;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getNextTime() {
            return nextTime;
        }

        public void setNextTime(long nextTime) {
            this.nextTime = nextTime;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<InjectionOilDataList> getInjectionOilDataList() {
            return injectionOilDataList;
        }

        public void setInjectionOilDataList(List<InjectionOilDataList> injectionOilDataList) {
            this.injectionOilDataList = injectionOilDataList;
        }

        public static class InjectionOilDataList {
            private long dataId;
            private long dateValue;
            private String textValue;
            private String radioValue;
            private String radioContent;
            private String selectValue;
            private String selectContent;
            private String numberValue;

            private InjectOilBean injectionOilItem;

            public long getDataId() {
                return dataId;
            }

            public void setDataId(long dataId) {
                this.dataId = dataId;
            }

            public long getDateValue() {
                return dateValue;
            }

            public void setDateValue(long dateValue) {
                this.dateValue = dateValue;
            }

            public String getTextValue() {
                return textValue;
            }

            public void setTextValue(String textValue) {
                this.textValue = textValue;
            }

            public String getRadioValue() {
                return radioValue;
            }

            public void setRadioValue(String radioValue) {
                this.radioValue = radioValue;
            }

            public String getRadioContent() {
                return radioContent;
            }

            public void setRadioContent(String radioContent) {
                this.radioContent = radioContent;
            }

            public String getSelectValue() {
                return selectValue;
            }

            public void setSelectValue(String selectValue) {
                this.selectValue = selectValue;
            }

            public String getSelectContent() {
                return selectContent;
            }

            public void setSelectContent(String selectContent) {
                this.selectContent = selectContent;
            }

            public String getNumberValue() {
                return numberValue;
            }

            public void setNumberValue(String numberValue) {
                this.numberValue = numberValue;
            }

            public InjectOilBean getInjectionOilItem() {
                return injectionOilItem;
            }

            public void setInjectionOilItem(InjectOilBean injectionOilItem) {
                this.injectionOilItem = injectionOilItem;
            }
        }
    }
}
