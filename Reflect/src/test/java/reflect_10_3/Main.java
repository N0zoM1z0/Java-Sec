package reflect_10_3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        Person person = new Person("N0zoM1z0",1,2);
        /*
        reflect method
        */
        Class<?> c = Class.forName("reflect_10_3.Person");
        Method m = c.getMethod("getName", String.class);
        System.out.println(m.invoke(person, "N0z0"));
        /*
        reflect field
        */
        Field field = c.getField("name");
        field.setAccessible(true);
        field.set(person, "M1z0");
        field = c.getDeclaredField("age");
        field.setAccessible(true);
        field.set(person, 20);
        field = c.getDeclaredField("stuNo");
        field.setAccessible(true);
        field.set(person, 111);
        System.out.println(person);
        /*
        reflect constructor
         */
        Constructor constructor = c.getDeclaredConstructor(String.class, int.class, int.class);
        constructor.setAccessible(true);
        Person person3 = (Person)constructor.newInstance("N0zoM1z0",3,3);
        System.out.println(person3);
    }
}
