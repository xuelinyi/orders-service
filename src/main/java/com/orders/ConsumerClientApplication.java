package com.orders;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.orders.*")
@ServletComponentScan
@EnableDiscoveryClient
@RefreshScope   //开启配置更新功能
public class ConsumerClientApplication {
    public static void main(String[] args) {

        SpringApplication.run(ConsumerClientApplication.class, args);
    }

    /**
     * MP分页插件,后面会有说明
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
