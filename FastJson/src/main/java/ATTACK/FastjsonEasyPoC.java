package ATTACK;

import com.alibaba.fastjson.JSON;

public class FastjsonEasyPoC {
    public static void main(String[] args) {
        String jsonString ="{\"@type\":\"ATTACK.Student\",\"age\":6,\"name\":\"Name\",\"address\":\"china\",\"properties\":{}}";
        Object o = JSON.parseObject(jsonString,Object.class);
        System.out.println(o);
        System.out.println(o.getClass().getName());
    }
}
