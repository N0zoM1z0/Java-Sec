package CBUsageDemo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


public class demo1 {
    public static void main(String[] args) throws Exception {
        System.out.println(PropertyUtils.getProperty(new Person(),"name"));
        Person p2 = new Person();
        PropertyUtils.setProperty(p2,"age",18);
        System.out.println(PropertyUtils.getProperty(p2,"age"));
    }
}
