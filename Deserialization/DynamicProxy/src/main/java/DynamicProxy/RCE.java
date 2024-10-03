package DynamicProxy;

public class RCE {
    public static void main(String[] args) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        VulnProxyInvocationHandler vulnProxyInvocationHandler = new VulnProxyInvocationHandler();
        vulnProxyInvocationHandler.setUserService(userServiceImpl);
        UserService proxy = (UserService) vulnProxyInvocationHandler.getProxy();
        try {
            /*
            ProxyInvocationHandler will first call its `invoke` method.
            A[O] -> O.abc
                    O.invoke -> O.f
            A[B] -> B.f

             */
            proxy.add();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
