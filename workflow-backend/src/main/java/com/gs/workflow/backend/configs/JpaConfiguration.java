package com.gs.workflow.backend.configs;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by zhengyali on 2017/10/10.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.gs.workflow"},repositoryFactoryBeanClass = com.gsafety.springboot.common.pagerepository.QueryMetaDataRepositoryFactoryBean.class)
@EnableAutoConfiguration
@EntityScan({"com.gs.workflow"})
public class JpaConfiguration {
}
