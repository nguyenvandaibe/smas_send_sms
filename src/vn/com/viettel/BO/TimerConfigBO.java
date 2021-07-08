/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.BO;

/**
 *
 * @author chiendd1
 */
public class TimerConfigBO {
    private String TimerConfigId;
    private int SchoolId;
    private int partitionId;

    
    public String getTimerConfigId() {
        return TimerConfigId;
    }

    public void setTimerConfigId(String TimerConfigId) {
        this.TimerConfigId = TimerConfigId;
    }

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int SchoolId) {
        this.SchoolId = SchoolId;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }
}
