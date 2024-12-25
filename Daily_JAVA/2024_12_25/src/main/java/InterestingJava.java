public class InterestingJava {
    public static void main(String[] args) throws Exception {
        Integer i1 = 1000, i2 = 1000;
        System.out.println(i1==i2);
        Integer i3 = 100, i4 = 100; // Integer cache
        System.out.println(i3==i4);
    }
}
