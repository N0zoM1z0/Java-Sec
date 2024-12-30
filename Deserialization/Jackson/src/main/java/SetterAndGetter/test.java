package SetterAndGetter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class test {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();

        String json = "{\"age\":1,\"name\":\"aaa\",\"sex\":{\"sex\":1}}";
        Person person = mapper.readValue(json, Person.class);
        System.out.println(person);
//        String json = "{\"sex\":1}";
//        Sex sex = mapper.readValue(json,Sex.class);
//        System.out.println(sex);
    }

}
