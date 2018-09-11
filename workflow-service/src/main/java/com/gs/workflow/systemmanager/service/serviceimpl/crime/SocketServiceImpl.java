package com.gs.workflow.systemmanager.service.serviceimpl.crime;

import com.fasterxml.jackson.core.type.TypeReference;

import com.gs.workflow.common.enums.RoleType;
import com.gs.workflow.systemmanager.contract.model.common.NoticeMessage;
import com.gs.workflow.systemmanager.contract.model.common.User;

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
public class SocketServiceImpl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${socket.server.url}")
    private String socketServerUrl;
    @Autowired
    private HttpClientUtil httpClientUtil;

    /**
     * 获取空闲用户列表
     *
     * @return 用户列表集合
     */
    public List<User> getUserListByRoleType(RoleType roleType) {

        List<User> userList = new ArrayList<>();
        if (roleType == null) {
            return userList;
        }
        try {
            userList = httpClientUtil.httpGetWithType(socketServerUrl + "/socket/getAll?roleType=" + roleType, new TypeReference<List<User>>() {
            });
            return userList;
        } catch (Exception e) {
            logger.info("get socket list error",e);
            return userList;
        }

    }

    /**
     * 发送消息
     */
    public boolean sendMessage(NoticeMessage messageInfo) {
        try {
            Boolean result = httpClientUtil.httpPostReturnTypeRef(socketServerUrl + "/message/send", messageInfo, new TypeReference<Boolean>() {
            }, false);
            if (result == null) {
                return false;
            } else {
                return result;
            }
        } catch (Exception e) {
            logger.info("send  socket message error",e);
            return false;
        }
    }
}
