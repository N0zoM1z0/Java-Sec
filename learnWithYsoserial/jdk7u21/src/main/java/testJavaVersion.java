public class testJavaVersion {
    public static void main(String[] args) throws Exception {
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java version: " + javaVersion); // 1.7.0

        long Magic = 0xf5a5a608L;
        System.out.println(Long.toHexString(Magic).hashCode() == 0);
    }
}
