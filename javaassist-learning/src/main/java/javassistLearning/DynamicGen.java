package javassistLearning;
import javassist.*;

public class DynamicGen {
    public static void main(String[] args) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        // Create class
        CtClass ctClass = classPool.makeClass("javassistLearning.exampleClass");
        // Create field
        CtField ctField = new CtField(CtClass.intType, "id", ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        // Create method
        ctClass.addMethod(CtNewMethod.getter("getID",ctField));
        ctClass.addMethod(CtNewMethod.setter("setID",ctField));

        CtMethod ctMethod = new CtMethod(CtClass.voidType,"printID",
                new CtClass[]{},ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{ System.out.println(\"ID: \" + id); }");
        ctClass.addMethod(ctMethod);

        // load and instance
        Class<?> dynamicClass = ctClass.toClass();
        // need use reflect to construct!
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();

        // use reflect to invoke method
        dynamicClass.getDeclaredMethod("setID",int.class).invoke(instance,2);
        dynamicClass.getDeclaredMethod("printID").invoke(instance);

    }
}
