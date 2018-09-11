package com.gs.workflow.systemmanager.service.delegate.notice;

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
 * 审核员审核通过.
 * 给归档员发消息
 */
@Component("NoticeAuditorEndDelegate")
public class NoticeAuditorEndDelegate implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${filer.event}")
    private String filerEvent;

    @Value("${message.result.notice.content}")
    private String noticeEndContent;
    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    /**
     * 审核通过后 通知归档员
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String filerId = (String) execution.getVariable("operator");
        String auditorId = (String) execution.getVariable("auditor");
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(filerId, RoleType.FILER, filerEvent, noticeEndContent,  MessageType.NOTICE_AUDITOR_END,auditorId);
        if (!flag) {
            logger.info("Access Socket Server To Send Message Called Exception");
        }

    }
}
