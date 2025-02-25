package HighVersionBypass.LocalEvilClassAsReferenceFactory;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Client {
    public static void main(String[] args) throws Exception {
        String uri = "rmi://localhost:6666/Object";
        Context ctx = new InitialContext();
        ctx.lookup(uri);
/*
createValueExpression:62, ExpressionFactoryImpl (org.apache.el)
getValue:59, ELProcessor (javax.el)
eval:54, ELProcessor (javax.el)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:498, Method (java.lang.reflect)
getObjectInstance:211, BeanFactory (org.apache.naming.factory)
getObjectInstance:332, NamingManager (javax.naming.spi)
decodeObject:499, RegistryContext (com.sun.jndi.rmi.registry)
lookup:138, RegistryContext (com.sun.jndi.rmi.registry)
lookup:218, GenericURLContext (com.sun.jndi.toolkit.url)
lookup:417, InitialContext (javax.naming)
main:10, Client (HighVersionBypass.LocalEvilClassAsReferenceFactory)
 */
    }
}