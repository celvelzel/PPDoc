package com.majy.ppdocapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class PpdocApiApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PpdocApiApplication.class, args);
    }


//    @Value("${ppocr.command}")
//    private String ppocrCommand;
//
//    @PostConstruct
//    public void startPpocrService() {
//        // 启动 PPOCR 服务的命令行指令
//        String[] command = ppocrCommand.split(" ");
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        try {
//            Process process = processBuilder.start();
//            // 这里可以添加一些代码来处理 PPOCR 服务的输出和错误
//            // 例如，读取输入流和错误流
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
