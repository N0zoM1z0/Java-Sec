package DynamicProxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class VulnProxyInvocationHandler implements InvocationHandler {
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
        try {
            System.out.println("[+] HACKED!");
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String in;
            while ((in = br.readLine()) != null) {
                System.out.println(in);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log(method.getName());
        Object result = method.invoke(userService, args);
        return result;
    }
}
