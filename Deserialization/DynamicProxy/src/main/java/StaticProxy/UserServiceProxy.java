package StaticProxy;

public class UserServiceProxy implements UserService {
    private UserService userService;
    public UserServiceProxy(){

    }
    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void log(String msg){
        System.out.println("[+] called " + msg);
    }
    @Override
    public void add() {
        log("add");
        userService.add();
    }

    @Override
    public void delete() {
        log("delete");
        userService.delete();
    }

    @Override
    public void update() {
        log("update");
        userService.update();
    }

    @Override
    public void query() {
        log("query");
        userService.query();
    }
}
