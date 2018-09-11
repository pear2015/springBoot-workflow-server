package com.gs.workflow.systemmanager.service.util;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.TaskQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangqiang on 2017/9/28.
 */
@Component
public class BubberSortLessTaskAssignee {

    @Autowired
    private TaskService taskService;

    private static BubberSortLessTaskAssignee bubberSortLessTaskAssign;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 初始化构造
     */
    public BubberSortLessTaskAssignee() {
        bubberSortLessTaskAssign = this;
    }

    /**
     * 设置taskService
     */
    @PostConstruct
    public void init() {
        bubberSortLessTaskAssign.taskService = this.taskService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 使用冒泡排序，查找任务量最少的用户
     *
     * @param assigneeIds
     * @return
     */
    public String getLessTaskAssigneeId(List<String> assigneeIds, String processDefinedId) {
        String lessTaskAssigneeId = "";

        if (!assigneeIds.isEmpty()) {
            //将List转换为Id数组
            String[] array = new String[assigneeIds.size()];
            String[] ids = assigneeIds.toArray(array);
            lessTaskAssigneeId = getLessTaskAssignee(ids, processDefinedId);
        }
        return lessTaskAssigneeId;
    }

    /**
     * 获取任务最少量的人
     */
    private String getLessTaskAssignee(String[] ids, String processDefinedId) {
        try {
            for (int i = 0; i < ids.length; i++) {
                for (int j = 0; j < ids.length - 1 - i; j++) {
                    //前一个人的任务量
                    TaskQuery taskQueryFirst = bubberSortLessTaskAssign.taskService.createTaskQuery().taskAssignee(ids[j]).processDefinitionId(processDefinedId);
                    int  taskFirst = taskQueryFirst != null?taskQueryFirst.list().size():0;
                    //后一个人的任务量
                    TaskQuery taskQueryLast = bubberSortLessTaskAssign.taskService.createTaskQuery().taskAssignee(ids[j + 1]).processDefinitionId(processDefinedId);
                    int taskLast =taskQueryLast != null?taskQueryLast.list().size():0;
                    this.exchangeData(taskFirst, taskLast, j, ids);
                }
            }
        } catch (Exception e) {
            logger.info(new Date().toString() + "Exception:getLessTaskAssignee has Exception Cell ", e);
        }
        return ids[0];
    }

    /**
     *  值交换
     * @param taskFirst
     * @param taskLast
     * @param j
     * @param ids
     * @return
     */
    private  void  exchangeData( int  taskFirst,int taskLast,int j,String[] ids){
        if (taskFirst > taskLast) {
            String temp = ids[j];
            ids[j] = ids[j + 1];
            ids[j + 1] = temp;
        }
    }
}
