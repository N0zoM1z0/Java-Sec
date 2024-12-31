package DynamicProxy;

import StaticProxy.*;

import java.lang.reflect.Proxy;

public class DynamicProxyDemo {
    public static void main(String[] args) throws Exception {
        CrudServiceImpl crudService = new CrudServiceImpl();

        // Create proxy instance
        CrudService proxyInstance = (CrudService) Proxy.newProxyInstance(
            crudService.getClass().getClassLoader(),
                crudService.getClass().getInterfaces(),
            new DynamicProxyHandler(crudService)
        );
//        CrudService proxyInstance = (CrudService) ProxyFactory.CreateProxy(crudService);

        proxyInstance.Create("Create Data");
        proxyInstance.Read(1);
        proxyInstance.Update(1,"Update Data");
        proxyInstance.Delete(1);
    }
}
