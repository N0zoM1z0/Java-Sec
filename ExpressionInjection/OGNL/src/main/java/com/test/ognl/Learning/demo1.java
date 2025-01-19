package com.test.ognl.Learning;
import ognl.OgnlException;
import ognl.Ognl;
import ognl.OgnlContext;

class User{
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
public class demo1 {
    public static void main(String[] args) throws Exception {
        String exp1 = "1+2";
        OgnlContext context = new OgnlContext();
        Object ognl = Ognl.parseExpression(exp1);
        Object value = Ognl.getValue(ognl,context,context.getRoot());
        System.out.println(value);
        banner();

        User user = new User();
        user.setName("webber");
        context.put("user",user);
        String exp2 = "#user.name";
        Object ognl2 = Ognl.parseExpression(exp2);
        value = Ognl.getValue(ognl2,context,context.getRoot());
        System.out.println(value);
        banner();
    }
    public static void banner(){
        System.out.println("============================================");
    }
}
