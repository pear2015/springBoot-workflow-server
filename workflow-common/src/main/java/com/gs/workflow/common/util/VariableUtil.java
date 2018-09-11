package com.gs.workflow.common.util;

import java.util.Date;

/**
 * Created by zhengyali on 2017/12/5.
 */
public class VariableUtil {
    public   static final String PRIORITY="priority";
    public   static final String AUDITOR="auditor";
    public   static final String ANALYST="analyst";
    public   static final String ISREPLY="isReply";

//常量
    public static  final String BNUSSINEESSPERSON="1";
    public static  final String BNUSSINEESSPGOVER="2";
    public static  final String BNUSSINEESSNOTICE="3";
    public  static  final  String CURRENTTIME= new Date().toString();
    private VariableUtil(){
    }
}
