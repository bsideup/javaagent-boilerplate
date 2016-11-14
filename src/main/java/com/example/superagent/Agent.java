package com.example.superagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));

                if (className.equals("spark/webserver/JettyHandler")) {
                    try {
                        CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

                        CtMethod ctMethod = ct.getDeclaredMethod("doHandle");
                        ctMethod.insertBefore("{ $4.setHeader(\"X-My-Super-Header\", \"header value\"); }");

                        return ct.toBytecode();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                return classfileBuffer;
            }
        });
    }
}
