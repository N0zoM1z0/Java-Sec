package StaticProxy;

public class CrudServiceImpl implements CrudService {

    @Override
    public void Create(String data) {
        System.out.println("Creating data: " + data);
    }

    @Override
    public void Read(int id) {
        System.out.println("Reading data with id: " + id);
    }

    @Override
    public void Update(int id, String data) {
        System.out.println("Updating data with id " + id + " to: " + data);
    }

    @Override
    public void Delete(int id) {
        System.out.println("Deleting data with id: " + id);
    }
}
