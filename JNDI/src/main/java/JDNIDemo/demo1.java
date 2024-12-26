package JDNIDemo;

import javafx.print.Printer;

import javax.naming.Context;
import javax.naming.InitialContext;

public class demo1 {
    public static void main(String[] args) throws Exception {
        Context ctx = new InitialContext();
        Printer printer = (Printer) ctx.lookup("myprinter");
        
    }
}
