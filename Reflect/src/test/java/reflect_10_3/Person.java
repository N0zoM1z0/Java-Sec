package reflect_10_3;

public class Person {
    public String name;
    protected int age;
    private int stuNo;
    public Person(String name, int age, int stuNo) {
        this.name = name;
        this.age = age;
        this.stuNo = stuNo;
    }
    public String getName(String name) {
        return name;
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", stuNo=" + stuNo +
                '}';
    }
}
