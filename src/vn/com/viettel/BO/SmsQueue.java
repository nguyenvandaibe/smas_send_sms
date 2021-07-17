package vn.com.viettel.BO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SmsQueue {

    private String Id;
    private String TenantId;
    private String Mobile;
    private int SmsTypeId;
    private String ReceiverId;
    private String SchoolYearId;
    private String SchoolYearCode;
    private String SmsContent;
    private String Username;
    private String SenderName;
    private String ReceiverName;
    private int SmsCnt;
    private int ReceiveType;
    private String SmsSubscriberId;
    private String SenderUnitId;
    private String ReceiverUnitName;
    private boolean IsAuto;
    private Long SenderGroup;
    private String ServiceId;
    private int ContentType;
    private boolean IsOnSchedule;
    private String SmsTimerConfigId;
    private boolean IsAdmin;
    private String SenderId;
    private String ContactGroupId;
    private boolean IsCustomType;
    private int TelcoId;
    private Date SyncTime;
    private int RetryNum;
    private int LastDigitNumber;
    private int YearId;
    private String RequestId;
    private String HistoryRawId;
    private String CpCode;

    public SmsQueue() {
    }

    public SmsQueue(ResultSet rs) throws SQLException {
        TenantId = rs.getString("TenantId");
        Mobile = rs.getString("Mobile");
        SmsTypeId = rs.getInt("SmsTypeId");
        ReceiverId = rs.getString("ReceiverId");
        SchoolYearId = rs.getString("SchoolYearId");
        SchoolYearCode = rs.getString("SchoolYearCode");
        SmsContent = rs.getString("SmsContent");
        Username = rs.getString("Username");
        SenderName = rs.getString("SenderName");
        ReceiverName = rs.getString("ReceiverName");
        SmsCnt = rs.getInt("SmsCnt");
        ReceiveType = rs.getInt("ReceiveType");
        SmsSubscriberId = rs.getString("SmsSubscriberId");
        SenderUnitId = rs.getString("SenderUnitId");
        ReceiverUnitName = rs.getString("ReceiverUnitName");
        IsAuto = rs.getBoolean("IsAuto");
        SenderGroup = rs.getLong("SenderGroup");
        ServiceId = rs.getString("ServiceId");
        ContentType = rs.getInt("ContentType");
        IsOnSchedule = rs.getBoolean("IsOnSchedule");
        SmsTimerConfigId = rs.getString("SmsTimerConfigId");
        IsAdmin = rs.getBoolean("IsAdmin");
        SenderId = rs.getString("SenderId");
        ContactGroupId = rs.getString("ContactGroupId");
        IsCustomType = rs.getBoolean("IsCustomType");
        TelcoId = rs.getInt("TelcoId");
        SyncTime = rs.getTime("SyncTime");
        RetryNum = rs.getInt("RetryNum");
        LastDigitNumber = rs.getInt("LastDigitNumber");
        YearId = rs.getInt("YearId");
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getSmsTypeId() {
        return SmsTypeId;
    }

    public void setSmsTypeId(int smsTypeId) {
        SmsTypeId = smsTypeId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getSchoolYearId() {
        return SchoolYearId;
    }

    public void setSchoolYearId(String schoolYearId) {
        SchoolYearId = schoolYearId;
    }

    public String getSchoolYearCode() {
        return SchoolYearCode;
    }

    public void setSchoolYearCode(String schoolYearCode) {
        SchoolYearCode = schoolYearCode;
    }

    public String getSmsContent() {
        return SmsContent;
    }

    public void setSmsContent(String smsContent) {
        SmsContent = smsContent;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public int getSmsCnt() {
        return SmsCnt;
    }

    public void setSmsCnt(int smsCnt) {
        SmsCnt = smsCnt;
    }

    public int getReceiveType() {
        return ReceiveType;
    }

    public void setReceiveType(int receiveType) {
        ReceiveType = receiveType;
    }

    public String getSmsSubscriberId() {
        return SmsSubscriberId;
    }

    public void setSmsSubscriberId(String smsSubscriberId) {
        SmsSubscriberId = smsSubscriberId;
    }

    public String getSenderUnitId() {
        return SenderUnitId;
    }

    public void setSenderUnitId(String senderUnitId) {
        SenderUnitId = senderUnitId;
    }

    public String getReceiverUnitName() {
        return ReceiverUnitName;
    }

    public void setReceiverUnitName(String receiverUnitName) {
        ReceiverUnitName = receiverUnitName;
    }

    public boolean isAuto() {
        return IsAuto;
    }

    public void setAuto(boolean auto) {
        IsAuto = auto;
    }

    public Long getSenderGroup() {
        return SenderGroup;
    }

    public void setSenderGroup(Long senderGroup) {
        SenderGroup = senderGroup;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public boolean isOnSchedule() {
        return IsOnSchedule;
    }

    public void setOnSchedule(boolean onSchedule) {
        IsOnSchedule = onSchedule;
    }

    public String getSmsTimerConfigId() {
        return SmsTimerConfigId;
    }

    public void setSmsTimerConfigId(String smsTimerConfigId) {
        SmsTimerConfigId = smsTimerConfigId;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getContactGroupId() {
        return ContactGroupId;
    }

    public void setContactGroupId(String contactGroupId) {
        ContactGroupId = contactGroupId;
    }

    public boolean isCustomType() {
        return IsCustomType;
    }

    public void setCustomType(boolean customType) {
        IsCustomType = customType;
    }

    public int getTelcoId() {
        return TelcoId;
    }

    public void setTelcoId(int telcoId) {
        TelcoId = telcoId;
    }

    public Date getSyncTime() {
        return SyncTime;
    }

    public void setSyncTime(Date syncTime) {
        SyncTime = syncTime;
    }

    public int getRetryNum() {
        return RetryNum;
    }

    public void setRetryNum(int retryNum) {
        RetryNum = retryNum;
    }

    public int getLastDigitNumber() {
        return LastDigitNumber;
    }

    public void setLastDigitNumber(int lastDigitNumber) {
        LastDigitNumber = lastDigitNumber;
    }

    public int getYearId() {
        return YearId;
    }

    public void setYearId(int yearId) {
        YearId = yearId;
    }


    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getHistoryRawId() {
        return HistoryRawId;
    }

    public void setHistoryRawId(String historyRawId) {
        HistoryRawId = historyRawId;
    }

    public String getPara() {
        String para = "TenantId=" + TenantId;
        return para;
    }

    public String getCpCode() {
        return CpCode;
    }

    public void setCpCode(String cpCode) {
        CpCode = cpCode;
    }
}
