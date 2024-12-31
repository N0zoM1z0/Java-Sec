package SetterAndGetter;

public class Person {
    public String name;
    public Sex sex;
    public int age;
    public Person(){}

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("Person.age=%d, Person.name=%s, %s", age, name, sex == null ? "null" : sex);
    }
}
