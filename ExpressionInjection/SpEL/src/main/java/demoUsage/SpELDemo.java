package demoUsage;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELDemo {
    public static void main(String[] args) throws Exception {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression exp1 = expressionParser.parseExpression("'Hello'.concat(' World')");
        System.out.println(exp1.getValue(String.class));
        // ----
        Expression exp2 = expressionParser.parseExpression("10*7+1");
        System.out.println(exp2.getValue(Integer.class));
        // ----
        Expression exp3 = expressionParser.parseExpression("'java'.toUpperCase()");
        System.out.println(exp3.getValue(String.class));
        // ----
        Expression exp4 = expressionParser.parseExpression("T(java.lang.Math).PI");
        System.out.println(exp4.getValue(Double.class));
        // ----
        Expression exp5 = expressionParser.parseExpression("T(java.lang.Math).sqrt(17)");
        System.out.println(exp5.getValue(Double.class));
        // ----
        Expression exp6 = expressionParser.parseExpression("T(java.lang.Runtime).getRuntime().exec('/usr/bin/gnome-calculator')");
        System.out.println(exp6.getValue(Object.class));
    }
}
