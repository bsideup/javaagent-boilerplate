package spark.webserver;

import net.bytebuddy.implementation.bind.annotation.AllArguments;

import javax.servlet.http.HttpServletResponse;

public class JettyHandlerInterceptor {

    public static void doHandle(@AllArguments Object[] args) {
        System.out.println("!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!");
        ((HttpServletResponse) args[3]).setHeader("X-My-Super-Header", "header value");
    }
}
