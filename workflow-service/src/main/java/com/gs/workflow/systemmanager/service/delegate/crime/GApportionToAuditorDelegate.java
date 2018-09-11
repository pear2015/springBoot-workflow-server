package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 2017/9/22.
 * 将政府申请任务分配给审核员
 */
@Component("GApportionToAuditorDelegate")
public class GApportionToAuditorDelegate implements JavaDelegate {

    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${crime.server.url}")
    private String crimeServerUrl;
    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        /**
         * 前提条件，没有设置过审核员
         * 将审核任务分派给在线审核员的步骤
         * 1.从消息通知获取所有在线的审核员
         * 2.将任务分给任务量最少的审核员
         * 3.将这条数据加入到任务和用户关联表中
         */
        if (workFlowServiceImpl.apportionResolve(execution, "auditor","2")) {
            logger.info("Check Auditor is online over!");
        }
    }

}