package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.enums.RoleType;
import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 2017/9/22.
 * 政府申请：发送审核结果（打印消息）给操作员代理类
 */
@Component("GSendPrintMessageDelegate")
public class GSendPrintMessageDelegate implements JavaDelegate {

    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${analyst.event}")
    private String analystEvent;

    @Value("${message.result.government.content}")
    private String applyResult;

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String analyst = (String) execution.getVariable("operator");
        String auditorId = (String) execution.getVariable("auditor");
        String applyStatus = (String) execution.getVariable("applyStatus");
        MessageType messageType=MessageType.GOVERMENT_APPLY_READY_PRINT;
        if (StringUtils.equals("5", applyStatus)) {
            messageType=MessageType.GOVERMENT_AUDITOR_END;
        }
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(analyst, RoleType.ANALYST, analystEvent, applyResult,  messageType,auditorId);

        if (!flag) {
            logger.info("Access Socket Server To Send Message  Called Exception when auditor->analyst");
        }

        logger.info("Send Print Message From Government Application Task Over!");
    }
}