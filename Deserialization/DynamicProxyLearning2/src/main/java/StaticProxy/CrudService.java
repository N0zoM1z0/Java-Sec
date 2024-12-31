package StaticProxy;

public interface CrudService {
    void Create(String data);
    void Read(int id);
    void Update(int id,String data);
    void Delete(int id);

}
