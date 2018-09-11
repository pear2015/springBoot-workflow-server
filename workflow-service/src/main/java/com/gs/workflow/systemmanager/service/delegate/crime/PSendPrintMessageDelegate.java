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
 * 发送审核结果（打印消息）给操作员代理类
 */
@Component("PSendPrintMessageDelegate")
public class PSendPrintMessageDelegate implements JavaDelegate {
    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${crime.server.url}")
    private String crimeServerUrl;

    @Value("${operator.event}")
    private String operatorEvent;

    @Value("${message.result.person.content}")
    private String applyResult;

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        /**
         * 发送申请结果给操作员
         * 1.调用犯罪服务端，拿到操作员Id  改为从流程变量中取操作员Id
         * 2.发送消息给操作员
         */
        String operatorId = (String) execution.getVariable("operator");
        String auditorId = (String) execution.getVariable("auditor");
        String applyStatus = (String) execution.getVariable("applyStatus");
        MessageType   messageType=MessageType.PERSON_APPLY_READY_PRINT;
        if (StringUtils.equals("5", applyStatus)) {
            messageType=MessageType.PERSON_APPLY_END;
        }
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(operatorId, RoleType.OPERATOR, operatorEvent, applyResult,  messageType,auditorId);
        if (!flag) {
            logger.info("Access Socket Server To Send Message Called Exception when person print auditor->operator");
        }
        logger.info("Send Apply Result Message From Personal Application Task Over!");
    }
}