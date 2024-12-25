package UnsafeDemo;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

class MyClass{
    private MyClass(){
        System.out.println("Constructor");
    }
}
public class demo2 {
    // learn more native operation of `Unsafe`
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Unsafe.class;
        Field field = clazz.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        // 1. directly manage memory
        long memoryAddress = unsafe.allocateMemory(1024);
        System.out.printf("%x\n", memoryAddress);
        unsafe.putByte(memoryAddress, (byte)123);
        byte value = unsafe.getByte(memoryAddress);
        System.out.println(value);
        unsafe.freeMemory(memoryAddress);

        // 2. create objective directly
        MyClass obj = (MyClass) unsafe.allocateInstance(MyClass.class);
        System.out.println("Object created: " + obj);
    }
}
