package com.gs.workflow.systemmanager.service.delegate.crime;
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
 * Created by zhangqiang on 2017/9/23.
 * 政府申请：分派分析任务给其他分析员
 */
@Component("GApportionToAnalystStartDelegate")
public class GApportionToAnalystStartDelegate implements JavaDelegate {
    @Value("${crime.server.url}")
    private String crimeServerUrl;
    @Autowired
    private TaskService taskService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 前提条件，此任务第一次设置分析员
     * 这里的分派逻辑和个人申请不同
     * 1.获取此流程实例的业务key，也就是申请ID
     * 2.调用犯罪服务端接口，查询此条申请是谁分析的，将重新分析的分析员指定为此人
     * 3.设置分析员
     * 4.调用犯罪服务接口，将此条记录加入到任务关联表r_apply_analyst_auditor中
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // 启动流程传入的operator变量，指的操作员也指分析员
        String analystId = (String) execution.getVariable("operator");
        execution.setVariable("analyst", analystId);
        Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (singleTask != null) {
            this.taskService.setAssignee(singleTask.getId(), analystId);
        }
        logger.info("Assignee to analyst task listener over!");
    }
}