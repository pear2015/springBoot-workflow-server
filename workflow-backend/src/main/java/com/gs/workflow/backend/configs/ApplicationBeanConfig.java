package com.gs.workflow.backend.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gsafety.springboot.common.entityrepository.QueryEntityFactory;
import com.gsafety.springboot.common.entityrepository.QueryEntityFactoryImpl;
import com.gsafety.springboot.common.utils.HttpClientUtil;
import com.gsafety.springboot.common.utils.HttpClientUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiaodiming on 2017/3/7.
 */
@Configuration
public class ApplicationBeanConfig {
    /**
     * appInitBean
     *
     * @return
     */
    @Bean
    public AppInit appInitBean() {
        return new AppInit();
    }

    /**
     * 配置httpclient帮助类bean到容器
     *
     * @return the http client util
     */
    @Bean
    public HttpClientUtil configHttpClientUtil() {
        return new HttpClientUtilImpl();
    }


    /**
     * 配置实体查询帮助类bean到容器
     *
     * @return
     */
    @Bean
    public QueryEntityFactory configQueryEntityFactory() {
        return new QueryEntityFactoryImpl();
    }

    /**
     * 配置jackson转换工具
     * @return
     */
    @Bean
   public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }
}
