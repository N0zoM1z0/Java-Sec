package SerializeTest;

import org.yaml.snakeyaml.Yaml;

public class YamlSerialize {

    public static void main(String[] args) throws Exception {
//        serialize();
        deserialize();
    }
    public static void serialize(){
        Person person = new Person();
        person.setName("N0");
        person.setAge(20);
        person.setSchool("BS");
        person.setProvince("BJ");
        Yaml yaml = new Yaml();
        String str = yaml.dump(person);
        System.out.println(str);
    }
    public static void deserialize(){
        String str1 = "!!SerializeTest.Person {age: 20, name: N0}";
        String str2 = "age: 20\n" +
                "name: N0";
        Yaml yaml = new Yaml();
        Person person1 = yaml.load(str1);
        Person person2 = yaml.loadAs(str2, Person.class);
    }
}
