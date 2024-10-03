package StaticProxy;

public class Client {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        /*
        The defect of static proxy is :
            when we add a client, we have to add a proxy...
            It is ineffective...
         */
        UserServiceProxy proxy = new UserServiceProxy();
        proxy.setUserService(userService);
        proxy.delete();
    }
}
