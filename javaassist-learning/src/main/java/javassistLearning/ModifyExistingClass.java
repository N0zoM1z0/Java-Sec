package javassistLearning;
import javassist.*;

public class ModifyExistingClass {
    public static void main(String[] args) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("javassistLearning.ExistingClass");

        CtMethod method = ctClass.getDeclaredMethod("SayHello");
        method.insertBefore("System.out.println(\"Before SayHello()\");");
        method.insertAfter("System.out.println(\"After SayHello()\");");

        Class<?> modifiedClass = ctClass.toClass();
        Object instance = modifiedClass.getDeclaredConstructor().newInstance();
        modifiedClass.getDeclaredMethod("SayHello").invoke(instance);
    }
}
