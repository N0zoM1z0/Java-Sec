package Polymorphism;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultTypingExample {
    public static void main(String[] args) throws Exception {
        Person person = new Person();
        person.name = "John";
        person.pet = new Dog();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        String json = objectMapper.writeValueAsString(person);
        System.out.println("Serialized JSON: " + json);

        Person deserializedPerson = objectMapper.readValue(json, Person.class);
        System.out.println("Deserialized Person: " + deserializedPerson);

        deserializedPerson.pet.speak();
    }
}
