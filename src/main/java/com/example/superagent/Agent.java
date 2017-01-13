package com.example.superagent;

import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(named("spark.webserver.JettyHandler"))
                .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                                .advice(named("doHandle"), JettyHandlerAdvice.class.getName())
                )
                .installOn(instrumentation);
    }
}

