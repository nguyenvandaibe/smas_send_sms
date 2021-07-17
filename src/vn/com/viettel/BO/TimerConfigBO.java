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
    private String TenantId;

    
    public String getTimerConfigId() {
        return TimerConfigId;
    }

    public void setTimerConfigId(String TimerConfigId) {
        this.TimerConfigId = TimerConfigId;
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }
}
