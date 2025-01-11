package DEMO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class StudentSerialize {
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("Name");

        String jsonString = JSON.toJSONString(student, SerializerFeature.WriteClassName);
        System.out.println(jsonString);
    }
}
