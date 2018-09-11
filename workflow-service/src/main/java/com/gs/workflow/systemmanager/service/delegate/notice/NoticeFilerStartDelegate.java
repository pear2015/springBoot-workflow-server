package com.gs.workflow.systemmanager.service.delegate.notice;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhengyali on 2017/10/31.
 */
@Component("NoticeFilerStartDelegate")
public class NoticeFilerStartDelegate implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${crime.server.url}")
    private String crimeServerUrl;
    @Autowired
    private TaskService taskService;

    /**
     * 公告申请被驳回 指定归档员
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // 启动流程传入的operator变量，指的操作员也指归档员
        String filerId = (String) execution.getVariable("operator");
        execution.setVariable("filer", filerId);
        Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (singleTask != null) {
            this.taskService.setAssignee(singleTask.getId(), filerId);
        }
        logger.info("Assignee to analyst task listener over!");
    }
}
