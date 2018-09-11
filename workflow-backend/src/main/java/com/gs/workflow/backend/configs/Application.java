package com.gs.workflow.backend.configs;


import com.gs.workflow.backend.aspectj.AppWideExceptionHandle;
import com.gs.workflow.common.XseedSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/2.
 */
@SpringBootApplication
@EnableConfigurationProperties({XseedSettings.class})
@ComponentScan({"com.gs.workflow.*"})
public class Application {

    private Logger logger = LoggerFactory.getLogger(AppWideExceptionHandle.class);

    /**
     * mian
     *
     * @param args string[]
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    /**
     * commandLineRunner
     *
     * @param ctx ApplicationContext
     * @return
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            logger.info("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.info(beanName);
            }

        };
    }
}
