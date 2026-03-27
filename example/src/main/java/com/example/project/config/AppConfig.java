package com.example.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 业务配置类
 *
 * @author CodeGenerator
 */
@Configuration
@PropertySource(value = {
 "classpath:application.yml", "classpath:ValidationMessages.properties" }, ignoreResourceNotFound = true)
public class AppConfig {

    // 自定义业务配置可在此添加

}
