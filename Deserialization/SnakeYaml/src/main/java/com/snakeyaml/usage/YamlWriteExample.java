package com.snakeyaml.usage;

import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlWriteExample {

    public static void main(String[] args) {
        Yaml yaml = new Yaml();

        // 创建一个示例数据对象
        Map<String, Object> person = new HashMap<>();
        person.put("name", "John Doe");
        person.put("age", 30);

        Map<String, String> address = new HashMap<>();
        address.put("street", "123 Main St");
        address.put("city", "Anytown");
        address.put("postalCode", "12345");

        person.put("address", address);

        // 将数据写入 YAML 文件
        try (FileWriter writer = new FileWriter("output.yaml")) {
            yaml.dump(person, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
