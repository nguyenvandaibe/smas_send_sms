/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.BO;

/**
 *
 * @author Administrator
 */
public class PreNumberTelco {
    private int PREFIX_NUMBER_ID;
    private String PREFIX_NUMBER;
    private String TELCO;
    private String M_SOURCEDB;
    private Integer PREFIX_TYPE;
    private boolean IS_ACTIVE;

   

   
    
    private String PREFIX_TRANS;

    public int getPREFIX_NUMBER_ID() {
        return PREFIX_NUMBER_ID;
    }

    public void setPREFIX_NUMBER_ID(int PREFIX_NUMBER_ID) {
        this.PREFIX_NUMBER_ID = PREFIX_NUMBER_ID;
    }

    public String getPREFIX_NUMBER() {
        return PREFIX_NUMBER;
    }

    public void setPREFIX_NUMBER(String PREFIX_NUMBER) {
        this.PREFIX_NUMBER = PREFIX_NUMBER;
    }

    public String getTELCO() {
        return TELCO;
    }

    public void setTELCO(String TELCO) {
        this.TELCO = TELCO;
    }

    public String getM_SOURCEDB() {
        return M_SOURCEDB;
    }

    public void setM_SOURCEDB(String M_SOURCEDB) {
        this.M_SOURCEDB = M_SOURCEDB;
    }

    public Integer getPREFIX_TYPE() {
        return PREFIX_TYPE;
    }

    public void setPREFIX_TYPE(Integer PREFIX_TYPE) {
        this.PREFIX_TYPE = PREFIX_TYPE;
    }

    public String getPREFIX_TRANS() {
        return PREFIX_TRANS;
    }

    public void setPREFIX_TRANS(String PREFIX_TRANS) {
        this.PREFIX_TRANS = PREFIX_TRANS;
    }
    
     public boolean getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(boolean IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }
    
}
