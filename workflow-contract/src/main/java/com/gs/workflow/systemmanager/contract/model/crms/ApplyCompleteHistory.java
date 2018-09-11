package com.gs.workflow.systemmanager.contract.model.crms;
import java.util.Date;

/**
 * Created by zhengyali on 2018/1/22.
 */
public class ApplyCompleteHistory {
    /**
     * id
     */
    private String id;
    /**
     * 申请Id
     */
    private String applyInfoId;

    /**
     * 工作流流程ID
     */
    private String processId;
    /**
     * 完成人
     */
    private  String completeUserId;

    /**
     * 創建時間
     */

    private Date createTime;

    /**
     * 是否首次完成 true 重新
     */
    private boolean  isReplyComplete;
    /**
     * 是否是当前流程
     */
    private  String isCurrentProcess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyInfoId() {
        return applyInfoId;
    }

    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCompleteUserId() {
        return completeUserId;
    }

    public void setCompleteUserId(String completeUserId) {
        this.completeUserId = completeUserId;
    }

    public boolean isReplyComplete() {
        return isReplyComplete;
    }

    public void setReplyComplete(boolean replyComplete) {
        isReplyComplete = replyComplete;
    }

    public String getIsCurrentProcess() {
        return isCurrentProcess;
    }

    public void setIsCurrentProcess(String isCurrentProcess) {
        this.isCurrentProcess = isCurrentProcess;
    }

}
