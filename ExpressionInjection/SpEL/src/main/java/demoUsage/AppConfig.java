package demoUsage;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "demoUsage") // 指定扫描的包路径
public class AppConfig {
    // 如果需要，可以在这里定义更多的配置或 Bean
}
