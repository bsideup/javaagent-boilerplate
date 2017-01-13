package com.example.superagent;

import net.bytebuddy.asm.Advice;

import javax.servlet.http.HttpServletResponse;

class JettyHandlerAdvice {

    @Advice.OnMethodEnter
    public static void doHandle(@Advice.Argument(3) HttpServletResponse response) {
        response.setHeader("X-My-Super-Header", "header value");
    }
}
