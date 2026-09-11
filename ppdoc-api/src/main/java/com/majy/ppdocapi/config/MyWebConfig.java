package com.majy.ppdocapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//文件绝对路径与虚拟地址的映射
@Configuration
public class MyWebConfig implements WebMvcConfigurer
{
    @Value("${file.pdf.path}")
    private String pdfFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + pdfFolder);
    }
}
