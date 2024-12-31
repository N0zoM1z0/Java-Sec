package SetterAndGetter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NoSetterButGetter {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"name\":\"John\",\"age\":30}";
        Person1 person = mapper.readValue(json, Person1.class);
        System.out.println(person);
    }
}

class Person1{
    private String name;
    private int age;

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person1{name='" + name + "', age=" + age + "}";
    }
}