package demoUsage;

import org.springframework.context.*;
import org.springframework.context.annotation.*;

public class Launch {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TwoCharDiffDemo obj = context.getBean(TwoCharDiffDemo.class);
        obj.printValues();
        System.out.println("============================");
        obj.printProperties();
    }
}
