package com.gs.workflow.systemmanager.service.serviceimpl.crime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gs.workflow.systemmanager.contract.model.crms.ApplyAnalystAuditor;
import com.gs.workflow.systemmanager.contract.model.crms.WaitApportion;

import com.gsafety.springboot.common.utils.HttpClientUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2017/9/29.
 */
@Service
public class CrimeServiceImpl {


    @Value("${crime.server.url}")
    private String crimeServerUrl;

    @Autowired
    private HttpClientUtil httpClientUtil;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 调用犯罪服务筛选无申请的分析员
     *
     * @param users
     */
    public List<String> getAnalystFreeList(List<String> users) {
        List<String> userList = new ArrayList<>();
        try {
            userList = httpClientUtil.httpPostReturnTypeRef(crimeServerUrl + "/analysis/post", users, new TypeReference<List<String>>() {
            }, false);
            return userList;
        } catch (Exception e) {
            logger.info("getAnalystFreeList has Exception", e);
            return userList;
        }

    }

    /**
     * 调用犯罪服务保存等待分派列表
     */
    public boolean saveWaitApportion(WaitApportion waitApportion) {

        try {
            Boolean result = httpClientUtil.httpPostReturnTypeRef(crimeServerUrl + "/waitApportion/add", waitApportion, new TypeReference<Boolean>() {
            }, false);
            return result == null ? false : result;
        } catch (Exception e) {
            logger.info("saveWaitApportion has Exception", e);
            return false;
        }

    }

    /**
     * 保存任务分派
     */
    public boolean saveApplyAnalystAuditor(ApplyAnalystAuditor applyAnalystAuditor) {
        try {
            Boolean result = httpClientUtil.httpPostReturnTypeRef(crimeServerUrl + "/applyAndAnalystRelation/add", applyAnalystAuditor, new TypeReference<Boolean>() {
            }, false);
            return result == null ? false : result;
        } catch (Exception e) {
            logger.info("saveApplyAnalystAuditor has Exception", e);
            return false;
        }
    }
}
