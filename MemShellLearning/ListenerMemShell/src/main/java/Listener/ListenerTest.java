package Listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import java.util.EventListener;

@WebListener("/listenerTest")
public class ListenerTest implements ServletRequestListener {
    public ListenerTest(){}

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // trigger point
        // we put malicious code here
        System.out.println("Listener 被调用");
    }
}
