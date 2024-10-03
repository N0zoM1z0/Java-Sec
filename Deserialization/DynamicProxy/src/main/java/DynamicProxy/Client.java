package DynamicProxy;

import StaticProxy.UserServiceProxy;

public class Client {
    public static void main(String[] args) throws Exception {
        // what we proxy is interface!!!
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        UserProxyInvocationHandler userProxyInvocationHandler = new UserProxyInvocationHandler();
        userProxyInvocationHandler.setUserService((UserService) userServiceImpl);

        UserService proxy = (UserService) userProxyInvocationHandler.getProxy();

        proxy.add();
        proxy.delete();
        proxy.update();
        proxy.query();

    }
}
