package UnsafeDemo;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class demo1 {
    // in this, we learned that we should use reflect to get access to Unsafe
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Unsafe.class;
        Field field = clazz.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        System.out.println(unsafe.hashCode());
    }

}
