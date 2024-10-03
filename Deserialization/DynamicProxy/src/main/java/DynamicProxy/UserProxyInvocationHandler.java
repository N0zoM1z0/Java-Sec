package DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserProxyInvocationHandler implements InvocationHandler {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public Object getProxy(){
        Object object = Proxy.newProxyInstance(this.getClass().getClassLoader(), userService.getClass().getInterfaces(), this);
        return object;
    }
    public void log(String msg){
        System.out.println("[+] called " + msg);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log(method.getName());
        Object result = method.invoke(userService, args);
        return result;
    }
}
