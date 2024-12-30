package Polymorphism;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("I'm a cat");
    }
}
