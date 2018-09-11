package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.enums.RoleType;
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
 * 发送消息给分析员，需要申请重新分析
 */
@Component("GSendReAnalysisMesDelegate")
public class GSendReAnalysisMesDelegate implements JavaDelegate {
    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${analyst.event}")
    private String analystEvent;

    @Value("${message.analysis.reply.government.content}")
    private String replyAnalysisContent;

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 先获取当前分析的分析员
     *
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String analyst = (String) execution.getVariable("operator");
        String auditorId = (String) execution.getVariable("auditor");
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(analyst, RoleType.ANALYST, analystEvent, replyAnalysisContent,  MessageType.GOVERNMENT_AUDITOR_REFUSE,auditorId);
        if (!flag) {
            logger.info("Access Socket Server To Send Message  Called Exception when gov reply auditor->analyst");
        }
    }
}