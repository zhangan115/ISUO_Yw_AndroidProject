package com.sito.customer.mode.bean.count;

import com.sito.customer.mode.bean.User;

import java.util.List;

/**
 * Created by zhangan on 2018/4/19.
 */

public class PartPersonStatistics {

    private FaultStatBean faultStat;
    private TaskStatBean taskStat;
    private TaskStatBean.TheMostStatBean theMostStat;
    private List<IncrementStatBean> incrementStat;
    private WorkStat workStat;
    private BoardStat boardStat;

    public FaultStatBean getFaultStat() {
        return faultStat;
    }

    public void setFaultStat(FaultStatBean faultStat) {
        this.faultStat = faultStat;
    }

    public TaskStatBean getTaskStat() {
        return taskStat;
    }

    public void setTaskStat(TaskStatBean taskStat) {
        this.taskStat = taskStat;
    }

    public TaskStatBean.TheMostStatBean getTheMostStat() {
        return theMostStat;
    }

    public void setTheMostStat(TaskStatBean.TheMostStatBean theMostStat) {
        this.theMostStat = theMostStat;
    }

    public List<IncrementStatBean> getIncrementStat() {
        return incrementStat;
    }

    public void setIncrementStat(List<IncrementStatBean> incrementStat) {
        this.incrementStat = incrementStat;
    }

    public WorkStat getWorkStat() {
        return workStat;
    }

    public void setWorkStat(WorkStat workStat) {
        this.workStat = workStat;
    }

    public BoardStat getBoardStat() {
        return boardStat;
    }

    public void setBoardStat(BoardStat boardStat) {
        this.boardStat = boardStat;
    }

    public static class FaultStatBean {
        private int repairAllCount;
        private int faultFinishCount;
        private int faultAllCount;
        private int repairFinishCount;

        public int getRepairAllCount() {
            return repairAllCount;
        }

        public void setRepairAllCount(int repairAllCount) {
            this.repairAllCount = repairAllCount;
        }

        public int getFaultFinishCount() {
            return faultFinishCount;
        }

        public void setFaultFinishCount(int faultFinishCount) {
            this.faultFinishCount = faultFinishCount;
        }

        public int getFaultAllCount() {
            return faultAllCount;
        }

        public void setFaultAllCount(int faultAllCount) {
            this.faultAllCount = faultAllCount;
        }

        public int getRepairFinishCount() {
            return repairFinishCount;
        }

        public void setRepairFinishCount(int repairFinishCount) {
            this.repairFinishCount = repairFinishCount;
        }
    }

    public static class TaskStatBean {
        private int monthAllCount;
        private int manualAllCount;
        private double dayEquipmentAvg;
        private double manualMinutesAvg;
        private int weekFinishCount;
        private int dayFinishCount;
        private double manualEquipmentAvg;
        private int monthFinishCount;
        private int dayAllCount;
        private double monthEquipmentAvg;
        private double monthMinutesAvg;
        private double weekMinutesAvg;
        private double weekEquipmentAvg;
        private int weekAllCount;
        private double dayMinutesAvg;
        private int manualFinishCount;

        public int getMonthAllCount() {
            return monthAllCount;
        }

        public void setMonthAllCount(int monthAllCount) {
            this.monthAllCount = monthAllCount;
        }

        public int getManualAllCount() {
            return manualAllCount;
        }

        public void setManualAllCount(int manualAllCount) {
            this.manualAllCount = manualAllCount;
        }

        public double getDayEquipmentAvg() {
            return dayEquipmentAvg;
        }

        public void setDayEquipmentAvg(double dayEquipmentAvg) {
            this.dayEquipmentAvg = dayEquipmentAvg;
        }

        public double getManualMinutesAvg() {
            return manualMinutesAvg;
        }

        public void setManualMinutesAvg(double manualMinutesAvg) {
            this.manualMinutesAvg = manualMinutesAvg;
        }

        public int getWeekFinishCount() {
            return weekFinishCount;
        }

        public void setWeekFinishCount(int weekFinishCount) {
            this.weekFinishCount = weekFinishCount;
        }

        public int getDayFinishCount() {
            return dayFinishCount;
        }

        public void setDayFinishCount(int dayFinishCount) {
            this.dayFinishCount = dayFinishCount;
        }

        public double getManualEquipmentAvg() {
            return manualEquipmentAvg;
        }

        public void setManualEquipmentAvg(double manualEquipmentAvg) {
            this.manualEquipmentAvg = manualEquipmentAvg;
        }

        public int getMonthFinishCount() {
            return monthFinishCount;
        }

        public void setMonthFinishCount(int monthFinishCount) {
            this.monthFinishCount = monthFinishCount;
        }

        public int getDayAllCount() {
            return dayAllCount;
        }

        public void setDayAllCount(int dayAllCount) {
            this.dayAllCount = dayAllCount;
        }

        public double getMonthEquipmentAvg() {
            return monthEquipmentAvg;
        }

        public void setMonthEquipmentAvg(double monthEquipmentAvg) {
            this.monthEquipmentAvg = monthEquipmentAvg;
        }

        public double getMonthMinutesAvg() {
            return monthMinutesAvg;
        }

        public void setMonthMinutesAvg(double monthMinutesAvg) {
            this.monthMinutesAvg = monthMinutesAvg;
        }

        public double getWeekMinutesAvg() {
            return weekMinutesAvg;
        }

        public void setWeekMinutesAvg(double weekMinutesAvg) {
            this.weekMinutesAvg = weekMinutesAvg;
        }

        public double getWeekEquipmentAvg() {
            return weekEquipmentAvg;
        }

        public void setWeekEquipmentAvg(double weekEquipmentAvg) {
            this.weekEquipmentAvg = weekEquipmentAvg;
        }

        public int getWeekAllCount() {
            return weekAllCount;
        }

        public void setWeekAllCount(int weekAllCount) {
            this.weekAllCount = weekAllCount;
        }

        public double getDayMinutesAvg() {
            return dayMinutesAvg;
        }

        public void setDayMinutesAvg(double dayMinutesAvg) {
            this.dayMinutesAvg = dayMinutesAvg;
        }

        public int getManualFinishCount() {
            return manualFinishCount;
        }

        public void setManualFinishCount(int manualFinishCount) {
            this.manualFinishCount = manualFinishCount;
        }

        public static class TheMostStatBean {

            private long averageTimeCost;
            private FlimsyEquipmentTypeBean flimsyEquipmentType;
            private String theMostFaultType;
            private long fastestTimeCost;
            private long slowestTimeCost;
            private Task earliestTask;
            private Task latestTask;
            private List<PersonBean> mostRepairUsers;
            private List<PersonBean> leastRepairUsers;
            private HardestRepair hardestRepair;

            public long getAverageTimeCost() {
                return averageTimeCost;
            }

            public void setAverageTimeCost(long averageTimeCost) {
                this.averageTimeCost = averageTimeCost;
            }

            public FlimsyEquipmentTypeBean getFlimsyEquipmentType() {
                return flimsyEquipmentType;
            }

            public void setFlimsyEquipmentType(FlimsyEquipmentTypeBean flimsyEquipmentType) {
                this.flimsyEquipmentType = flimsyEquipmentType;
            }

            public String getTheMostFaultType() {
                return theMostFaultType;
            }

            public void setTheMostFaultType(String theMostFaultType) {
                this.theMostFaultType = theMostFaultType;
            }

            public long getFastestTimeCost() {
                return fastestTimeCost;
            }

            public void setFastestTimeCost(long fastestTimeCost) {
                this.fastestTimeCost = fastestTimeCost;
            }

            public long getSlowestTimeCost() {
                return slowestTimeCost;
            }

            public void setSlowestTimeCost(long slowestTimeCost) {
                this.slowestTimeCost = slowestTimeCost;
            }

            public Task getEarliestTask() {
                return earliestTask;
            }

            public void setEarliestTask(Task earliestTask) {
                this.earliestTask = earliestTask;
            }

            public Task getLatestTask() {
                return latestTask;
            }

            public void setLatestTask(Task latestTask) {
                this.latestTask = latestTask;
            }

            public List<PersonBean> getMostRepairUsers() {
                return mostRepairUsers;
            }

            public void setMostRepairUsers(List<PersonBean> mostRepairUsers) {
                this.mostRepairUsers = mostRepairUsers;
            }

            public List<PersonBean> getLeastRepairUsers() {
                return leastRepairUsers;
            }

            public void setLeastRepairUsers(List<PersonBean> leastRepairUsers) {
                this.leastRepairUsers = leastRepairUsers;
            }

            public HardestRepair getHardestRepair() {
                return hardestRepair;
            }

            public void setHardestRepair(HardestRepair hardestRepair) {
                this.hardestRepair = hardestRepair;
            }

            public static class FlimsyEquipmentTypeBean {
                private String equipmentTypeName;

                public String getEquipmentTypeName() {
                    return equipmentTypeName;
                }

                public void setEquipmentTypeName(String equipmentTypeName) {
                    this.equipmentTypeName = equipmentTypeName;
                }
            }

            public static class Task {

                private long startTime;
                private long endTime;

                public long getStartTime() {
                    return startTime;
                }

                public void setStartTime(long startTime) {
                    this.startTime = startTime;
                }

                public long getEndTime() {
                    return endTime;
                }

                public void setEndTime(long endTime) {
                    this.endTime = endTime;
                }
            }

            public static class HardestRepair {
                private String repairName;

                public String getRepairName() {
                    return repairName;
                }

                public void setRepairName(String repairName) {
                    this.repairName = repairName;
                }
            }
        }
    }

    public static class IncrementStatBean {

        private int finishCount;
        private String incrementWorkTypeName;
        private int allCount;

        public int getFinishCount() {
            return finishCount;
        }

        public void setFinishCount(int finishCount) {
            this.finishCount = finishCount;
        }

        public String getIncrementWorkTypeName() {
            return incrementWorkTypeName;
        }

        public void setIncrementWorkTypeName(String incrementWorkTypeName) {
            this.incrementWorkTypeName = incrementWorkTypeName;
        }

        public int getAllCount() {
            return allCount;
        }

        public void setAllCount(int allCount) {
            this.allCount = allCount;
        }
    }

    public static class WorkStat {

        private int taskFinishCount;
        private int taskEquipmentCount;
        private int oilCount;
        private int faultCreateCount;
        private int faultFinishCount;
        private int incrementCount;

        public int getTaskFinishCount() {
            return taskFinishCount;
        }

        public void setTaskFinishCount(int taskFinishCount) {
            this.taskFinishCount = taskFinishCount;
        }

        public int getTaskEquipmentCount() {
            return taskEquipmentCount;
        }

        public void setTaskEquipmentCount(int taskEquipmentCount) {
            this.taskEquipmentCount = taskEquipmentCount;
        }

        public int getOilCount() {
            return oilCount;
        }

        public void setOilCount(int oilCount) {
            this.oilCount = oilCount;
        }

        public int getFaultCreateCount() {
            return faultCreateCount;
        }

        public void setFaultCreateCount(int faultCreateCount) {
            this.faultCreateCount = faultCreateCount;
        }

        public int getFaultFinishCount() {
            return faultFinishCount;
        }

        public void setFaultFinishCount(int faultFinishCount) {
            this.faultFinishCount = faultFinishCount;
        }

        public int getIncrementCount() {
            return incrementCount;
        }

        public void setIncrementCount(int incrementCount) {
            this.incrementCount = incrementCount;
        }
    }

    public static class BoardStat {
        private long taskRank;
        private long taskEquipmentRank;
        private long incrementRank;

        public long getTaskRank() {
            return taskRank;
        }

        public void setTaskRank(long taskRank) {
            this.taskRank = taskRank;
        }

        public long getTaskEquipmentRank() {
            return taskEquipmentRank;
        }

        public void setTaskEquipmentRank(long taskEquipmentRank) {
            this.taskEquipmentRank = taskEquipmentRank;
        }

        public long getIncrementRank() {
            return incrementRank;
        }

        public void setIncrementRank(long incrementRank) {
            this.incrementRank = incrementRank;
        }
    }

    public static class PersonBean{
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
