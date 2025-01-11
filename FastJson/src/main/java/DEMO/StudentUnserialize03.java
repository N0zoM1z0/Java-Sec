package DEMO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class StudentUnserialize03 {
    public static void main(String[] args) {
        String jsonString ="{\"@type\":\"DEMO.Student\",\"age\":6," +
                "\"name\":\"Name\",\"address\":\"china\",\"properties\":{}}";
        Object obj = JSON.parse(jsonString, Feature.SupportNonPublicField);
        // 或以下语句，输出结果一致
        //JSONObject obj = JSON.parseObject(jsonString);
        System.out.println(obj);
        System.out.println(obj.getClass().getName());
    }
}