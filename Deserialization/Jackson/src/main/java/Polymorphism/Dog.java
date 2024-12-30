package Polymorphism;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("I am a Dog");
    }

    public Object object;
    public String nonsense = "Dooooog";

    @Override
    public String toString() {
        return "Dog" + "Object: " + object;
    }
}
