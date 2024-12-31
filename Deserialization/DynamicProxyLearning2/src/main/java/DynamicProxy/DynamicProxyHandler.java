package DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxyHandler implements InvocationHandler {
    private final Object target;
    public DynamicProxyHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[Dynamic Proxy] Logging before method: " + method.getName());
        System.out.println("[Dynamic Proxy] Method: " + method.getName() + "\nArgs: " + args);
        Object result = method.invoke(target, args);
        System.out.println("[Dynamic Proxy] Logging after method: " + method.getName());
        return result;
    }
}
