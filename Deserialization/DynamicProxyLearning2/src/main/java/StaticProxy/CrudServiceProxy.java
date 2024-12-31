package StaticProxy;

public class CrudServiceProxy implements CrudService {
    private final CrudService crudService;

    public CrudServiceProxy(CrudService crudService) {
        this.crudService = crudService;
    }

    @Override
    public void Create(String data){
        System.out.println("[Proxy] Logging before create...");
        crudService.Create(data);
        System.out.println("[Proxy] Logging after create...");
    }

    @Override
    public void Read(int id) {
        System.out.println("[Proxy] Logging before read...");
        crudService.Read(id);
        System.out.println("[Proxy] Logging after read...");
    }

    @Override
    public void Update(int id, String data) {
        System.out.println("[Proxy] Logging before update...");
        crudService.Update(id, data);
        System.out.println("[Proxy] Logging after update...");
    }

    @Override
    public void Delete(int id) {
        System.out.println("[Proxy] Logging before delete...");
        crudService.Delete(id);
        System.out.println("[Proxy] Logging after delete...");
    }

}
