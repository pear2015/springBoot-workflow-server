package com.gs.workflow.systemmanager.contract.model.crms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2017/10/10.
 */
public class AuditorApportion {
    private  List<WaitApportion> waitApportionList=new ArrayList<>();
    private List<String> userIds =new ArrayList<>();
    public List<WaitApportion> getWaitApportionList() {
        return waitApportionList;
    }

    public void setWaitApportionList(List<WaitApportion> waitApportionList) {
        this.waitApportionList = waitApportionList;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

}
