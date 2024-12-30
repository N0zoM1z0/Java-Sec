package Polymorphism;

public class Person {
    public String name;
    public Animal pet;

    @Override
    public String toString() {
        return "Person{name='" + name + "', pet=" + pet + "}";
    }
}
