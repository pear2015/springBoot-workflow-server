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
 * 个人申请： 先检查分析员是否在线
 * 1、在线 流程往下走
 * 2、不在线 流程挂起
 */
@Component("PApportionToAnalystDelegate")
public class PApportionToAnalystDelegate implements JavaDelegate {
    @Value("${crime.server.url}")
    private String crimeServerUrl;

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //传入角色类型 和业务类型
        if (workFlowServiceImpl.apportionResolve(execution, "analyst","1")) {
            logger.info("Check analyst is online over!");
        }
        execution.setVariable("auditor", "");
        logger.info("Assignee to analyst task listener over!");
    }
}