package SetterAndGetter;

import java.io.IOException;

public class Sex {
    int sex;
    public Sex(){
        try {
            Runtime.getRuntime().exec("whoami");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
