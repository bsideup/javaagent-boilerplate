package com.example.superagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import spark.webserver.JettyHandlerInterceptor;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(named("spark.webserver.JettyHandler"))
                .transform(new AgentBuilder.Transformer() {
                               @Override
                               public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
                                   return builder
                                           .method(named("doHandle"))
                                           .intercept(
                                                   MethodDelegation.to(JettyHandlerInterceptor.class)
                                                           .andThen(SuperMethodCall.INSTANCE)
                                           );
                               }
                           }
                )
                .installOn(instrumentation);
    }
}
