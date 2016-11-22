package com.example.superagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(named("spark.webserver.JettyHandler"))
                .transform(new Transformer() {
                               @Override
                               public Builder<?> transform(Builder<?> builder, TypeDescription desc, ClassLoader cl) {
                                   return builder
                                           .method(named("doHandle"))
                                           .intercept(Advice.to(JettyHandlerAdvice.class));
                               }
                           }
                )
                .installOn(instrumentation);
    }
}

