package DEMO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class StudentUnserialize01 {
    public static void main(String[] args) {
        String jsonString = "{\"@type\":\"DEMO.Student\",\"age\":6,\"name\":\"Name\"}";
        Student student = (Student) JSON.parseObject(jsonString, Object.class);
        System.out.println(student);
        System.out.println(student.getClass().getName());
    }
}