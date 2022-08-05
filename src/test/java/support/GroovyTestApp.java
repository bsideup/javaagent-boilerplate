package support;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class GroovyTestApp<SELF extends GroovyTestApp<SELF>> extends GenericContainer<SELF> {

    public GroovyTestApp(String script) {
        super("zeroturnaround/groovy:2.4.5");

        withClasspathResourceMapping("agent.jar", "/agent.jar", BindMode.READ_ONLY);
        withClasspathResourceMapping(script, "/app/app.groovy", BindMode.READ_ONLY);

        // Cache Grapes
        addFileSystemBind(System.getProperty("user.home") + "/.groovy", "/root/.groovy/", BindMode.READ_WRITE);

        withEnv("JAVA_OPTS", "-javaagent:/agent.jar");

        withCommand("/opt/groovy/bin/groovy /app/app.groovy");

        withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(script)));
    }

    public String getURL() {
        return "http://" + getHost() + ":" + getMappedPort(getExposedPorts().get(0));
    }
}
