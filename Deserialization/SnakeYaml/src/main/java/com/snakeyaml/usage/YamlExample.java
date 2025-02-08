package com.snakeyaml.usage;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class YamlExample {

    public static void main(String[] args) throws Exception{
        Yaml yaml = new Yaml();

        try (FileInputStream inputStream = new FileInputStream("example.yaml")) {
            // 读取并解析 YAML 文件
            Map<String, Object> data = yaml.load(inputStream);

            // 打印解析结果
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
