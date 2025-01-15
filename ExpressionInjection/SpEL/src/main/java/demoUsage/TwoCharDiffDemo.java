package demoUsage;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwoCharDiffDemo {
    @Value("#{10 + 20}") // 动态计算表达式
    private int sum;

    @Value("#{T(java.lang.Math).PI}") // 调用静态属性
    private double pi;

    @Value("#{T(java.lang.Math).sqrt(16)}") // 调用静态方法
    private double sqrt;

    @Value("#{10 > 5}") // 布尔表达式
    private boolean isTenGreaterThanFive;

    public void printValues() {
        System.out.println("Sum: " + sum); // 输出：30
        System.out.println("PI: " + pi); // 输出：3.141592653589793
        System.out.println("Square Root of 16: " + sqrt); // 输出：4.0
        System.out.println("Is 10 > 5? " + isTenGreaterThanFive); // 输出：true
    }

    @Value("${app.name}") // 加载配置文件的值
    private String appName;

    @Value("${app.version}") // 加载配置文件的值
    private String appVersion;

    public void printProperties() {
        System.out.println("App Name: " + appName); // 输出：SpEL Demo
        System.out.println("App Version: " + appVersion); // 输出：1.0
    }

}
