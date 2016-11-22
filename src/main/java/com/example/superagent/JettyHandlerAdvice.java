package com.example.superagent;

import net.bytebuddy.asm.Advice;

import javax.servlet.http.HttpServletResponse;

class JettyHandlerAdvice {

    @Advice.OnMethodEnter
    public static void doHandle(@Advice.AllArguments Object[] args) {
        ((HttpServletResponse) args[3]).setHeader("X-My-Super-Header", "header value");
    }
}
