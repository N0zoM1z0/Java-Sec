package demoUsage;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.*;
import java.util.*;

public class test1 {
    public static void main(String[] args) throws Exception {
        // Step 1: 创建配置对象
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);

        // Step 2: 设置模板文件的加载路径
        cfg.setDirectoryForTemplateLoading(new File("templates"));

        // Step 3: 获取模板
        Template template = cfg.getTemplate("hello.ftl");

        // Step 4: 创建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "${1+2}");
        dataModel.put("age", 30);

        // Step 5: 创建输出流
        Writer out = new OutputStreamWriter(System.out);

        // Step 6: 合并模板和数据模型
        try {
            template.process(dataModel, out); // 输出到控制台
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
