package com.minxing365.vote.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.IOException;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
    @Value( "${img.location}" )
    private String location;

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory multipartConfigFactory=new MultipartConfigFactory();
           //文件最大 大小
           multipartConfigFactory.setMaxFileSize( "2MB" );
           //上传数据的大小
           multipartConfigFactory.setMaxRequestSize( "10MB" );
           return multipartConfigFactory.createMultipartConfig();
    }
    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations(location);
        super.addResourceHandlers(registry);
    }


}
