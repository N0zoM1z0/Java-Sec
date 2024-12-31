package StaticProxy;

public class StaticProxyDemo {
    public static void main(String[] args) throws Exception {
        CrudServiceImpl crudService = new CrudServiceImpl();
        CrudServiceProxy proxy = new CrudServiceProxy(crudService);

        proxy.Create("Create Data");
        proxy.Read(1);
        proxy.Update(1,"Update Data");
        proxy.Delete(1);
    }
}
